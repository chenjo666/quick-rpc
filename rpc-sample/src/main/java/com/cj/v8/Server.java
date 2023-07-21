package com.cj.v8;

import com.cj.v8.provider.ServiceProvider;
import com.cj.v8.provider.impl.DefaultServiceProvider;
import com.cj.v8.server.AbstractRPCServer;
import com.cj.v8.server.NettyRPCServer;
import com.cj.v8.service.BookService;
import com.cj.v8.service.UserService;
import com.cj.v8.service.impl.BookServiceImpl;
import com.cj.v8.service.impl.UserServiceImpl;

public class Server {
    public static void main(String[] args) {
        // 注册服务
        UserService userService = new UserServiceImpl();
        BookService bookService = new BookServiceImpl();
        // 存储服务
        ServiceProvider serviceProvider = new DefaultServiceProvider();
        serviceProvider.addService(userService.getClass().getInterfaces()[0].getName(), userService);
        serviceProvider.addService(bookService.getClass().getInterfaces()[0].getName(), bookService);
        // 测试服务
        AbstractRPCServer abstractRpcServer = new NettyRPCServer(8888, serviceProvider);
        abstractRpcServer.startServer();
    }
}
