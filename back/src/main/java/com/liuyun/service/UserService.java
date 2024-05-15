package com.liuyun.service;

import com.liuyun.pojo.User;

import java.util.List;

/**
 * @PackageName com.liuyun.service
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/6 14:33
 * @Version 1.0
 */
public interface UserService {
    List<User> queryAllUsers();

    User queryByUserName(String userName);

    int register(User registerUser);

    int updateUser(User user);

    int getUseSpaceByUserName(String userName);
}
