package com.pkh.pkec.phone.service.impl;

import com.pkh.pkec.phone.mapper.UserMapper;
import com.pkh.pkec.phone.po.User;
import com.pkh.pkec.phone.po.UserExample;
import com.pkh.pkec.phone.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */
public class UserServiceImpl implements UserService {


    private UserMapper userMapper;

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Boolean checkUser(String userName, String userPassword) {
        try {

            UserExample userExample = new UserExample();
            userExample.createCriteria().andUserNameEqualTo(userName).andUserPasswordEqualTo(userPassword);
            List<User> userList = userMapper.selectByExample(userExample);
            return userList.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
