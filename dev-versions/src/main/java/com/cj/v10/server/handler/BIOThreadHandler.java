package com.cj.v10.server.handler;

import com.cj.v10.dto.RPCRequest;
import com.cj.v10.dto.RPCResponse;
import com.cj.v10.provider.ServiceProvider;
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
public class BIOThreadHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BIOThreadHandler.class);

    private Socket socket;
    private ServiceProvider serviceProvider;
    @Override
    public void run() {
        try {
            // 得到 IO 流
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 请求对象
            RPCRequest request = (RPCRequest) ois.readObject();
            // 得到服务名
            String serviceName = request.getInterfaceName();
            logger.info("服务名: {}", serviceName);
            // 得到服务实现类
            Object service = serviceProvider.getService(serviceName);
            // 得到服务方法并调用
            Method method = service.getClass().getMethod(request.getMethodName(), request.getArgsTypes());
            Object obj = method.invoke(service, request.getArgs());
            // 封装结果
            RPCResponse response = RPCResponse.ok(obj);
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
