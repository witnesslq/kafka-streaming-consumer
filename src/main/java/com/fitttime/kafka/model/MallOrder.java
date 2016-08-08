package com.fitttime.kafka.model;

/**
 * 商城订单model
 * Created by root on 16-07-22.
 */
public class MallOrder {

    private String outerOrigin;
    private String couponName;
    private String orderId;
    private String city;
    private String couponTemplateId;
    private String userId;
    private String orderItems;// 本订单的商品列表
    private String orderTime;
    private String orderAmount;
    private String payAmount;
    private String province;

    private String district;
    private String userIp;
    private String topic;
    private String innerOrigin;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOuterOrigin() {
        return outerOrigin;
    }

    public void setOuterOrigin(String outerOrigin) {
        this.outerOrigin = outerOrigin;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCouponTemplateId() {
        return couponTemplateId;
    }

    public void setCouponTemplateId(String couponTemplateId) {
        this.couponTemplateId = couponTemplateId;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getInnerOrigin() {
        return innerOrigin;
    }

    public void setInnerOrigin(String innerOrigin) {
        this.innerOrigin = innerOrigin;
    }

    @Override
    public String toString() {
        return "MallOrder{" +
                "outerOrigin='" + outerOrigin + '\'' +
                ", couponName='" + couponName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", city='" + city + '\'' +
                ", couponTemplateId='" + couponTemplateId + '\'' +
                ", userId='" + userId + '\'' +
                ", orderItems='" + orderItems + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", payAmount='" + payAmount + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", userIp='" + userIp + '\'' +
                ", topic='" + topic + '\'' +
                ", innerOrigin='" + innerOrigin + '\'' +
                '}';
    }
}
