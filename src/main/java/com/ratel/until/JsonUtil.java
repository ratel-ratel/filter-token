package com.ratel.until;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS;

/**
 * JsonUtil工具类
 * 导入com.fasterxml.jackson.core.jackson-databind.jar包
 *
 * @author HJ
 * @ClassName: JsonUtil
 * @date 2016-3-16
 */
public class JsonUtil {

    protected JsonUtil(){
    }
    private static ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * 转换所有内容为string
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> String objectToStringJson(T data) {
        String json = null;
        if (data != null) {
            try {
                JSON_MAPPER.configure(WRITE_NUMBERS_AS_STRINGS, true);
                json = JSON_MAPPER.writeValueAsString(data);
            } catch (Exception e) {
                throw Exceptions.unchecked("objectToJson method error: " + e);
            }
        }
        return json;
    }

    /**
     * 转换所有内容为string
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> String objectToJson(T data) {
        String json = null;
        if (data != null) {
            try {
                json = JSON_MAPPER.writeValueAsString(data);
            } catch (Exception e) {
                throw Exceptions.unchecked("objectToJson method error: " + e);
            }
        }
        return json;
    }

    public static <T> T jsonToObject(String json, Class<T> cls) {
        T object = null;
        if (StringUtil.isNotEmpty(json) && cls != null) {
            try {
                if (json.startsWith("\"{")) {
                    json = json.replace("\"{", "{").replace("}\"", "}").replace("\\\"", "\"");
                }
                object = JSON_MAPPER.readValue(json, cls);
            } catch (Exception e) {
                throw Exceptions.unchecked("jsonToObject method error: " + e);
            }
        }
        return object;
    }

    public static List<Map<String, Object>> jsonToList(String json) {
        List<Map<String, Object>> list = null;
        if (StringUtil.isNotEmpty(json)) {
            try {
                if (json.startsWith("\"[")) {
                    json = json.replace("\"[", "[").replace("]\"", "]").replace("\\\"", "\"");
                }
                list = JSON_MAPPER.readValue(json, List.class);
            } catch (Exception e) {
                throw Exceptions.unchecked("jsonToList method error: " + e);
            }
        }
        return list;
    }

    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> maps = null;
        if (StringUtil.isNotEmpty(json)) {
            try {
                if (json.startsWith("\"{")) {
                    json = json.replace("\"{", "{").replace("}\"", "}").replace("\\\"", "\"");
                }
                maps = JSON_MAPPER.readValue(json, Map.class);
            } catch (Exception e) {
                throw Exceptions.unchecked("jsonToMap method error: " + e);
            }
        }
        return maps;
    }
}
