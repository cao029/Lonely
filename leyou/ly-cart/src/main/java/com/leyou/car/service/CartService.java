package com.leyou.car.service;

import com.leyou.car.filter.LoginInterceptor;
import com.leyou.car.mapper.CartMapper;
import com.leyou.car.pojo.Cart;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "ly:cart:uid:";

    static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public void addCart(Cart cart) {

        UserInfo user = LoginInterceptor.getLoginUser();

        String key = KEY_PREFIX + user.getId();

        String hashkey = cart.getSkuId().toString();

//        获取数量
        Integer num = cart.getNum();

        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);

        if ( hashOps.hasKey(hashkey) ) {
            cart = JsonUtils.toBean(hashOps.get(hashkey).toString(), Cart.class);
            cart.setNum(num + cart.getNum());
        }

        hashOps.put(hashkey,JsonUtils.toString(cart));

    }

    public List<Cart> getCart() {
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOPs = redisTemplate.boundHashOps(key);
        List<Object> carts = hashOPs.values();
        if ( CollectionUtils.isEmpty(carts) ) {
            throw new LyException(ExceptionEnum.CART_NOT_HAVE_DATE);
        }
        return carts.stream().map(o -> JsonUtils.toBean(o.toString(),Cart.class)).collect(Collectors.toList());
    }


    public void changeCart(Long skuId, Integer num) {
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashPos = redisTemplate.boundHashOps(key);
        if ( !hashPos.hasKey(skuId.toString()) ) {
            throw new LyException(ExceptionEnum.CART_NOT_HAVE_DATE);
        }
        Cart cart = JsonUtils.toBean(hashPos.get(skuId.toString()).toString(), Cart.class);
        cart.setNum(cart.getNum() + num);
        hashPos.put(skuId.toString(),JsonUtils.toString(cart));
    }

    public void deleteCart(Long skuId) {
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
//        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
//        if ( !hashOps.hasKey(skuId.toString()) ) {
//            throw new LyException(ExceptionEnum.CART_NOT_HAVE_DATE);
//        }
//        hashOps.delete(skuId);
        redisTemplate.opsForHash().delete(key,skuId);
    }
}
