package com.demo.extra.springListener;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class AppleListener implements ApplicationListener<AppleEvent> {
    @Override
    @Async
    public void onApplicationEvent(AppleEvent appleEvent) {


        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("监听apple事件 "+appleEvent.getStr());
    }
}
