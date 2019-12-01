package com.baizhi.controller;

import com.baizhi.dao.UserDao;
import com.baizhi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserDao userDao;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        //获取所有的cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                User user = userDao.selectByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }

        return "index";

    }

    /*@GetMapping("greeting")
    public String greeting(@RequestParam(name = "name",required = false,defaultValue = "World")String name, Model model){
        model.addAttribute("name",name);
        return "index";
    }*/
}
