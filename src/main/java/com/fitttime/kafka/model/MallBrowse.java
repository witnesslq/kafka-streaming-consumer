package com.fitttime.kafka.model;

/**
 * 商城浏览model
 * Created by root on 16-07-22.
 */
public class MallBrowse {

    private String topic;
    private String userId;
    private String uuid;
    private String userAgent;
    private String channel2;
    private String url;// 本订单的商品列表
    private String referer;
    private String pageType;
    private String timestamp;
    private String click;
    private String ftsource;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getChannel2() {
        return channel2;
    }

    public void setChannel2(String channel2) {
        this.channel2 = channel2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getFtsource() {
        return ftsource;
    }

    public void setFtsource(String ftsource) {
        this.ftsource = ftsource;
    }

    @Override
    public String toString() {
        return "MallBrowse{" +
                "topic='" + topic + '\'' +
                ", userId='" + userId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", channel2='" + channel2 + '\'' +
                ", url='" + url + '\'' +
                ", referer='" + referer + '\'' +
                ", pageType='" + pageType + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", click='" + click + '\'' +
                ", ftsource='" + ftsource + '\'' +
                '}';
    }
}
