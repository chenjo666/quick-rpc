package com.cj.v12.remoting.bio;

import com.cj.v12.provider.AbstractServiceProvider;
import com.cj.v12.remoting.AbstractRpcServer;
import com.cj.v12.remoting.bio.handler.BioThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolBioServer extends AbstractRpcServer {
    // 服务提供商
    private final ThreadPoolExecutor threadPool;
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolBioServer.class);

    public ThreadPoolBioServer(AbstractServiceProvider serviceProvider) {
        super(serviceProvider);
        this.threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }
    public ThreadPoolBioServer(AbstractServiceProvider serviceProvider, ThreadPoolExecutor threadPool) {
        super(serviceProvider);
        this.threadPool = threadPool;
    }

    @Override
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(getServiceProvider().getPort());
            logger.info("服务器启动成功...");
            // BIO 方式等待客户端连接
            while (true) {
                // 进入阻塞，等待连接
                Socket socket = serverSocket.accept();
                logger.info("服务器连接成功：{}", socket);
                // 开启线程去处理
                threadPool.execute(new BioThreadHandler(socket, getServiceProvider()));
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
