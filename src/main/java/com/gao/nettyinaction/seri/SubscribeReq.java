package com.gao.nettyinaction.seri;

import java.io.Serializable;

/**
 * User: wangchen
 * Date: 2016/12/11
 * Time: 23:40
 */
public class SubscribeReq implements Serializable{

    private static final long serialVersionUID = 2054484526289232140L;

    /**
     * 订购编号
     */
    private int subReqID;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 订购的产品名称
     */
    private String productName;

    /**
     * 订购者手机号码
     */
    private String phoneNumber;

    /**
     * 订购者家庭住址
     */
    private String address;

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "subscribeReq [subReqID=" + subReqID + ", userName=" + userName +
                ",productName=" + productName + ", phoneNumber=" + phoneNumber +
                ", address=" + address + "]";
    }
}
