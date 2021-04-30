package com.demo.service.impl;

import com.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("userServiceRemote")
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public String getUserName() {
        logger.info("start to invoke");
        return "i am provider";
    }
}
