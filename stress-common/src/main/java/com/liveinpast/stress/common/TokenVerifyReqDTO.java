package com.liveinpast.stress.common;

import javax.validation.constraints.NotBlank;

/**
 * Created by Live.InPast on 2018/11/5.
 */
public class TokenVerifyReqDTO {

    /**
     * 密钥
     */
    @NotBlank(message = "Token不能为空")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
