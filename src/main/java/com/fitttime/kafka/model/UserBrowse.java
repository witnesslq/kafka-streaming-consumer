package com.fitttime.kafka.model;

/**
 * 用户浏览历史记录
 * Created by root on 15-11-16.
 */
public class UserBrowse {

    private String userId;
    private String appVersion;
    private String appType;
    private String createTime;
    private String data;
    private String dataId;
    private String dataType;
    private String deviceId;
    private String cids;// 分类数组
    private String tags;// 标签数组
    private String os;// 标签数组
    private String geo;// 经纬度
    private String topic;
    private String duration; // 停留时间

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

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
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

    public String getCids() {
        return cids;
    }

    public void setCids(String cids) {
        this.cids = cids;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "UserBrowse{" +
                "userId='" + userId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appType='" + appType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", data='" + data + '\'' +
                ", dataId='" + dataId + '\'' +
                ", dataType='" + dataType + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", cids='" + cids + '\'' +
                ", tags='" + tags + '\'' +
                ", os='" + os + '\'' +
                ", geo='" + geo + '\'' +
                ", topic='" + topic + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
