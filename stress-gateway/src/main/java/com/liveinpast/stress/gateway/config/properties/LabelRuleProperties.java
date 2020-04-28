package com.liveinpast.stress.gateway.config.properties;

import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by Live.InPast on 2018/11/20.
 */
@ConfigurationProperties("gateway.label-rule")
public class LabelRuleProperties {

    /**
     * 黑名单
     */
    public List<LabelRule> forbiddenRules = Lists.newArrayList();

    public List<LabelRule> getForbiddenRules() {
        return forbiddenRules;
    }

    public void setForbiddenRules(List<LabelRule> forbiddenRules) {
        this.forbiddenRules = forbiddenRules;
    }

    /**
     * 标签规则
     *
     * @author Live.InPast
     * @date 2018/11/20
     */
    class LabelRule {

        /**
         * 标签规则是否启用
         */
        private boolean enable;

        /**
         * 标签具体规则
         */
        private Map<String, String> rules;

        /**
         * 标签规则启用后提示的信息
         */
        private String message;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public Map<String, String> getRules() {
            return rules;
        }

        public void setRules(Map<String, String> rules) {
            this.rules = rules;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}

