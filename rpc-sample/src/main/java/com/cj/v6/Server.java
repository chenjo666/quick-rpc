package com.cj.v6;

import com.cj.v6.provider.ServiceProvider;
import com.cj.v6.provider.impl.DefaultServiceProvider;
import com.cj.v6.server.AbstractRPCServer;
import com.cj.v6.server.SingleThreadBIOServer;
import com.cj.v6.service.BookService;
import com.cj.v6.service.UserService;
import com.cj.v6.service.impl.BookServiceImpl;
import com.cj.v6.service.impl.UserServiceImpl;

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
        AbstractRPCServer rpcServer = new SingleThreadBIOServer(8888, serviceProvider);
        rpcServer.startServer();
    }
}
