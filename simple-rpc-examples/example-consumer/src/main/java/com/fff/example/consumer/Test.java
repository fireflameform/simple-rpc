package com.fff.example.consumer;

import com.fff.example.api.UserService;
import com.fff.example.api.dto.User;
import com.fff.simplerpc.proxy.RpcProxyFactory;

public class Test {
    public static void main(String[] args) {

        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory();
        UserService userService = rpcProxyFactory.createProxy(UserService.class);
        User alice = userService.getUserInfo("alice");
        System.out.println(alice);

    }
}
