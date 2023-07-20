package com.cj.v3;

public interface UserService {
    // 查询用户
    User getUserById(String id);
    // 更新用户名
    User updateUserName(String id, String name);
}
