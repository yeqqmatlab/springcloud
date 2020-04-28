package com.liveinpast.stress.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Live.InPast
 * @Date: 2020/4/20
 */
@ConfigurationProperties(prefix = "login-token")
public class LoginTokenProperties {


    /**
     * 发行人
     */
    private String jwtIssuer;

    /**
     * 秘钥
     */
    private String jwtSecret;

    /**
     * 过期时间(天)
     */
    private Integer jwtExpire;

    /**
     * rsa 公钥
     */
    private String rsaPublicKey;

    /**
     * rsa 私钥
     */
    private String rsaPrivateKey;

    /**
     * 密码加盐
     */
    private String passwordSalt;

    /**
     * basic验证用户名
     */
    private String basicUser;

    /**
     * basic验证密码
     */
    private String basicPassword;

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public void setJwtIssuer(String jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public Integer getJwtExpire() {
        return jwtExpire;
    }

    public void setJwtExpire(Integer jwtExpire) {
        this.jwtExpire = jwtExpire;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getBasicUser() {
        return basicUser;
    }

    public void setBasicUser(String basicUser) {
        this.basicUser = basicUser;
    }

    public String getBasicPassword() {
        return basicPassword;
    }

    public void setBasicPassword(String basicPassword) {
        this.basicPassword = basicPassword;
    }

}
