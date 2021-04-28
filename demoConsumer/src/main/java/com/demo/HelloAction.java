package com.demo;

import com.demo.extra.springListener.AppleEvent;
import com.demo.extra.springListener.CustomerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HelloAction {

    @Autowired
    RestTemplate restTemplate;
    //访问hello的时候发送一个事件。
    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping("/p-{id}/hello")
    public String helloWorld(Model model, HttpServletRequest req, HttpServletResponse Resp, @RequestParam("name") String name,@PathVariable("id") Long id){
        model.addAttribute("name",name);
        System.out.println("开始发布customer事件");
        applicationContext.publishEvent(new CustomerEvent(this,name));
        System.out.println("结束发布customer事件");
        return "helloworld";
    }
    @RequestMapping("/p-{id}/helloapple")
    @ResponseBody
    public String helloApple(Model model, HttpServletRequest req, HttpServletResponse Resp, @RequestParam("name") String name,@PathVariable("id") Long id){
        model.addAttribute("name",name);
        System.out.println("开始发布apple事件");
        applicationContext.publishEvent(new AppleEvent(this,name));
        System.out.println("结束发布apple事件");
        return "helloapple";
    }
    @RequestMapping("/rewrite-{id}")
    @ResponseBody
    public String rewrite(Model model, HttpServletRequest req, HttpServletResponse Resp, @PathVariable("id") Long id){
        String forObject = restTemplate.getForObject("http://localhost:8080/consumer/restTemplateHanlder.do", String.class);
        System.out.println("forObject="+forObject);
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8080/consumer/restTemplateHanlder.do", String.class);
        String body = forEntity.getBody();
        System.out.println("body="+body);
        return "我是一个重写url"+id;
    }

    @RequestMapping("/submit")
    public String submit(Model model, HttpServletRequest req, HttpServletResponse Resp, @RequestParam("name") String name){
        model.addAttribute("name",name+"#submit");
        return "helloworld";
    }

    @RequestMapping("/restTemplateHanlder")
    @ResponseBody
    public String restTemplateHanlder(){
        return "这是一个rest handler";
    }

    @RequestMapping("/dubbocall")
    @ResponseBody
    public String dubbocall(){

        return "dubbocall result=";
    }

}
