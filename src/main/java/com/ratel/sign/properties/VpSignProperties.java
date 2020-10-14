package com.ratel.sign.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yxc on 2020/10/12.
 */
@Component
@ConfigurationProperties(
        prefix = "vpsign"
)
public class VpSignProperties {
    private String type;
    private String key;
    private boolean enabled = false;
    private long timeout = 1L;
    private Map<String, String> signMap = new HashMap();
    List<String> ignoreMappings;
    List<String> filterMappings;

    public VpSignProperties() {
    }

    public String getType() {
        return this.type;
    }

    public String getKey() {
        return this.key;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public Map<String, String> getSignMap() {
        return this.signMap;
    }

    public List<String> getIgnoreMappings() {
        return this.ignoreMappings;
    }

    public List<String> getFilterMappings() {
        return this.filterMappings;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setSignMap(Map<String, String> signMap) {
        this.signMap = signMap;
    }

    public void setIgnoreMappings(List<String> ignoreMappings) {
        this.ignoreMappings = ignoreMappings;
    }

    public void setFilterMappings(List<String> filterMappings) {
        this.filterMappings = filterMappings;
    }
    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if(!(o instanceof VpSignProperties)) {
            return false;
        } else {
            VpSignProperties other = (VpSignProperties)o;
            if(!other.canEqual(this)) {
                return false;
            } else {
                label79: {
                    Object this$type = this.getType();
                    Object other$type = other.getType();
                    if(this$type == null) {
                        if(other$type == null) {
                            break label79;
                        }
                    } else if(this$type.equals(other$type)) {
                        break label79;
                    }

                    return false;
                }

                Object this$key = this.getKey();
                Object other$key = other.getKey();
                if(this$key == null) {
                    if(other$key != null) {
                        return false;
                    }
                } else if(!this$key.equals(other$key)) {
                    return false;
                }

                if(this.isEnabled() != other.isEnabled()) {
                    return false;
                } else if(this.getTimeout() != other.getTimeout()) {
                    return false;
                } else {
                    label62: {
                        Object this$signMap = this.getSignMap();
                        Object other$signMap = other.getSignMap();
                        if(this$signMap == null) {
                            if(other$signMap == null) {
                                break label62;
                            }
                        } else if(this$signMap.equals(other$signMap)) {
                            break label62;
                        }

                        return false;
                    }

                    label55: {
                        Object this$ignoreMappings = this.getIgnoreMappings();
                        Object other$ignoreMappings = other.getIgnoreMappings();
                        if(this$ignoreMappings == null) {
                            if(other$ignoreMappings == null) {
                                break label55;
                            }
                        } else if(this$ignoreMappings.equals(other$ignoreMappings)) {
                            break label55;
                        }

                        return false;
                    }

                    Object this$filterMappings = this.getFilterMappings();
                    Object other$filterMappings = other.getFilterMappings();
                    if(this$filterMappings == null) {
                        if(other$filterMappings != null) {
                            return false;
                        }
                    } else if(!this$filterMappings.equals(other$filterMappings)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof VpSignProperties;
    }
    @Override
    public int hashCode() {
        Boolean PRIME = true;
        int result = 1;
        Object $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        Object $key = this.getKey();
        result = result * 59 + ($key == null?43:$key.hashCode());
        result = result * 59 + (this.isEnabled()?79:97);
        long $timeout = this.getTimeout();
        result = result * 59 + (int)($timeout >>> 32 ^ $timeout);
        Object $signMap = this.getSignMap();
        result = result * 59 + ($signMap == null?43:$signMap.hashCode());
        Object $ignoreMappings = this.getIgnoreMappings();
        result = result * 59 + ($ignoreMappings == null?43:$ignoreMappings.hashCode());
        Object $filterMappings = this.getFilterMappings();
        result = result * 59 + ($filterMappings == null?43:$filterMappings.hashCode());
        return result;
    }
    @Override
    public String toString() {
        return "VpSignProperties(type=" + this.getType() + ", key=" + this.getKey() + ", enabled=" + this.isEnabled() + ", timeout=" + this.getTimeout() + ", signMap=" + this.getSignMap() + ", ignoreMappings=" + this.getIgnoreMappings() + ", filterMappings=" + this.getFilterMappings() + ")";
    }
}

