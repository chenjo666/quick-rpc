package com.cj.v12.api.service;

import com.cj.v12.api.pojo.User;

public interface UserService {
    // 查询用户
    User getUser(String id);
    // 更新用户名
    User updateUserName(String id, String name);
}
