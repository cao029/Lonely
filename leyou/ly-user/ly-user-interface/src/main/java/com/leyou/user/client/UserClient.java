package com.leyou.user.client;

import com.leyou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("query")
    User queryUserByUsernameAndPassword(@RequestParam("username")String username, @RequestParam("password")String password);
}
