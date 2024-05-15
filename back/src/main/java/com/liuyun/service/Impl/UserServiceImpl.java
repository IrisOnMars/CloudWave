package com.liuyun.service.Impl;

import com.liuyun.mapper.UserMapper;
import com.liuyun.pojo.User;
import com.liuyun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @PackageName com.liuyun.service.Impl
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/6 14:34
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> queryAllUsers() {
        List<User> users=userMapper.queryAllUsers();
        return users;
    }

    @Override
    public User queryByUserName(String userName) {
        return userMapper.queryByUserName(userName);
    }

    @Override
    public int register(User registerUser) {
        return userMapper.register(registerUser);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int getUseSpaceByUserName(String userName) {
        return userMapper.getUseSpaceByUserName(userName);
    }
}
