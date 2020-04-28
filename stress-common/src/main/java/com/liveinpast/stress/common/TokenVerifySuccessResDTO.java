package com.liveinpast.stress.common;

/**
 * Token验证成功后解析的DTO
 *
 * @author Live.InPast
 * @date 2018/10/23
 */
public class TokenVerifySuccessResDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 预留的额外用户ID
     */
    private String extraUserId;

    /**
     * 微信程序唯一识别号
     */
    private String openId;

    /**
     * 微信开发者平台唯一识别号
     */
    private String unionId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 学校ID
     */
    private Integer schoolId;

    /**
     * 服务组(项目)
     */
    private String serviceGroup;

    /**
     * 登陆的设备(客户端)
     */
    private Integer client;

    /**
     * 登陆的设备(客户端)名称
     */
    private String clientName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExtraUserId() {
        return extraUserId;
    }

    public void setExtraUserId(String extraUserId) {
        this.extraUserId = extraUserId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
