package com.leyou.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

    @GetMapping("hello")
    public String Hello(){
        return "hello , leyou --- ";
    }

}
