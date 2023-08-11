package com.cj.v5.service.impl;

import com.cj.v5.pojo.User;
import com.cj.v5.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(String id) {
        return User.builder().id(id).name("rpc").build();
    }

    @Override
    public User updateUserName(String id, String name) {
        return User.builder().id(id).name(name).build();
    }
}
