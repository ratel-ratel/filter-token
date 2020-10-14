package com.ratel.web;

import com.ratel.sign.properties.TokenKey;
import com.ratel.sign.properties.VpSignProperties;
import com.ratel.until.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yxc on 2020/10/12.
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {
    @Autowired
    TokenKey tokenKey;
    @Autowired
    VpSignProperties vpSignProperties;

    @RequestMapping("/test")
    private String test(){
        return "test";
    }
    @RequestMapping("/tokenKey")
    private String tokenKey() throws Exception {
        return JsonUtil.objectToJson(tokenKey);
    }
}
