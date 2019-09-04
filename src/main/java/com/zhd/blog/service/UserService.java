package com.zhd.blog.service;

import com.zhd.blog.po.User;

public interface UserService {

    User checkUsers(String username, String password);
}
