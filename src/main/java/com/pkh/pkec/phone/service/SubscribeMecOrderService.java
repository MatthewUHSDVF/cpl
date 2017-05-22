package com.pkh.pkec.phone.service;

import com.pkh.pkec.order.po.QueryOrderListVo;

import java.util.Map;

/**
 * Created by slipkinem on 2016/9/29.
 */
public interface SubscribeMecOrderService {
    Map<String,Object> getMecOrder(String cardCode,String refCode);
    Map<String,Object> getMECOrderByQueryOrderListVo(QueryOrderListVo queryOrderListVo);
}
