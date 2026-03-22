package com.fff.simplerpc.transport.api;

public interface RpcServer {
    void start();
    void close();
    void register(String interfaceName, Object impl);
}
