package com.cj.v7.client;

import com.cj.v7.dto.RPCRequest;
import com.cj.v7.dto.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {
    private AbstractRPCClient abstractRpcClient;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建请求
        RPCRequest request = RPCRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .args(args)
                .argsTypes(method.getParameterTypes())
                .build();
        // 发送请求
        RPCResponse response = abstractRpcClient.sendRPCRequest(request);
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
