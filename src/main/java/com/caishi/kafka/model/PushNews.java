package com.caishi.kafka.model;

/**
 * 新闻推荐历史 model
 * Created by root on 15-10-27.
 */
public class PushNews {
    private String userId;
    private String app;
    private String appName;
    private String createTime;
    private String data;
    private String deviceId;
    private String deviceType;
    private String osType;
    private String referentId;
    private String referentType;
    private String topic;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getReferentId() {
        return referentId;
    }

    public void setReferentId(String referentId) {
        this.referentId = referentId;
    }

    public String getReferentType() {
        return referentType;
    }

    public void setReferentType(String referentType) {
        this.referentType = referentType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "PushNews{" +
                "userId='" + userId + '\'' +
                ", app='" + app + '\'' +
                ", appName='" + appName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", data='" + data + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", osType='" + osType + '\'' +
                ", referentId='" + referentId + '\'' +
                ", referentType='" + referentType + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
