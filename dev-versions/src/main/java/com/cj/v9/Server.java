package com.cj.v9;

import com.cj.v9.provider.ServiceProvider;
import com.cj.v9.provider.impl.DefaultServiceProvider;
import com.cj.v9.registry.ServiceRegistry;
import com.cj.v9.registry.ZkServiceRegistry;
import com.cj.v9.server.AbstractRPCServer;
import com.cj.v9.server.NettyRPCServer;
import com.cj.v9.service.BookService;
import com.cj.v9.service.UserService;
import com.cj.v9.service.impl.BookServiceImpl;
import com.cj.v9.service.impl.UserServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    public static void main(String[] args) throws UnknownHostException {
        // 注册服务
        UserService userService = new UserServiceImpl();
        BookService bookService = new BookServiceImpl();
        // 添加服务
        ServiceProvider serviceProvider = new DefaultServiceProvider(
                InetAddress.getLocalHost().getHostAddress(),
                8888,
                new ZkServiceRegistry());
        serviceProvider.addService(userService.getClass().getInterfaces()[0].getName(), userService);
        serviceProvider.addService(bookService.getClass().getInterfaces()[0].getName(), bookService);
        // 发布服务
        serviceProvider.pubService(userService.getClass().getInterfaces()[0].getName());
        serviceProvider.pubService(bookService.getClass().getInterfaces()[0].getName());
        // 测试服务
        AbstractRPCServer abstractRpcServer = new NettyRPCServer(8888, serviceProvider);
        abstractRpcServer.startServer();
    }
}
