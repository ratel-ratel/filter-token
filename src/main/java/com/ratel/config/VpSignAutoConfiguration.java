package com.ratel.config;

import com.ratel.sign.filter.TokenFilter;
import com.ratel.sign.properties.TokenKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yxc on 2020/10/12.
 */
@Configuration
@EnableConfigurationProperties({TokenKey.class})
@ConditionalOnProperty(
        prefix = "tokenkey",
        name = {"enabled"},
        havingValue = "true"
)
@Slf4j
public class VpSignAutoConfiguration {
    @Autowired
    protected TokenKey tokenKey;

    public VpSignAutoConfiguration() {
    }

    @Bean
    public FilterRegistrationBean tokenFilter() {
//        if(StringUtils.isEmpty(this.tokenKey.getToken())) {
//            throw new RuntimeException("you must set tokenKey.token value first,please add moses-web.yml to spring.profiles.include !!!");
//        } else {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new TokenFilter(this.tokenKey));
            List<String> mappings = this.tokenKey.getFilterMappings();
            if(CollectionUtils.isEmpty(mappings)) {
                mappings = new ArrayList();
                ((List)mappings).add("/*");
            }

            log.debug("tokenFilter is active ..");
            Iterator var3 = ((List)mappings).iterator();

            while(var3.hasNext()) {
                String url = (String)var3.next();
                registration.addUrlPatterns(new String[]{url});
            }

            registration.setName("tokenFilter");
            registration.setOrder(2);
            return registration;
//        }
    }
}

