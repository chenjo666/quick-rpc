package com.cj.v10;

import com.cj.v10.loadbalance.RoundLoadBalance;
import com.cj.v10.provider.ServiceProvider;
import com.cj.v10.provider.impl.DefaultServiceProvider;
import com.cj.v10.registry.ZkServiceRegistry;
import com.cj.v10.server.AbstractRPCServer;
import com.cj.v10.server.NettyRPCServer;
import com.cj.v10.service.BookService;
import com.cj.v10.service.UserService;
import com.cj.v10.service.impl.BookServiceImpl;
import com.cj.v10.service.impl.UserServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server2 {
    private static final int port = 8889;
    public static void main(String[] args) throws UnknownHostException {
        // 注册服务
        UserService userService = new UserServiceImpl();
        BookService bookService = new BookServiceImpl();
        // 添加服务
        ServiceProvider serviceProvider = new DefaultServiceProvider(
                InetAddress.getLocalHost().getHostAddress(),
                port,
                new ZkServiceRegistry(new RoundLoadBalance()));
        serviceProvider.addService(userService.getClass().getInterfaces()[0].getName(), userService);
        serviceProvider.addService(bookService.getClass().getInterfaces()[0].getName(), bookService);
        // 发布服务
        serviceProvider.pubService(userService.getClass().getInterfaces()[0].getName());
        serviceProvider.pubService(bookService.getClass().getInterfaces()[0].getName());
        // 测试服务
        AbstractRPCServer abstractRpcServer = new NettyRPCServer(port, serviceProvider);
        abstractRpcServer.startServer();
    }
}
