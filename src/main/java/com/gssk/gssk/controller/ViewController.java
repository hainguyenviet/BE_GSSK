package com.gssk.gssk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping({"/", "/genogram", "/input-information", "/thankyou"} )
    public String index() {
        return "forward:/index.html";
    }
}
