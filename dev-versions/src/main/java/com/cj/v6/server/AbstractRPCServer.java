package com.cj.v6.server;

import com.cj.v6.provider.ServiceProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AbstractRPCServer {
    private int port;

    private ServiceProvider serviceProvider;
    public abstract void startServer();
    public abstract void stopServer();
}
