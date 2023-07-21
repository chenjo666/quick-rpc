package com.cj.v8.client;

import com.cj.v8.client.handler.NettyRPCClientHandler;
import com.cj.v8.codec.RPCDecoder;
import com.cj.v8.codec.RPCEncoder;
import com.cj.v8.constant.RPCConstant;
import com.cj.v8.dto.RPCMessage;
import com.cj.v8.dto.RPCRequest;
import com.cj.v8.dto.RPCResponse;
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
    public NettyRPCClient(String host, int port) {
        super(host, port);
    }

    @Override
    public RPCResponse sendRPCRequest(RPCRequest request) {
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
                .connect(new InetSocketAddress(getHost(), getPort()));
        try {
            // 同步阻塞，直到连接建立完成发送结果
            channelFuture.sync();
            logger.info("Netty 客户端连接建立成功...");
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
