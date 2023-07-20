package com.cj.v3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCServer {
    private static final Logger logger = LoggerFactory.getLogger(RPCServer.class);
    public static void main(String[] args) throws IOException {
        // 注册服务
        UserService userService = new UserServiceImpl();
        ServerSocket serverSocket = new ServerSocket(8888);
        try {
            logger.info("服务器启动成功...");
            // BIO 方式等待客户端连接
            while (true) {
                // 进入阻塞，等待连接
                Socket socket = serverSocket.accept();
                logger.info("服务器连接成功：{}", socket);
                // 开启线程去处理
                new Thread(() -> {
                    try {
                        // 得到 IO 流
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        // 请求对象
                        RPCRequest request = (RPCRequest) ois.readObject();
                        // 反射调用方法
                        Method method = userService.getClass().getMethod(request.getMethodName(), request.getArgsTypes());
                        Object obj = method.invoke(userService, request.getArgs());
                        // 封装结果
                        RPCResponse response = RPCResponse.ok(obj);
                        // 响应结果
                        oos.writeObject(response);
                        oos.flush();
                        logger.info("服务器发送：{}", response);
                        // 关闭通道
                        oos.close();
                        ois.close();
                    } catch (IOException | ClassNotFoundException e) {
                        logger.info("客户端连接错误...");
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            logger.info("服务器启动失败...");
            throw new RuntimeException(e);
        }
    }
}
