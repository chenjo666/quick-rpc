package com.cj.v9.util;

import java.net.InetSocketAddress;

public class ServiceUtil {

    public static String getServerAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

    public static InetSocketAddress getServerAddress(String serverAddress) {
        String[] result = serverAddress.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
