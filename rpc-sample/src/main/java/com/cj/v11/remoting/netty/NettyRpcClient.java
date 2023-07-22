package com.cj.v11.remoting.netty;

import com.cj.v11.remoting.AbstractRpcClient;
import com.cj.v11.remoting.netty.handler.NettyRpcClientHandler;
import com.cj.v11.remoting.netty.codec.RPCDecoder;
import com.cj.v11.remoting.netty.codec.RPCEncoder;
import com.cj.v11.constant.RpcConstant;
import com.cj.v11.dto.RpcMessage;
import com.cj.v11.dto.RpcRequest;
import com.cj.v11.dto.RpcResponse;
import com.cj.v11.registry.discovery.ServiceDiscovery;
import com.cj.v11.serializer.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@NoArgsConstructor
public class NettyRpcClient extends AbstractRpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);

    public NettyRpcClient(ServiceDiscovery serviceDiscovery) {
        super(serviceDiscovery);
    }


    @Override
    public RpcResponse sendRPCRequest(RpcRequest request) {
        InetSocketAddress address = getServiceDiscovery().discoverService(request);
        String host = address.getHostName();
        int port = address.getPort();

        CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 解码器
                        nioSocketChannel.pipeline().addLast(new RPCDecoder());
                        // 入站处理器
                        nioSocketChannel.pipeline().addLast(new NettyRpcClientHandler(completableFuture));
                        // 编码器
                        nioSocketChannel.pipeline().addLast(new RPCEncoder());
                    }
                })
                .connect(new InetSocketAddress(host, port));
        try {
            // 同步阻塞，直到连接建立完成发送结果
            channelFuture.sync();
            logger.info("Netty 客户端连接服务: {}:{}", host, port);

            Channel channel = channelFuture.channel();
            // 构造发送数据
            RpcMessage rpcMessage = RpcMessage.builder()
                    .magicNum(RpcConstant.MESSAGE_MAGIC_NUM)
                    .version(RpcConstant.MESSAGE_VERSION)
                    .messageType(RpcConstant.MESSAGE_TYPE_REQUEST)
                    .serializeTpe(Serializer.SERIALIZER_KRYO)
                    .data(request)
                    .build();

            channel.writeAndFlush(rpcMessage);
            logger.info("Netty 客户端发送：[{}]", rpcMessage);
            // 异步获取处理结果
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
