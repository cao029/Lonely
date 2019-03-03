package com.leyou.user.web;

import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("check/{param}/{type}")
    public ResponseEntity<Boolean> checkUserData(
            @PathVariable("param")String param,
            @PathVariable("type")Integer type
    ){
        boolean bool = userService.checkUserData(param,type);
        return ResponseEntity.ok(bool);
    }

//    添加注释
    @PostMapping("code")
    public ResponseEntity<Void> sendUserPhone(@RequestParam("phone")String phone){
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code){
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(
            @RequestParam("username")String username,
            @RequestParam("password")String password
    ){
        User user = userService.login(username,password);
        return ResponseEntity.ok().build();
    }


}
