package com.pkh.pkec.phone.service;

import com.pkh.pkec.phone.po.CustomerManage;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public interface CustomerManageService {

    List<CustomerManage> queryCustomerByCustomerPhoneAndCustomerName(String customerName,String customerPhone);

    int insertCustomerManage(CustomerManage customerManage);

    int updateCustomerManage(CustomerManage customerManage);

    List<CustomerManage> selectCustomerByCustomerPhone(String mobile);

}
