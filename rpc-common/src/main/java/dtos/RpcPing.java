package dtos;

import lombok.Data;

import java.net.InetAddress;
import java.net.UnknownHostException;
@Data
public class RpcPing {
    private final String message;

    public RpcPing() {
        try {
            message = "ping: " + InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
