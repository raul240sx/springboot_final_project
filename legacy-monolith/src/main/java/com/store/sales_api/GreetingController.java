package com.store.sales_api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class GreetingController {

    @GetMapping("/")
    public String greeting() {
        return "index";
    }
}
