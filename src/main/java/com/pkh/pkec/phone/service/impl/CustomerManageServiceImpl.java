package com.pkh.pkec.phone.service.impl;

import com.pkh.pkec.phone.mapper.CustomerManageMapper;
import com.pkh.pkec.phone.po.CustomerManage;
import com.pkh.pkec.phone.po.CustomerManageExample;
import com.pkh.pkec.phone.service.CustomerManageService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/10/18.
 */
public class CustomerManageServiceImpl implements CustomerManageService {

    private CustomerManageMapper customerManageMapper;

    @Override
    public List<CustomerManage> queryCustomerByCustomerPhoneAndCustomerName(String customerName,String customerPhone) {
        CustomerManageExample customerManageExample = new CustomerManageExample();
        if(customerPhone!=null&&!"".equals(customerPhone)&&customerName!=null&&!"".equals(customerName)){
            customerManageExample.createCriteria().andCustomerNameEqualTo(customerName).andCustomerPhoneEqualTo(customerPhone);
            return customerManageMapper.selectByExample(customerManageExample);
        } else if (customerPhone!=null&&!"".equals(customerPhone)){
            customerManageExample.createCriteria().andCustomerPhoneEqualTo(customerPhone);
            return customerManageMapper.selectByExample(customerManageExample);
        } else if (customerName!=null&&!"".equals(customerName)){
            customerManageExample.createCriteria().andCustomerNameEqualTo(customerName);
            return customerManageMapper.selectByExample(customerManageExample);
        } else {
            return new ArrayList<CustomerManage>();
        }
    }

    @Override
    public int insertCustomerManage(CustomerManage customerManage) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            customerManage.setCustomerCode(format.format(new Date()));
            customerManage.setCreateTime(new Date());
            return customerManageMapper.insert(customerManage);
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int updateCustomerManage(CustomerManage customerManage){
        return customerManageMapper.updateByPrimaryKey(customerManage);
    }

    @Override
    public List<CustomerManage> selectCustomerByCustomerPhone(String mobile) {
        CustomerManageExample customerManageExample = new CustomerManageExample();
        customerManageExample.createCriteria().andCustomerPhoneEqualTo(mobile);
        return customerManageMapper.selectByExample(customerManageExample);
    }


    public void setCustomerManageMapper(CustomerManageMapper customerManageMapper) {
        this.customerManageMapper = customerManageMapper;
    }
}
