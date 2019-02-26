package com.leyou.filter;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.prop.FilterProperties;
import com.leyou.prop.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private FilterProperties filterProp;

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();

        String requestURI = request.getRequestURI();
        return !isAllowPath(requestURI);
    }

    private boolean isAllowPath(String requestURI) {
//        定义一个标记
        boolean flag =false;

        for (String path : this.filterProp.getAllowPaths()) {
            if ( requestURI.equals(path) ) {
                flag = true;
                break;
            }
        }

        return flag;

    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();

        String token = CookieUtils.getCookieValue(request, prop.getCookieName());

        try {
            logger.info("校验token通过什么都不做！");
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
        } catch (Exception e) {
            logger.info("检验出异常，返回403");
            ctx.setSendZuulResponse(false);
            throw new LyException(ExceptionEnum.TOKEN_SHOW_EXCEPTION);
        }

        return null;
    }

}
