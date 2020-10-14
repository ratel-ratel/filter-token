package com.ratel.until;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by yxc on 2020/10/12.
 */
@Slf4j
public class HttpRequestWrapperUtil {
    /**
     * 获取 Body 参数
     *
     * @param request
     */
    public static SortedMap<String, String> getBodyParams(final HttpServletRequest request) throws Exception {
        //转化成json对象
        return JsonUtil.jsonToObject(getBodyParamsStr(request), SortedMap.class);
    }

    /**
     * 获取 Body 参数
     *
     * @param request
     */
    public static String getBodyParamsStr(final HttpServletRequest request) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String str;
        StringBuilder wholeStr = new StringBuilder();
        //一行一行的读取body体里面的内容；
        while ((str = reader.readLine()) != null) {
            wholeStr.append(str);
        }

        if (StringUtil.isEmpty(wholeStr)) {
            wholeStr.append("{}");
        }

        //转化成json对象
        return wholeStr.toString();
    }

    /**
     * 将URL请求参数转换成Map
     *
     * @param request
     */
    public static Map<String, String> getUrlParams(HttpServletRequest request) {
        String param = "";
        Map<String, String> result = new TreeMap<>();
        if (StringUtil.isEmpty(request.getQueryString())) {
            return result;
        }

        try {
            param = URLDecoder.decode(request.getQueryString(), "utf-8");
            result = MapParserUtil.getAllRequestParam(request);
            log.info("param is {} allRequestParam is {}", param, result);

        } catch (UnsupportedEncodingException e) {
            log.error("URLDecoder.decode error is", e);
        } catch (Exception e) {
            log.error("Exception. error is", e);

        }

        return result;
    }
}
