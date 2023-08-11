package com.cj.v5.server;

import com.cj.v5.provider.ServiceProvider;
import com.cj.v5.server.handler.BIOThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolBIOServer extends AbstractRPCServer {
    // 服务提供商
    private final ThreadPoolExecutor threadPool;
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolBIOServer.class);

    public ThreadPoolBIOServer(int port, ServiceProvider serviceProvider) {
        super(port, serviceProvider);
        this.threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }
    public ThreadPoolBIOServer(int port, ServiceProvider serviceProvider, ThreadPoolExecutor threadPool) {
        super(port, serviceProvider);
        this.threadPool = threadPool;
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
                threadPool.execute(new BIOThreadHandler(socket, getServiceProvider()));
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
