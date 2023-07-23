package com.cj.v12;

import com.cj.v12.api.service.BookService;
import com.cj.v12.api.service.UserService;
import com.cj.v12.api.service.impl.BookServiceImpl;
import com.cj.v12.api.service.impl.UserServiceImpl;
import com.cj.v12.provider.AbstractServiceProvider;
import com.cj.v12.provider.DefaultServiceProvider;
import com.cj.v12.remoting.AbstractRpcServer;
import com.cj.v12.remoting.netty.NettyRpcServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    private static final int port = 8888;
    public static void main(String[] args) throws UnknownHostException {
        // 注册服务
        UserService userService = new UserServiceImpl();
        BookService bookService = new BookServiceImpl();
        // 添加服务
        AbstractServiceProvider serviceProvider = new DefaultServiceProvider(InetAddress.getLocalHost().getHostAddress(), port);
        serviceProvider.addService(userService.getClass().getInterfaces()[0].getName(), userService);
        serviceProvider.addService(bookService.getClass().getInterfaces()[0].getName(), bookService);
        // 发布服务
        serviceProvider.pubService(userService.getClass().getInterfaces()[0].getName());
        serviceProvider.pubService(bookService.getClass().getInterfaces()[0].getName());
        // 测试服务
        AbstractRpcServer abstractRpcServer = new NettyRpcServer(serviceProvider);
        abstractRpcServer.startServer();
    }
}
