package com.cj.v4.server;

import com.cj.v4.dto.RPCRequest;
import com.cj.v4.dto.RPCResponse;
import com.cj.v4.provider.ServiceProvider;
import com.cj.v4.provider.impl.DefaultServiceProvider;
import com.cj.v4.service.BookService;
import com.cj.v4.service.UserService;
import com.cj.v4.service.impl.BookServiceImpl;
import com.cj.v4.service.impl.UserServiceImpl;
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
        BookService bookService = new BookServiceImpl();
        // 存储服务
        ServiceProvider serviceProvider = new DefaultServiceProvider();
        serviceProvider.addService(userService.getClass().getInterfaces()[0].getName(), userService);
        serviceProvider.addService(bookService.getClass().getInterfaces()[0].getName(), bookService);

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
                        // 得到服务名
                        String serviceName = request.getInterfaceName();
                        logger.info("serviceName: {}", serviceName);
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
