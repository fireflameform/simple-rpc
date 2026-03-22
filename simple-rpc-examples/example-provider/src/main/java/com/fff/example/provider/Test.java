package com.fff.example.provider;


import com.fff.example.provider.service.UserServiceImpl;
import com.fff.simplerpc.transport.netty.server.NettyRpcServer;

public class Test {
    public static void main(String[] args) {
        NettyRpcServer rpcServer = new NettyRpcServer();
        rpcServer.register("UserService", new UserServiceImpl());
        rpcServer.start();
    }
}
