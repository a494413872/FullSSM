package com.demo.service.impl;

import com.demo.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public String getUserName() {
        return "i am provider";
    }
}
