package com.cj.v11.remoting.proxy;

import com.cj.v11.dto.RpcResponse;
import com.cj.v11.dto.RpcRequest;
import com.cj.v11.remoting.AbstractRpcClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;

@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    private AbstractRpcClient abstractRpcClient;
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建请求
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .args(args)
                .argsTypes(method.getParameterTypes())
                .ip(InetAddress.getLocalHost().getHostAddress())
                .build();
        // 发送请求
        RpcResponse response = abstractRpcClient.sendRPCRequest(request);
        // 返回数据
        return response.getData();
    }

    public <T>T getProxyService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                this);
    }
}
