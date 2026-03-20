package com.fff.example.springboot.consumer.component;

import com.fff.example.api.UserService;
import com.fff.example.api.dto.User;
import com.fff.simplerpc.proxy.RpcProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RpcTestRunner implements ApplicationRunner {

    @Autowired
    RpcProxyFactory rpcProxyFactory;


    @Override
    public void run(ApplicationArguments args) {
        UserService userService = rpcProxyFactory.createProxy(UserService.class);
        User alice = userService.getUserInfo("Alice");
        System.out.println(alice);
    }
}
