package com.pkh.pkec.phone.po;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/2.
 */
public class WorkOrderAndCustomerInfo {


    private Integer id;

    private String serialNo;

    private Date createtime;

    private Date begintime;

    private Date endtime;

    private Date updatetime;

    private String channel;

    private String callStates;

    private String workorderType;

    private String feedback;

    private String status;

    private String handler;

    private String customerName;

    private String customerPhone;

    private String customerType;

    private String customerSex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getChannel() {
        if ("1".equals(channel)) {
            return "微信";
        } else if ("2".equals(channel)) {
            return "电话";
        } else {
            return "未知";
        }
    }

    public void setChannel(String channel) {
//        if ("电话".equals(channel)) {
//            this.channel = "2";
//        } else if ("微信".equals(channel)) {
//            this.channel = "1";
//        } else {
//            this.channel = null;
//        }
        this.channel = channel;
    }

    public String getCallStates() {
        if ("1".equals(callStates)) {
            return "呼入";
        } else if ("2".equals(callStates)) {
            return "呼出";
        } else {
            return "其他";
        }
    }

    public void setCallStates(String callStates) {
//        if ("呼出".equals(callStates)) {
//            this.callStates = "2";
//        } else if ("呼入".equals(callStates)) {
//            this.callStates = "1";
//        } else {
//            this.callStates = "3";
//        }
        this.callStates = callStates;
    }

    public String getWorkorderType() {
        if ("1".equals(workorderType)) {
            return "咨询工单";
        } else if ("2".equals(workorderType)) {
            return "投诉工单";
        }else if ("3".equals(workorderType)) {
            return "微信咨询";
        }else if ("4".equals(workorderType)) {
            return "微信投诉";
        }
        return "未知";
    }

    public void setWorkorderType(String workorderType) {
//        if ("投诉工单".equals(workorderType)) {
//            this.workorderType = "2";
//        } else if ("咨询工单".equals(workorderType)) {
//            this.workorderType = "1";
//        }else if ("微信咨询".equals(workorderType)) {
//            this.workorderType = "3";
//        }else if ("微信投诉".equals(workorderType)) {
//            this.workorderType = "4";
//        }
        this.workorderType = workorderType;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        if ("1".equals(status)) {
            return "完成";
        } else if ("2".equals(status)) {
            return "暂存未处理";
        }else if ("0".equals(status)) {
            return "未完成";
        } else {
            return "其他";
        }
    }

    public void setStatus(String status) {
//        if ("暂存未处理".equals(status)) {
//            this.status = "2";
//        } else if ("完成".equals(status)) {
//            this.status = "1";
//        } else if ("未完成".equals(status)) {
//            this.status = "1";
//        } else {
//            this.callStates = null;
//        }
        this.status = status;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerType() {
        if ("1".equals(customerType)) {
            return "商户";
        } else if ("2".equals(customerType)) {
            return "个人";
        } else {
            return "未知";
        }
    }

    public void setCustomerType(String customerType) {
//        if ("个人".equals(customerType)) {
//            this.customerType = "2";
//        } else if ("商户".equals(customerType)) {
//            this.customerType = "1";
//        } else {
//            this.customerType = null;
//        }
        this.customerType = customerType;
    }

    public String getCustomerSex() {
        if ("1".equals(customerSex)) {
            return "男";
        } else if ("2".equals(customerSex)) {
            return "女";
        } else {
            return "未知";
        }
    }

    public void setCustomerSex(String customerSex) {
//        if ("女".equals(customerSex)) {
//            this.customerSex = "2";
//        } else if ("男".equals(customerSex)) {
//            this.customerSex = "1";
//        } else {
//            this.customerSex = "0";
//        }
        this.customerSex = customerSex;
    }
}
