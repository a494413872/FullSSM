package com.demo.extra.circleIoc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceBImpl  implements ServiceB {
    @Autowired
    ServiceA serviceA;
    @Override
    public void speak() {
        System.out.println("i am service B ");
        serviceA.speak();
    }
}
