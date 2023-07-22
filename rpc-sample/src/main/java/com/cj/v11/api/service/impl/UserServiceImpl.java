package com.cj.v11.api.service.impl;

import com.cj.v11.api.pojo.User;
import com.cj.v11.api.service.UserService;

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
