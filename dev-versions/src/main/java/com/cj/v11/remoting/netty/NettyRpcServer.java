package com.cj.v11.remoting.netty;

import com.cj.v11.provider.AbstractServiceProvider;
import com.cj.v11.remoting.AbstractRpcServer;
import com.cj.v11.remoting.netty.handler.NettyRpcServerHandler;
import com.cj.v11.remoting.netty.codec.RPCDecoder;
import com.cj.v11.remoting.netty.codec.RPCEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyRpcServer extends AbstractRpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);


    public NettyRpcServer(AbstractServiceProvider serviceProvider) {
        super(serviceProvider);
    }

    @Override
    public void startServer() {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 解码器
                        nioSocketChannel.pipeline().addLast(new RPCDecoder());
                        // 管道数据处理器（rpcRequest）
                        nioSocketChannel.pipeline().addLast(new NettyRpcServerHandler(getServiceProvider()));
                        // 编码器
                        nioSocketChannel.pipeline().addLast(new RPCEncoder());
                    }
                })
                .bind(getServiceProvider().getPort());
        logger.info("Netty 服务端等待连接...");
    }

    @Override
    public void stopServer() {

    }
}
