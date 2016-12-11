package com.gao.nettyinaction.seri;

import java.io.Serializable;

/**
 * User: wangchen
 * Date: 2016/12/11
 * Time: 23:42
 */
public class SubscribeResp implements Serializable {


    private static final long serialVersionUID = 6840255707973503504L;

    /**
     * 订购编号
     */
    private int subReqID;

    /**
     * 订购结果，0标示成功
     */
    private int respCode;

    /**
     * 可选的详细描述信息
     */
    private String desc;

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SubscribeResp [subReqID=" + subReqID + ", respCode=" + respCode + ", desc=" + desc + "]";
    }
}
