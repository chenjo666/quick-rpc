package com.cj.v10.client;

import com.cj.v10.client.handler.NettyRPCClientHandler;
import com.cj.v10.codec.RPCDecoder;
import com.cj.v10.codec.RPCEncoder;
import com.cj.v10.constant.RPCConstant;
import com.cj.v10.dto.RPCMessage;
import com.cj.v10.dto.RPCRequest;
import com.cj.v10.dto.RPCResponse;
import com.cj.v10.registry.ServiceRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NettyRPCClient extends AbstractRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRPCClient.class);

    public NettyRPCClient(ServiceRegistry serviceRegistry) {
        super(serviceRegistry);
    }
    @Override
    public RPCResponse sendRPCRequest(RPCRequest request) {
        InetSocketAddress address = getServiceRegistry().discoverService(request);
        String host = address.getHostName();
        int port = address.getPort();

        CompletableFuture<RPCResponse> completableFuture = new CompletableFuture<>();
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 解码器
                        nioSocketChannel.pipeline().addLast(new RPCDecoder());
                        // 入站处理器
                        nioSocketChannel.pipeline().addLast(new NettyRPCClientHandler(completableFuture));
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
            RPCMessage rpcMessage = RPCMessage.builder()
                    .magicNum(RPCConstant.MESSAGE_MAGIC_NUM)
                    .version(RPCConstant.MESSAGE_VERSION)
                    .messageType(RPCConstant.MESSAGE_TYPE_REQUEST)
                    .serializeTpe(RPCConstant.MESSAGE_SERIALIZE_KRYO)
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
