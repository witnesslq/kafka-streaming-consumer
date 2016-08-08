package com.fitttime.kafka.model;

/**
 * 冷启动用户选择个人喜好 model
 * Created by root on 15-11-16.
 */
public class UserActive {

    private String userId;
    private String appVersion;
    private String createTime;
    private String data;
    private String dataId;
    private String dataType;
    private String deviceId;
    private String operationType;
    private String os;
    private String ip;

    private String osType;
    private String topic;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "UserActive{" +
                "userId='" + userId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", createTime='" + createTime + '\'' +
                ", data='" + data + '\'' +
                ", dataId='" + dataId + '\'' +
                ", dataType='" + dataType + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", operationType='" + operationType + '\'' +
                ", os='" + os + '\'' +
                ", ip='" + ip + '\'' +
                ", osType='" + osType + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
