package com.fff.simplerpc.registry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    void registerService(String serviceName, InetSocketAddress address);
}
