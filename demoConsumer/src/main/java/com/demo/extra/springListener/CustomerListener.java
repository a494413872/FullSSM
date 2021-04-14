package com.demo.extra.springListener;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

//@EnableAsync加上Async才会生效
@Component
@EnableAsync
public class CustomerListener implements ApplicationListener<CustomerEvent> {
    @Override
    @Async
    public void onApplicationEvent(CustomerEvent customerEvent) {

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("监听customer事件 "+customerEvent.getStr());
    }
}
