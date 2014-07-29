package com.mycompany.tutorial.controller;

import com.mycompany.tutorial.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HelloWorldController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "world";
    }

    @RequestMapping("/hello2")
    public ModelAndView hello2(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return new ModelAndView("world2", "name", name);
    }

    @RequestMapping("/hello3")
    public String hello3(HttpServletResponse response) {
        response.addCookie(new Cookie("foo", "bar"));
        return "world3";
    }

    @RequestMapping("/execute")
    public ModelAndView execute(@RequestParam(value = "task", required = true) String task) {
        String result = taskService.execute(task);
        return new ModelAndView("execute", "result", result);
    }

}
