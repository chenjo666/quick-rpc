package com.cj.v7.server.handler;

import com.cj.v7.dto.RPCRequest;
import com.cj.v7.dto.RPCResponse;
import com.cj.v7.provider.ServiceProvider;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

    private ServiceProvider serviceProvider;
    private static final Logger logger = LoggerFactory.getLogger(NettyRPCServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {
        try {
            logger.info("Netty 服务端接收: 【{}】", rpcRequest);
            // 得到服务名
            String serviceName = rpcRequest.getInterfaceName();
            // 得到服务实现类
            Object service = serviceProvider.getService(serviceName);
            // 反射调用方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getArgsTypes());
            Object obj = method.invoke(service, rpcRequest.getArgs());
            // 封装结果
            RPCResponse response = RPCResponse.ok(obj);
            // 写入管道
            Channel channel = channelHandlerContext.channel();
            channel.writeAndFlush(response);
            logger.info("Netty 服务端发送：【{}】", response);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
