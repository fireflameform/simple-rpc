package com.fff.examples.pringboot.provider.service;

import com.fff.example.api.UserService;
import com.fff.example.api.dto.User;
import com.fff.simplerpc.transport.netty.server.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RpcServer rpcServer;

    @PostConstruct
    public void init() {
        rpcServer.register(UserService.class.getSimpleName(), this);
        System.out.println("注入服务UserService");
    }

    @Override
    public User getUserInfo(String name) {
        return User.builder().name(name).build();
    }
}
