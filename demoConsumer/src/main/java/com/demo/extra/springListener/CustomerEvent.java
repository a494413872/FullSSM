package com.demo.extra.springListener;

import org.springframework.context.ApplicationEvent;

public class CustomerEvent extends ApplicationEvent {

    String str="";

    public CustomerEvent(Object source,String str) {
        super(source);
        this.str =str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
