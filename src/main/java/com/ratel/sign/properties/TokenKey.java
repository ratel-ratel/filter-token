package com.ratel.sign.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yxc on 2020/9/21.
 */
@Component
@ConfigurationProperties(
        prefix = "tokenkey"
)
@Data
public class TokenKey  {
    /**
     * 平台 id
     */
    private String appId;
    /**
     * 密钥
     */
    private String secret;
    private String couponSecret;
    private String token;
    /**
     * 手机号码
     */
    private boolean enabled = false;
    private long timeout = 1L;
    private Map<String, String> signMap = new HashMap();
    List<String> ignoreMappings;
    List<String> filterMappings;

    @Override
    public String toString() {
        return "TokenKey{" +
                "appId='" + appId + '\'' +
                ", secret='" + secret + '\'' +
                ", couponSecret='" + couponSecret + '\'' +
                ", enabled=" + enabled +
                ", timeout=" + timeout +
                ", signMap=" + signMap +
                ", ignoreMappings=" + ignoreMappings +
                ", filterMappings=" + filterMappings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenKey tokenKey = (TokenKey) o;

        if (enabled != tokenKey.enabled) return false;
        if (timeout != tokenKey.timeout) return false;
        if (!appId.equals(tokenKey.appId)) return false;
        if (!secret.equals(tokenKey.secret)) return false;
        if (!couponSecret.equals(tokenKey.couponSecret)) return false;
        if (!signMap.equals(tokenKey.signMap)) return false;
        if (!ignoreMappings.equals(tokenKey.ignoreMappings)) return false;
        return filterMappings.equals(tokenKey.filterMappings);
    }

    @Override
    public int hashCode() {
        int result = appId.hashCode();
        result = 31 * result + secret.hashCode();
        result = 31 * result + couponSecret.hashCode();
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (int) (timeout ^ (timeout >>> 32));
        result = 31 * result + signMap.hashCode();
        result = 31 * result + ignoreMappings.hashCode();
        result = 31 * result + filterMappings.hashCode();
        return result;
    }
}
