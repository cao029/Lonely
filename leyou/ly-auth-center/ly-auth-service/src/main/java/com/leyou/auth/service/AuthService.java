package com.leyou.auth.service;

import com.leyou.auth.config.JwtProperties;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.client.UserClient;
import com.leyou.user.pojo.User;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private UserClient userClient;

    public String login(String username, String password) {
        try {
            User user = userClient.queryUserByUsernameAndPassword(username, password);
            if ( user == null ) {
                throw new LyException(ExceptionEnum.USER_IS_NOT_EXIST);
            }
            UserInfo userInfo = new UserInfo(user.getId(), user.getUsername());
            String token = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getPublicKey(), prop.getExpire());
            return token;
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.TOKEN_MAKE_FILE);
        }
    }
}
