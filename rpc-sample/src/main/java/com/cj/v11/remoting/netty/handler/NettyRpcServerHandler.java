package com.cj.v11.remoting.netty.handler;

import com.cj.v11.constant.RpcConstant;
import com.cj.v11.dto.RpcResponse;
import com.cj.v11.dto.RpcMessage;
import com.cj.v11.dto.RpcRequest;
import com.cj.v11.provider.AbstractServiceProvider;
import com.cj.v11.serializer.Serializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private AbstractServiceProvider serviceProvider;
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        try {
            logger.info("Netty 服务端接收: [{}]", rpcRequest);
            // 得到服务名
            String serviceName = rpcRequest.getInterfaceName();
            // 得到服务实现类
            Object service = serviceProvider.getService(serviceName);
            // 反射调用方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getArgsTypes());
            Object obj = method.invoke(service, rpcRequest.getArgs());
            // 封装结果
            RpcResponse response = RpcResponse.ok(obj);
            // 写入管道
            Channel channel = channelHandlerContext.channel();

            // 构造消息
            RpcMessage rpcMessage = RpcMessage.builder()
                    .magicNum(RpcConstant.MESSAGE_MAGIC_NUM)
                    .version(RpcConstant.MESSAGE_VERSION)
                    .messageType(RpcConstant.MESSAGE_TYPE_RESPONSE)
                    .serializeTpe(Serializer.SERIALIZER_KRYO)
                    .data(response)
                    .build();

            channel.writeAndFlush(rpcMessage);
            logger.info("Netty 服务端发送：[{}]", rpcMessage);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
