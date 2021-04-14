package com.demo.extra.circleIoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAImpl implements ServiceA {
    @Autowired
    ServiceB serviceB;
    @Override
    public void speak() {
        System.out.println("i am service A ");
        serviceB.speak();
    }
}
