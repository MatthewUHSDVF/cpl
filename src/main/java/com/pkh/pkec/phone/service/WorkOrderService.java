package com.pkh.pkec.phone.service;

import com.pkh.pkec.phone.po.CrmOptionItem;
import com.pkh.pkec.phone.po.WorkOrder;
import com.pkh.pkec.phone.po.WorkorderQueryVo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/22.
 */
public interface WorkOrderService {
    int insertWorkOrder(WorkOrder workOrder);
    WorkOrder selectInfo(String serialNo);
    Integer deleteWorkOrderBySerialNo(String serialNo);
    List<CrmOptionItem> selectCrmOptionItemByOptionTeamId(int i);
    Map<String,List<CrmOptionItem>> selectAllCrmOptionItem();
    List<WorkOrder> selectListByVo(WorkorderQueryVo workorderQueryVo);
    Map selectListByVoWithoutPage(WorkorderQueryVo workorderQueryVo);
    Map insertWorkOrderByExcel(Map params);

//    void downLoadWorkOrder(WorkorderQueryVo workorderQueryVo);
}
