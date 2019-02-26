package com.leyou.auth.web;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class AuthController {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    private ResponseEntity<Void> login(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request, HttpServletResponse response
    ){
        String token = authService.login(username,password);
        CookieUtils.newBuilder().response(response).request(request)
                .httpOnly(false).name(prop.getCookieName()).value(token).build();
        return ResponseEntity.ok().build();
    }

    @GetMapping("verity")
    public ResponseEntity<UserInfo> verityUser(@CookieValue("LY_TOKEN")String token,
                                               HttpServletRequest request,
                                               HttpServletResponse response){
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            log.info("获取token成功，我们需要刷新token");
            String newToken = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getExpire());
            log.info("我们将token写入cookie");
            CookieUtils.newBuilder().response(response).request(request)
                    .httpOnly(true).name(prop.getCookieName()).value(newToken).build();

            return ResponseEntity.ok(userInfo);
        }catch (Exception e){
            log.info("抛出异常，证明token无效，直接返回401");
            throw new LyException(ExceptionEnum.TOKEN_MAKE_FILE);
        }
    }


}
