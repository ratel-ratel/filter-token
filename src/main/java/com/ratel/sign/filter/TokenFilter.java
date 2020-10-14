package com.ratel.sign.filter;

import com.ratel.enums.ReturnCodeEnum;
import com.ratel.response.BackResponseUtil;
import com.ratel.response.BaseResponse;
import com.ratel.sign.properties.TokenKey;
import com.ratel.until.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.ConstructorProperties;
import java.io.IOException;
import java.util.*;

/**
 * Created by yxc on 2020/10/12.
 */
@Slf4j
@AllArgsConstructor
public class TokenFilter implements Filter {
    @Autowired
    TokenKey tokenKey;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init TokenFilter.....");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        HttpServletRequest requestWrapper = new RequestWrapper(httpRequest);
        String requestBody;
        if(CollectionUtils.isNotEmpty(this.tokenKey.getIgnoreMappings())) {
            Set<String> uriSet = new HashSet(this.tokenKey.getIgnoreMappings());
            //获取请求链接
            requestBody = httpRequest.getRequestURI();
            boolean isMatch = false;
            Iterator var10 = uriSet.iterator();

            while(var10.hasNext()) {
                String uri = (String)var10.next();
                isMatch = requestBody.equals(uri);
                if(isMatch) {
                    //当前请求在要忽略的请求路径中
                    break;
                }
            }

            log.info("当前请求的URI是==>{},isMatch==>{}", httpRequest.getRequestURI(), Boolean.valueOf(isMatch));
            //当前请求在要忽略的请求路径中,直接放过
            if(isMatch) {
                filterChain.doFilter(requestWrapper, response);
                return;
            }
        }
        boolean accept = true;
        String method = requestWrapper.getMethod();
        String signKey = this.tokenKey.getToken();
        Map<String, String> signKeyMap = this.tokenKey.getSignMap();
        boolean specialkey = false;
        if(null != signKeyMap && CollectionUtils.isNotEmpty(signKeyMap.keySet())) {
            specialkey = true;
        }
        String oldSign;
        String timestampStr;
        String version ;
        String appId;
        String clientIp;
        if(HttpMethod.POST.matches(method)) {
             oldSign = requestWrapper.getHeader("sign");
             timestampStr = requestWrapper.getHeader("timestamp");
             version = requestWrapper.getHeader("version");
             appId = requestWrapper.getHeader("appId");
             clientIp = requestWrapper.getHeader("clientIp");

            if (StringUtil.isEmpty(oldSign) || StringUtil.isEmpty(timestampStr) ||
                    StringUtil.isEmpty(version) || StringUtil.isEmpty(appId) ||
                    StringUtil.isEmpty(clientIp)) {
                returnFail("签名参数不全", httpResponse, ReturnCodeEnum.CODE_1006);

                return;
            }
            boolean validateTimeout = validateTimeout(timestampStr, httpResponse);
            if (!validateTimeout) {
                returnFail("签名已过期", httpResponse, ReturnCodeEnum.CODE_1012);
                return;
            }
            log.info("oldSign is {}", oldSign);

            if (specialkey) {
                signKey = (String)signKeyMap.get(appId);

                if (StringUtil.isEmpty(signKey)) {
                    signKey = this.tokenKey.getToken();
                }
            }

        } else if(HttpMethod.GET.matches(method)) {
            Map<String, String> urlParams = HttpRequestWrapperUtil.getUrlParams(requestWrapper);
             oldSign = (String)urlParams.get("token");
             timestampStr = (String)urlParams.get("timestamp");
             version = (String)urlParams.get("version");
             appId = (String)urlParams.get("appId");
             clientIp = (String)urlParams.get("clientIp");
            requestBody = (String)urlParams.get("requestBody");
            boolean validateParam = this.validateParam(oldSign, timestampStr, version, appId, clientIp, httpResponse);
            if(!validateParam) {
                this.returnFail("签名参数不全", httpResponse, ReturnCodeEnum.CODE_1006);
                return;
            }

            boolean validateTimeout = this.validateTimeout(timestampStr, httpResponse);
            if(!validateTimeout) {
                this.returnFail("签名已过期", httpResponse, ReturnCodeEnum.CODE_1012);
                return;
            }

            if(specialkey) {
                signKey = (String)signKeyMap.get(appId);
                if(StringUtil.isEmpty(signKey)) {
                    signKey = this.tokenKey.getToken();
                }
            }

            Map<String, String> signMap = BeanPopularUtil.createSignMap(clientIp, version, appId, timestampStr, requestBody);
            accept = SignatureUtil.validate(oldSign, signMap, signKey);
        }
    }

    @Override
    public void destroy() {
        log.info("TokenFilter destroy");
    }
    private boolean validateParam(String oldSign, String timestampStr, String version, String appId, String clientIp, HttpServletResponse httpResponse) throws IOException {
        boolean validateParam = true;
        return !StringUtil.isEmpty(oldSign) && !StringUtil.isEmpty(timestampStr) && !StringUtil.isEmpty(version) && !StringUtil.isEmpty(appId) && !StringUtil.isEmpty(clientIp)?validateParam:false;
    }

    private boolean validateTimeout(String timestampStr, HttpServletResponse httpResponse) throws IOException {
        boolean timeout = true;
        Long timestamp = Long.valueOf(timestampStr);
        long time = System.currentTimeMillis();
        long difference = time - timestamp.longValue();
        log.info("前端时间戳：{},服务端时间戳：{}", timestamp, Long.valueOf(time));
        if(difference > this.tokenKey.getTimeout() * 60L * 1000L) {
            timeout = false;
        }

        return timeout;
    }

    private void returnFail(String msg, HttpServletResponse response, ReturnCodeEnum code) throws IOException {
        response.setStatus(401);
        BaseResponse baseResponse = BackResponseUtil.build(code);
        baseResponse.setMessage(msg);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JsonUtil.objectToJson(baseResponse));
    }
}
