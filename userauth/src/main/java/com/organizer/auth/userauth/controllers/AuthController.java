package com.organizer.auth.userauth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @ResponseBody
    @GetMapping("/test")
    public String test(){
        return "works";
    }
}
