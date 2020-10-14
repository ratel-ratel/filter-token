//package com.ratel.sign.filter;
//
//import com.ratel.sign.properties.VpSignProperties;
//import com.ratel.until.StringUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.beans.ConstructorProperties;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//import org.apache.commons.collections.CollectionUtils;
///**
// * Created by yxc on 2020/10/12.
// */
//public class SignFilter implements Filter {
//    private static final Logger log = LoggerFactory.getLogger(SignFilter.class);
//    @Autowired
//    private VpSignProperties vpSignProperties;
//
//    public void init(FilterConfig filterConfig) throws ServletException {
//        log.info("init VpSignFilter.....");
//    }
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest)request;
//        HttpServletResponse httpResponse = (HttpServletResponse)response;
//        HttpServletRequest requestWrapper = new RequestWrapper(httpRequest);
//        String requestBody;
//        if(CollectionUtils.isNotEmpty(this.vpSignProperties.getIgnoreMappings())) {
//            Set<String> uriSet = new HashSet(this.vpSignProperties.getIgnoreMappings());
//            requestBody = httpRequest.getRequestURI();
//            boolean isMatch = false;
//            Iterator var10 = uriSet.iterator();
//
//            while(var10.hasNext()) {
//                String uri = (String)var10.next();
//                isMatch = requestBody.equals(uri);
//                if(isMatch) {
//                    break;
//                }
//            }
//
//            log.info("当前请求的URI是==>{},isMatch==>{}", httpRequest.getRequestURI(), Boolean.valueOf(isMatch));
//            if(isMatch) {
//                filterChain.doFilter(requestWrapper, response);
//                return;
//            }
//        }
//
//        boolean accept = true;
//        String method = requestWrapper.getMethod();
//        String signKey = this.vpSignProperties.getKey();
//        Map<String, String> signKeyMap = this.vpSignProperties.getSignMap();
//        boolean specialkey = false;
//        if(null != signKeyMap && CollectionUtils.isNotEmpty(signKeyMap.keySet())) {
//            specialkey = true;
//        }
//        if(HttpMethod.POST.matches(method)) {
//            String oldSign = requestWrapper.getHeader("sign");
//            String timestampStr = requestWrapper.getHeader("timestamp");
//            String version = requestWrapper.getHeader("version");
//            String appId = requestWrapper.getHeader("appId");
//            String clientIp = requestWrapper.getHeader("clientIp");
//
//            if (StringUtil.isEmpty(oldSign) || StringUtil.isEmpty(timestampStr) ||
//                    StringUtil.isEmpty(version) || StringUtil.isEmpty(appId) ||
//                    StringUtil.isEmpty(clientIp)) {
//                returnFail("签名参数不全", httpResponse, ReturnCodeEnum.CODE_1006);
//
//                return;
//            }
//            boolean validateTimeout = validateTimeout(timestampStr, httpResponse);
//            if (!validateTimeout) {
//                returnFail("签名已过期", httpResponse, ReturnCodeEnum.CODE_1012);
//                return;
//            }
//            log.info("oldSign is {}", oldSign);
//
//            if (specialkey) {
//                signKey = (String)signKeyMap.get(appId);
//
//                if (StringUtil.isEmpty(signKey)) {
//                    signKey = this.vpSignProperties.getKey();
//                }
//            }
//
//        } else if(HttpMethod.GET.matches(method)) {
//            Map<String, String> urlParams = HttpRequestWrapperUtil.getUrlParams(requestWrapper);
//            String oldSign = (String)urlParams.get("sign");
//            String timestampStr = (String)urlParams.get("timestamp");
//            String version = (String)urlParams.get("version");
//            String appId = (String)urlParams.get("appId");
//            String clientIp = (String)urlParams.get("clientIp");
//             requestBody = (String)urlParams.get("requestBody");
//            boolean validateParam = this.validateParam(oldSign, timestampStr, version, appId, clientIp, httpResponse);
//            if(!validateParam) {
//                this.returnFail("签名参数不全", httpResponse, ReturnCodeEnum.CODE_1006);
//                return;
//            }
//
//            boolean validateTimeout = this.validateTimeout(timestampStr, httpResponse);
//            if(!validateTimeout) {
//                this.returnFail("签名已过期", httpResponse, ReturnCodeEnum.CODE_1012);
//                return;
//            }
//
//            if(specialkey) {
//                signKey = (String)signKeyMap.get(appId);
//                if(StringUtil.isEmpty(signKey)) {
//                    signKey = this.vpSignProperties.getKey();
//                }
//            }
//
//            Map<String, String> signMap = BeanPopularUtil.createSignMap(clientIp, version, appId, timestampStr, requestBody);
//            accept = SignatureUtil.validate(oldSign, signMap, signKey);
//        }
//
//        if(!accept) {
//            this.returnFail("签名验证失败", httpResponse, ReturnCodeEnum.CODE_1011);
//        } else {
//            filterChain.doFilter(requestWrapper, response);
//        }
//    }
//
//    private boolean validateParam(String oldSign, String timestampStr, String version, String appId, String clientIp, HttpServletResponse httpResponse) throws IOException {
//        boolean validateParam = true;
//        return !StringUtil.isEmpty(oldSign) && !StringUtil.isEmpty(timestampStr) && !StringUtil.isEmpty(version) && !StringUtil.isEmpty(appId) && !StringUtil.isEmpty(clientIp)?validateParam:false;
//    }
//
//    private boolean validateTimeout(String timestampStr, HttpServletResponse httpResponse) throws IOException {
//        boolean timeout = true;
//        Long timestamp = Long.valueOf(timestampStr);
//        long time = System.currentTimeMillis();
//        long difference = time - timestamp.longValue();
//        log.info("前端时间戳：{},服务端时间戳：{}", timestamp, Long.valueOf(time));
//        if(difference > this.vpSignProperties.getTimeout() * 60L * 1000L) {
//            timeout = false;
//        }
//
//        return timeout;
//    }
//
//    private void returnFail(String msg, HttpServletResponse response, ReturnCodeEnum code) throws IOException {
//        response.setStatus(401);
//        BaseResponse baseResponse = BackResponseUtil.build(code);
//        baseResponse.setMessage(msg);
//        response.setContentType("txt/plain");
//        response.setCharacterEncoding("utf-8");
//        response.getWriter().write(JsonUtil.objectToJson(baseResponse));
//    }
//
//    public void destroy() {
//        log.info("VpSignFilter destroy");
//    }
//
//    @ConstructorProperties({"vpSignProperties"})
//    public VpSignFilter(VpSignProperties vpSignProperties) {
//        this.vpSignProperties = vpSignProperties;
//    }
//}
