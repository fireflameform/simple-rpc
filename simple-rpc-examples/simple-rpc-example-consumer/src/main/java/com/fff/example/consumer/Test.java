package com.fff.example.consumer;

import com.fff.example.api.UserService;
import com.fff.example.api.dto.User;
import com.fff.simplerpc.proxy.RpcProxyFactory;
import com.fff.simplerpc.transport.netty.client.RpcClient;

public class Test {
    public static void main(String[] args) {

        RpcClient rpcClient = new RpcClient();
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(rpcClient);
        UserService userService = rpcProxyFactory.createProxy(UserService.class);
        User alice = userService.getUserInfo("alice");
        System.out.println(alice);

    }
}
