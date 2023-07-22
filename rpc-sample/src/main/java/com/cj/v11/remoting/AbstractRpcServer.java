package com.cj.v11.remoting;

import com.cj.v11.provider.AbstractServiceProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AbstractRpcServer {
    private AbstractServiceProvider serviceProvider;

    public abstract void startServer();
    public abstract void stopServer();
}
