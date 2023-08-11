package com.cj.v10.server;

import com.cj.v10.provider.ServiceProvider;
import com.cj.v10.server.handler.BIOThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadBIOServer extends AbstractRPCServer {
    // 服务提供商

    private static final Logger logger = LoggerFactory.getLogger(SingleThreadBIOServer.class);

    public SingleThreadBIOServer(int port, ServiceProvider serviceProvider) {
        super(port, serviceProvider);
    }

    @Override
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(getPort());
            logger.info("服务器启动成功...");
            // BIO 方式等待客户端连接
            while (true) {
                // 进入阻塞，等待连接
                Socket socket = serverSocket.accept();
                logger.info("服务器连接成功：{}", socket);
                // 开启线程去处理
                new Thread(new BIOThreadHandler(socket, getServiceProvider())).start();
            }
        } catch (IOException e) {
            logger.info("服务器启动失败...");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopServer() {

    }
}
