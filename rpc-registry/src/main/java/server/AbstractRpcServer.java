package server;

import lombok.AllArgsConstructor;
import lombok.Data;
import provider.ServiceProvider;

@AllArgsConstructor
@Data
public abstract class AbstractRpcServer {
    private ServiceProvider serviceProvider;

    public abstract void startServer();
    public abstract void stopServer();
}
