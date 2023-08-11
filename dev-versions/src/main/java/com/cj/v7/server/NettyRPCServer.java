package com.cj.v7.server;

import com.cj.v7.provider.ServiceProvider;
import com.cj.v7.server.handler.NettyRPCServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyRPCServer extends AbstractRPCServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyRPCServer.class);


    public NettyRPCServer(int port, ServiceProvider serviceProvider) {
        super(port, serviceProvider);
    }

    @Override
    public void startServer() {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // jdk 解码器
                        nioSocketChannel.pipeline().addLast(new ObjectDecoder(Class::forName));
                        // 管道数据处理器（rpcRequest）
                        nioSocketChannel.pipeline().addLast(new NettyRPCServerHandler(getServiceProvider()));
                        // jdk 编码器
                        nioSocketChannel.pipeline().addLast(new ObjectEncoder());
                    }
                })
                .bind(getPort());
        logger.info("Netty 服务端等待连接...");
    }

    @Override
    public void stopServer() {

    }
}
