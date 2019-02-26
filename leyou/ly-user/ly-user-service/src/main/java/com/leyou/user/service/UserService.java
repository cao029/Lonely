package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CodecUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private final static String KEY_PREFIX = "ly:code:phone";


    public boolean checkUserData(String param, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(param);
                break;
            case 2:
                user.setPhone(param);
                break;
            default:
                throw new LyException(ExceptionEnum.TYPE_NOT_EXITED);
        }
        List<User> users = userMapper.select(user);
        if (CollectionUtils.isEmpty(users)){
            throw new LyException(ExceptionEnum.USER_NOT_EXIST);
        }
        int i = userMapper.selectCount(user);
        return i == 1;
    }

    public void sendCode(String phone) {
        if (!phone.matches("^1[35678]\\d{9}$")) {
            throw new LyException(ExceptionEnum.PHONE_NOT_MATCHES);
        }
//        生成随机验证码
        String code = NumberUtils.generateCode(6);

//        将数据存储到redis中
        redisTemplate.opsForValue().set(KEY_PREFIX + phone,code,5, TimeUnit.MINUTES);

        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);

    }

    public void register(User user, String code) {
//        校验验证码
        String RedisPhone = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (StringUtils.isEmpty(RedisPhone)) {
            throw new LyException(ExceptionEnum.REDIS_DATA_NOT_FOUND);
        }
        if (RedisPhone.equals(code)) {
            throw new LyException(ExceptionEnum.DATA_NOT_TOGETHER);
        }

//        校验用户其他参数
        if (StringUtils.isEmpty(user.getUsername()) && StringUtils.isEmpty(user.getPassword())) {
            throw new LyException(ExceptionEnum.DATA_NOT_NULL);
        }

//        生成salt
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setCreated(new Date());
//        对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        user.setId(null);

//        写入数据库
        userMapper.insertSelective(user);
    }

    public User login(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        List<User> users = userMapper.select(user);
        if ( CollectionUtils.isEmpty(users) ) {
            throw new LyException(ExceptionEnum.LOGIN_NOT_FILE);
        }
        return users.get(0);
    }

}
