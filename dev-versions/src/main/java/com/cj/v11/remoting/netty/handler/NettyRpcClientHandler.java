package com.cj.v11.remoting.netty.handler;

import com.cj.v11.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private CompletableFuture<RpcResponse> completableFuture;
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClientHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) {
        logger.info("Netty 客户端接收: [{}]", rpcResponse);
        completableFuture.complete(rpcResponse);
    }
}
