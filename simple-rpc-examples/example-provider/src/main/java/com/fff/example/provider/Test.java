package com.fff.example.provider;


import com.fff.example.provider.service.UserServiceImpl;
import com.fff.simplerpc.transport.netty.server.RpcServer;

public class Test {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.register("UserService", new UserServiceImpl());
        rpcServer.start();
    }
}
