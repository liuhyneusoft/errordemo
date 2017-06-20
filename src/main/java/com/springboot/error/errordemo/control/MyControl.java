package com.springboot.error.errordemo.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyControl {

    @RequestMapping("/e500")
    public String str(){
        String s = null;
        s.length();
        return "dd";
    }

    @RequestMapping("/e404")
    public String str404(){
        return "dd";
    }
}
