package com.cj.v12.remoting.bio.handler;

import com.cj.v12.remoting.dto.RpcRequest;
import com.cj.v12.remoting.dto.RpcResponse;
import com.cj.v12.provider.AbstractServiceProvider;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class BioThreadHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BioThreadHandler.class);

    private Socket socket;
    private AbstractServiceProvider serviceProvider;
    @Override
    public void run() {
        try {
            // 得到 IO 流
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 请求对象
            RpcRequest request = (RpcRequest) ois.readObject();
            // 得到服务名
            String serviceName = request.getInterfaceName();
            logger.info("服务名: {}", serviceName);
            // 得到服务实现类
            Object service = serviceProvider.getService(serviceName);
            // 得到服务方法并调用
            Method method = service.getClass().getMethod(request.getMethodName(), request.getArgsTypes());
            Object obj = method.invoke(service, request.getArgs());
            // 封装结果
            RpcResponse response = RpcResponse.ok(obj);
            // 响应结果
            oos.writeObject(response);
            oos.flush();
            logger.info("服务器发送：{}", response);
            // 关闭通道
            oos.close();
            ois.close();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
