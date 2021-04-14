package com.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginAction {

    @RequestMapping("/login")
    public String login(Model model, HttpServletRequest req, HttpServletResponse Resp){
        return "login";
    }

    @RequestMapping("/submitlogin")
    public String submitLogin(Model model, HttpServletRequest req, HttpServletResponse Resp, @RequestParam("name") String name){
        model.addAttribute("name",name);
        return "helloworld";
    }
}
