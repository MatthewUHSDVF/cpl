package com.pkh.pkec.phone.mapper;

import com.pkh.pkec.phone.po.WorkOrder;
import com.pkh.pkec.phone.po.WorkOrderAndCustomerInfo;
import com.pkh.pkec.phone.po.WorkorderQueryVo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/26.
 */
public interface WorkorderCustomMapper {

    List<WorkOrder> selectWorkorderListByQueryVo(WorkorderQueryVo workorderQueryVo);

    List<WorkOrderAndCustomerInfo> selectWorkorderListByQueryVoWithoutPage(WorkorderQueryVo workorderQueryVo);

}
