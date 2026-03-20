package com.fff.examples.pringboot.provider.service;

import com.fff.example.api.UserService;
import com.fff.example.api.dto.User;
import com.fff.simplerpc.springboot.annotation.RpcService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User getUserInfo(String name) {
        return User.builder().name(name).build();
    }
}
