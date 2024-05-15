package com.liuyun.mapper;

import com.liuyun.pojo.User;

import java.util.List;

/**
 * @PackageName com.liuyun.mapper
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/6 13:40
 * @Version 1.0
 */
public interface UserMapper {
    List<User> queryAllUsers();

    User queryByUserName(String userName);

    int register(User User);

    int updateUser(User user);

    int getUseSpaceByUserName(String userName);
}
