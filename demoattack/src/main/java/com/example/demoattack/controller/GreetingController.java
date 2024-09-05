package com.example.demoattack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String index(@RequestParam(name="name", required=true) String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}

