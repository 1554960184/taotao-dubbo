package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 展示注册和登录页面
 */
@Controller
public class PageController {
    @RequestMapping("/page/login")
    public String showLogin(String url, Model model){
        model.addAttribute("redirect",url);
        return "login";
    }

    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }
}
