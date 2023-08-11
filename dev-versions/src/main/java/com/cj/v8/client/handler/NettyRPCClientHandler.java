package com.cj.v8.client.handler;

import com.cj.v8.dto.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class NettyRPCClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    private CompletableFuture<RPCResponse> completableFuture;
    private static final Logger logger = LoggerFactory.getLogger(NettyRPCClientHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCResponse rpcResponse) {
        logger.info("Netty 客户端接收: [{}]", rpcResponse);
        completableFuture.complete(rpcResponse);
    }
}
