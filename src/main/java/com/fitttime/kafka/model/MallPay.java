package com.fitttime.kafka.model;

/**
 * 商城订单model
 * Created by root on 16-07-22.
 */
public class MallPay {

    private String outerOrigin;
    private String couponName;// 优惠券名称
    private String orderId;// 订单id
    private String city;// 订单发往地址城市
    private String couponTemplateId;// 优惠券模板id
    private String userId;
    private String orderItems;// 本订单的商品列表
    private String orderTime;// 下订单时间
    private String orderAmount;// 订单总额
    private String payAmount;// 应支付金额
    private String province;// 订单发往省
    private String payMethod;// 支付方式
    private String district;// 发往区域
    private String userIp;// 用户下订单的ip
    private String topic;
    private String innerOrigin;// 下订单的方式
    private String paySuccessTime; // 支付成功时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPaySuccessTime() {
        return paySuccessTime;
    }

    public void setPaySuccessTime(String paySuccessTime) {
        this.paySuccessTime = paySuccessTime;
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
        return "MallPay{" +
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
                ", payMethod='" + payMethod + '\'' +
                ", district='" + district + '\'' +
                ", userIp='" + userIp + '\'' +
                ", topic='" + topic + '\'' +
                ", innerOrigin='" + innerOrigin + '\'' +
                ", paySuccessTime='" + paySuccessTime + '\'' +
                '}';
    }
}
