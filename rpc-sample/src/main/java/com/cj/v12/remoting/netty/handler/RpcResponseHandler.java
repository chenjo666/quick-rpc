package com.cj.v12.remoting.netty.handler;

import com.cj.v12.remoting.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Data
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private CompletableFuture<RpcResponse> completableFuture;
    private static final Logger logger = LoggerFactory.getLogger(RpcResponseHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) {
        logger.info("Netty 客户端接收: [{}]", rpcResponse);
        completableFuture.complete(rpcResponse);
    }
}
