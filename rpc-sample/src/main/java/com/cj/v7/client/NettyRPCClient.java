package com.cj.v7.client;

import com.cj.v7.client.handler.NettyRPCClientHandler;
import com.cj.v7.dto.RPCRequest;
import com.cj.v7.dto.RPCResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
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
                        nioSocketChannel.pipeline().addLast(new ObjectDecoder(Class::forName));
                        // 入站处理器
                        nioSocketChannel.pipeline().addLast(new NettyRPCClientHandler(completableFuture));
                        // 编码器
                        nioSocketChannel.pipeline().addLast(new ObjectEncoder());
                    }
                })
                .connect(new InetSocketAddress(getHost(), getPort()));
        try {
            // 同步阻塞，直到连接建立完成发送结果
            channelFuture.sync();
            logger.info("Netty 客户端连接建立成功...");
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request);
            logger.info("Netty 客户端发送：【{}】", request);
            // 异步获取处理结果
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
