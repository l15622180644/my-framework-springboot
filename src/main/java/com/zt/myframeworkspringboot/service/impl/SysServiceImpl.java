package com.zt.myframeworkspringboot.service.impl;


import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.common.constant.Constants;
import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.common.util.Base64Utils;
import com.zt.myframeworkspringboot.common.util.CaptchaUtils;
import com.zt.myframeworkspringboot.common.util.RandomCodeUtils;
import com.zt.myframeworkspringboot.common.util.UuidUtil;
import com.zt.myframeworkspringboot.service.SysService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @module
 * @date 2022/1/14 17:27
 */

@Service
public class SysServiceImpl implements SysService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public BaseResult<Map<String,Object>> captcha() {
        String code = RandomCodeUtils.getRandomCode(4);
        String uuid = UuidUtil.get32UUID();
        redisTemplate.opsForValue().set(Constants.CAPTCHA_CODE_KEY.getName() + uuid,code,3L, TimeUnit.MINUTES);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            BufferedImage image = CaptchaUtils.create(200, 100, code);
            ImageIO.write(image,"jpg",out);
            Map<String,Object> map = new HashMap<>();
            map.put("key",uuid);
            map.put("img", Base64Utils.encode(out.toByteArray()));
            return BaseResult.returnResult(map);
        } catch (Exception e) {
            e.printStackTrace();
            redisTemplate.delete(Constants.CAPTCHA_CODE_KEY.getName() + uuid);
        }
        return BaseResult.error(Status.EXCEPTION);
    }
}
