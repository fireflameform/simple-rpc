package com.fff.example.provider.service;


import com.fff.example.api.UserService;
import com.fff.example.api.dto.User;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserInfo(String name) {
        return User.builder().name(name).build();
    }
}
