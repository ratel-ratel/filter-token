package com.ratel.until;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author : chenwei
 * @version V1.0
 * @Project: moses-framework
 * @Package cn.vpclub.moses.utils.common
 * @Description: 签名校验请求体
 * @date Date : 2017年01月04日 21:20
 */
@Slf4j
public class SignatureUtil {
    /**
     * 忽略大小写的排序比较
     */
    public static final Comparator CASE_INSENSITIVE = String.CASE_INSENSITIVE_ORDER;
    /**
     * 大小写铭感的排序比较
     */
    public static final Comparator CASE_SENSITIVE = new ComparableComparator();

    protected SignatureUtil() {

    }

    /**
     * MD5签名算法
     *
     * @param o      要参与签名的数据对象
     * @param apiKey API密钥
     * @return 签名
     */
    public static String getMD5Sign(Object o, String apiKey) {
        return SignatureUtil.getSign(o, apiKey, SystemConstant.MD5);
    }

    /**
     * MD5签名算法
     *
     * @param o          要参与签名的数据对象
     * @param apiKey     API密钥
     * @param comparator 比较算法
     * @return 签名
     */
    public static String getMD5Sign(Object o, String apiKey, Comparator comparator) {
        return SignatureUtil.getSign(o, apiKey, SystemConstant.MD5, comparator);
    }

    /**
     * 签名算法
     *
     * @param dataInfo  要参与签名的数据object
     * @param apiKey    API密钥
     * @param algorithm 运算法测
     * @return 签名
     */
    public static String getSign(Object dataInfo, String apiKey, String algorithm) {
        if (null != dataInfo && dataInfo instanceof Map) {
            return getSignFromMap((Map) dataInfo, apiKey, algorithm);
        } else {
            return getSignFromObject(dataInfo, apiKey, algorithm);
        }
    }

    /**
     * 签名算法
     *
     * @param dataInfo   要参与签名的数据object
     * @param apiKey     API密钥
     * @param algorithm  运算法测
     * @param comparator 比较算法
     * @return 签名
     */
    public static String getSign(Object dataInfo, String apiKey, String algorithm, Comparator comparator) {
        if (null != dataInfo && dataInfo instanceof Map) {
            return getSignFromMap((Map) dataInfo, apiKey, algorithm, comparator);
        } else {
            return getSignFromObject(dataInfo, apiKey, algorithm, comparator);
        }
    }

    /**
     * 签名算法
     *
     * @param dataInfo  要参与签名的数据object
     * @param apiKey    API密钥
     * @param algorithm 运算法测
     * @return 签名
     */
    public static String getSignFromObject(Object dataInfo, String apiKey, String algorithm) {
        return getSignFromObject(dataInfo, apiKey, algorithm, null);
    }

    /**
     * 签名算法
     *
     * @param dataInfo   要参与签名的数据object
     * @param apiKey     API密钥
     * @param algorithm  运算法测
     * @param comparator 比较算法
     * @return 签名
     */
    public static String getSignFromObject(Object dataInfo, String apiKey, String algorithm, Comparator comparator) {
        ArrayList<String> list = new ArrayList<String>();
        if (null != dataInfo) {
            Class cls = dataInfo.getClass();
            // 获取类所有方法
            Method[] methodArr = cls.getMethods();
            for (Method method : methodArr) {
                String methodName = method.getName();
                try {
                    if (methodName.startsWith("get") && !"getClass".equals(methodName)
                            && StringUtil.isNotEmpty(method.invoke(dataInfo))) {
                        Object value = method.invoke(dataInfo);
                        boolean pushDataFlag = true;
                        if (value instanceof Collection) {
                            if (((Collection) value).isEmpty()) {
                                pushDataFlag = false;
                            } else {
                                value = JsonUtil.objectToJson(value);
                            }
                        } else if (value instanceof Map) {
                            if (((Map) value).isEmpty()) {
                                pushDataFlag = false;
                            } else {
                                value = JsonUtil.objectToJson(value);
                            }
                        }
                        if (pushDataFlag) {
                            String key = methodName.substring(3);
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                            list.add(key + "=" + value + "&");
                        }
                    }
                } catch (Exception e) {
                    log.error("error:" + method, e);
                }
            }
        }
        return createSign(list, apiKey, algorithm, comparator);
    }

    /**
     * MD5签名算法
     *
     * @param map    要参与签名的数据Map
     * @param apiKey API密钥
     * @return 签名
     */
    public static String getMD5SignFromMap(Map<String, ? extends Object> map, String apiKey) {
        return SignatureUtil.getSignFromMap(map, apiKey, SystemConstant.MD5);
    }

    /**
     * MD5签名算法
     *
     * @param map        要参与签名的数据Map
     * @param apiKey     API密钥
     * @param comparator 比较算法
     * @return 签名
     */
    public static String getMD5SignFromMap(Map<String, ? extends Object> map, String apiKey, Comparator comparator) {
        return SignatureUtil.getSignFromMap(map, apiKey, SystemConstant.MD5, comparator);
    }

    /**
     * 签名算法
     *
     * @param map       要参与签名的数据Map
     * @param apiKey    API密钥
     * @param algorithm 运算法测
     * @return 签名
     */
    public static String getSignFromMap(Map<String, ? extends Object> map, String apiKey, String algorithm) {
        return getSignFromMap(map, apiKey, algorithm, null);
    }

    /**
     * 签名算法
     *
     * @param map        要参与签名的数据Map
     * @param apiKey     API密钥
     * @param algorithm  运算法测
     * @param comparator 比较算法
     * @return 签名
     */
    public static String getSignFromMap(Map<String, ? extends Object> map, String apiKey, String algorithm, Comparator comparator) {
        ArrayList<String> list = new ArrayList<String>();
        if (null != map && !map.isEmpty()) {
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                if (StringUtil.isEmpty(entry.getValue())
                        || "sign".equalsIgnoreCase(entry.getKey())) {
                    continue;
                }

                if (entry.getValue() instanceof Collection
                        && !((Collection) entry.getValue()).isEmpty()) {
                    list.add(entry.getKey() + "=" + JsonUtil.objectToJson(entry.getValue()) + "&");
                } else if (entry.getValue() instanceof Map
                        && !((Map) entry.getValue()).isEmpty()) {
                    list.add(entry.getKey() + "=" + JsonUtil.objectToJson(entry.getValue()) + "&");
                } else {
                    list.add(entry.getKey() + "=" + entry.getValue() + "&");
                }
            }
        }
        return createSign(list, apiKey, algorithm, comparator);
    }

    /**
     * 生成签名
     *
     * @param list      (key=value&)类型字符串列表
     * @param apiKey    签名密钥
     * @param algorithm 运算法测
     * @return 签名
     */
    private static String createSign(ArrayList<String> list, String apiKey, String algorithm) {
        return createSign(list, apiKey, algorithm, null);
    }

    /**
     * 生成签名
     *
     * @param list       (key=value&)类型字符串列表
     * @param apiKey     签名密钥
     * @param algorithm  运算法测
     * @param comparator 比较算法
     * @return 签名
     */
    private static String createSign(ArrayList<String> list, String apiKey, String algorithm, Comparator comparator) {
        String result = null;
        if (null != list && !list.isEmpty()) {
            int size = list.size();
            String[] arrayToSort = list.toArray(new String[size]);
            if (null == comparator) {
                Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
            } else {
                Arrays.sort(arrayToSort, comparator);
            }
            StringBuilder stringSignTemp = new StringBuilder();
            for (int i = 0; i < size; i++) {
                stringSignTemp.append(arrayToSort[i]);
            }
            String signTempStr;
            if (!StringUtil.isEmpty(apiKey)) {
                stringSignTemp.append("key=" + apiKey);
                signTempStr = stringSignTemp.toString();
            } else {
                signTempStr = stringSignTemp.substring(0, stringSignTemp.length() - 1);
            }
            log.info("createSign Before MD5:" + signTempStr);
            result = Encodes.encodeToAlgorithm(signTempStr, algorithm).toUpperCase();
        }
        log.info("createSign Result:" + result);
        return result;
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     *
     * @param map    API返回的MAP数据
     * @param apiKey API密钥
     * @return API签名是否合法
     */
    public static boolean checkIsSignValidFromData(Map<String, ? extends Object> map, String apiKey) {
        return checkIsSignValidFromData(map, apiKey, null);
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     *
     * @param map        API返回的MAP数据
     * @param apiKey     API密钥
     * @param comparator 比较算法
     * @return API签名是否合法
     */
    public static boolean checkIsSignValidFromData(Map<String, ? extends Object> map, String apiKey, Comparator comparator) {
        log.info(map.toString());

        String signFromAPIResponse = map.get("sign").toString();
        if (signFromAPIResponse == "" || signFromAPIResponse == null) {
            log.info("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }
        log.info("服务器回包里面的签名是:" + signFromAPIResponse);
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", null);
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String signForAPIResponse = SignatureUtil.getSignFromMap(map, apiKey, SystemConstant.MD5, comparator);

        if (!signForAPIResponse.equals(signFromAPIResponse)) {
            //签名验不过，表示这个API返回的数据有可能已经被篡改了
            log.info("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
            return false;
        }
        log.info("恭喜，API返回的数据签名验证通过!!!");
        return true;
    }

    /**
     * 校验签名
     *
     * @param object    要参与签名的数据object
     * @param oldSign   原签名
     * @param key       密钥
     * @param algorithm 运算法测
     * @return
     */
    public static boolean signVerify(Object object, String oldSign, String key, String algorithm) {
        return signVerify(object, oldSign, key, algorithm, null);
    }

    /**
     * 校验签名
     *
     * @param object     要参与签名的数据object
     * @param oldSign    原签名
     * @param key        密钥
     * @param algorithm  运算法测
     * @param comparator 比较算法
     * @return
     */
    public static boolean signVerify(Object object, String oldSign, String key, String algorithm, Comparator comparator) {
        if (StringUtil.isEmpty(oldSign)) {
            log.info("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        } else {
            String sign = getSign(object, key, algorithm, comparator);
            return sign.equals(oldSign);
        }
    }
    /**
     * 清空值为空的内容
     * @param paramMap
     * @return
     */
    public static Map<String, Object> removeMapEmptyValue(Map<String, Object> paramMap) {
        Set<String> set = paramMap.keySet();
        Iterator<String> it = set.iterator();
        List<String> listKey = new ArrayList<String>();
        while (it.hasNext()) {
            String str = it.next();
            if (paramMap.get(str) == null || "".equals(paramMap.get(str))) {
                listKey.add(str);
            }
        }
        for (String key : listKey) {
            paramMap.remove(key);
        }
        return paramMap;
    }

    /**
     *  校验签名
     * @param oldSign
     * @param request
     * @param md5Key
     * @return
     */
    public static boolean validate(String oldSign, Map<String,String> request,String md5Key){
        boolean result = false;
        String newSign = SignatureUtil.getMD5SignFromMap(request,md5Key);
        result = oldSign.equals(newSign);
        log.info("oldSign {} newSign {}",oldSign,newSign);
        return result;
    }

    /**
     *  时间戳校验超时
     * @param timestamp
     * @param expiredMinitue
     * @return
     */
    public static boolean validateExpired(String timestamp,Long expiredMinitue){
        boolean expired = false;

        if (!StringUtils.isNumeric(timestamp)){
            return expired;
        }
        if ( null == expiredMinitue){
            expiredMinitue = 10L;
        }
        Long time = Long.valueOf(timestamp);
        Long now = System.currentTimeMillis();
        boolean compare = (now - time) > expiredMinitue * 60 * 1000;
        //未超时10分钟 则通过
        if (!compare){
            expired = true;
        }
        log.info("validateExpired back :{}",expired);
        return expired;
    }

}
