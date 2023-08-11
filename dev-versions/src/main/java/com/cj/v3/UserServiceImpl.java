package com.cj.v3;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(String id) {
        return User.builder().id(id).name("rpc").build();
    }

    @Override
    public User updateUserName(String id, String name) {
        return User.builder().id(id).name(name).build();
    }
}
