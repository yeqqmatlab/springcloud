package com.liveinpast.stress.gateway.config.properties;

import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 白名单配置
 *
 * @author Live.InPast
 * @date 2018/11/21
 */
@ConfigurationProperties("gateway.white-list")
public class WhiteListProperties {

    /**
     * 白名单
     */
    private List<String> patterns = Lists.newArrayList();

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }
}
