package com.ratel.until.token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yxc on 2020/10/12.
 */
public class TokenBeanPopularUtil {
    public TokenBeanPopularUtil() {
    }

    public static Map<String, String> createSignMap(String clientIp, String version, String appId, String timestamp, String requestBody) {
        Map<String, String> request = new HashMap();
        request.put("clientIp", clientIp);
        request.put("version", version);
        request.put("appId", appId);
        request.put("timestamp", timestamp);
        request.put("requestBody", requestBody);
        return request;
    }
}
