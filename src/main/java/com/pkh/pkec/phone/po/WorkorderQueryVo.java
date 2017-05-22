package com.pkh.pkec.phone.po;

import com.pkh.pkcore.vo.QueryVo;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/26.
 */
public class WorkorderQueryVo extends QueryVo {
    private String serialNo;
    private String customerName;
    private String customerType;
    private String mobile;
    private String cardCode;
    private String createtime;
    private Date createFromTime;
    private Date createToTime;
    private String workorderType;
    private String finished;
    private String handler;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }


    public String getWorkorderType() {
        return workorderType;
    }

    public void setWorkorderType(String workorderType) {
        this.workorderType = workorderType;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Date getCreateFromTime() {
        return createFromTime;
    }

    public void setCreateFromTime(Date createFromTime) {
        this.createFromTime = createFromTime;
    }

    public Date getCreateToTime() {
        return createToTime;
    }

    public void setCreateToTime(Date createToTime) {
        this.createToTime = createToTime;
    }

    @Override
    public String toString() {
        return "WorkorderQueryVo{" +
                "serialNo='" + serialNo + '\'' +
                ", customerName='" + customerName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cardCode='" + cardCode + '\'' +
                ", createtime='" + createtime + '\'' +
                ", createFromTime=" + createFromTime +
                ", createToTime=" + createToTime +
                ", workorderType='" + workorderType + '\'' +
                ", finished='" + finished + '\'' +
                ", handler='" + handler + '\'' +
                '}';
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}
