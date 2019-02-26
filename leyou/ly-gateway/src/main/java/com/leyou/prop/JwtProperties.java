package com.leyou.prop;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.RsaUtils;
import lombok.Data;
import org.bouncycastle.crypto.tls.TlsRSAUtils;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String pubKeyPath; //公钥

    private PublicKey publicKey;

    private String cookieName;

    private static final Logger logger =  LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init(){
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.info("初始化公钥失败！",e);
            throw new LyException(ExceptionEnum.INIT_PUBLIC_KEY_FILE);
        }
    }

}
