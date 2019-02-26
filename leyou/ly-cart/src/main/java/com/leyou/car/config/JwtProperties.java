package com.leyou.car.config;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@Slf4j
@ConfigurationProperties("ly.jwt")
public class JwtProperties {

    private String pubkeyPath;

    private PublicKey publicKey;

    private String cookieName;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init(){
        try {
            this.publicKey = RsaUtils.getPublicKey(pubkeyPath);
        } catch (Exception e) {
            logger.info("根据公钥地址获取公钥失败");
            throw new LyException(ExceptionEnum.INIT_PUBLIC_KEY_FILE);
        }
    }

}
