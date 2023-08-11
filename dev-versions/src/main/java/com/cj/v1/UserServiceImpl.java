package com.cj.v1;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(String id) {
        return User.builder().id(id).name("rpc").build();
    }
}
