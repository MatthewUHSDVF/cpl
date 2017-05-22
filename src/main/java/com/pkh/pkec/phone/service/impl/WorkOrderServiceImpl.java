package com.pkh.pkec.phone.service.impl;

import com.pkh.pkcore.commons.JsonUtils;
import com.pkh.pkcore.exception.CustomException;
import com.pkh.pkec.phone.mapper.CrmOptionItemMapper;
import com.pkh.pkec.phone.mapper.CustomerManageMapper;
import com.pkh.pkec.phone.mapper.WorkOrderMapper;
import com.pkh.pkec.phone.mapper.WorkorderCustomMapper;
import com.pkh.pkec.phone.po.*;
import com.pkh.pkec.phone.service.WorkOrderService;
import com.pkh.pkec.phone.util.NaturalRecorderUtil;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Created by Administrator on 2016/9/22.
 */
public class WorkOrderServiceImpl implements WorkOrderService {

    static private Logger logger = LoggerFactory.getLogger(Logger.class);

    @Value("${filePath}")
    private String filePath;

    private WorkOrderMapper workOrderMapper;

    private CrmOptionItemMapper crmOptionItemMapper;

    private WorkorderCustomMapper workorderCustomMapper;

    private CustomerManageMapper customerManageMapper;

    @Override
    public int insertWorkOrder(WorkOrder workOrder) {
        String result = downloadNaturalRecordee(workOrder.getSoundRecording());
        if (!"0".equals(result)){
            workOrder.setSoundRecording(result);
        } else {
            workOrder.setSoundRecording(null);
        }
        int i = workOrderMapper.insertSelective(workOrder);
        return i;
    }

    public String downloadNaturalRecordee(String soundRecording){
        return NaturalRecorderUtil.downloadFromUrl(soundRecording,filePath);

    }

    @Override
    public WorkOrder selectInfo(String serialNo) {
        WorkOrderExample workOrderExample = new WorkOrderExample();
        workOrderExample.createCriteria().andSerialNoEqualTo(serialNo);
        List<WorkOrder> workOrderList = workOrderMapper.selectByExample(workOrderExample);
        return workOrderList.get(0);
    }

    @Override
    public Integer deleteWorkOrderBySerialNo(String serialNo) {
        WorkOrderExample workOrderExample = new WorkOrderExample();
        workOrderExample.createCriteria().andSerialNoEqualTo(serialNo);
        return workOrderMapper.deleteByExample(workOrderExample);
    }

    @Override
    public List<CrmOptionItem> selectCrmOptionItemByOptionTeamId(int i) {
        CrmOptionItemExample crmOptionItemExample = new CrmOptionItemExample();
        crmOptionItemExample.createCriteria().andOptionTeamIdEqualTo(i);
        List<CrmOptionItem> crmOptionItemList = crmOptionItemMapper.selectByExample(crmOptionItemExample);
        return crmOptionItemList;
    }

    @Override
    public Map<String,List<CrmOptionItem>> selectAllCrmOptionItem(){
        Map<String,List<CrmOptionItem>> map = new HashMap<String, List<CrmOptionItem>>();
        List<CrmOptionItem> list = crmOptionItemMapper.selectAll();
        List<List> crmOptionItemList = new ArrayList<List>();
        map.put(list.get(0).getOptionGroupId(),new ArrayList<CrmOptionItem>());
        for (CrmOptionItem l : list) {
            if (map.containsKey(l.getOptionGroupId())) {
                map.get(l.getOptionGroupId()).add(l);
            } else {
                map.put(l.getOptionGroupId(),new ArrayList<CrmOptionItem>());
                map.get(l.getOptionGroupId()).add(l);
            }
        }
        return map;
    }

    @Override
    public List<WorkOrder> selectListByVo(WorkorderQueryVo workorderQueryVo) {
        return workorderCustomMapper.selectWorkorderListByQueryVo(workorderQueryVo);
    }

    @Override
    public Map selectListByVoWithoutPage(WorkorderQueryVo workorderQueryVo) {
        Map map = new HashMap();
        List<WorkOrderAndCustomerInfo> list = workorderCustomMapper.selectWorkorderListByQueryVoWithoutPage(workorderQueryVo);
        List<Map> mapList = new ArrayList<Map>();
        for(WorkOrderAndCustomerInfo m : list){
            mapList.add(beanToMap(m));
        }
//        for (Map m : mapList){
//        if("1".equals(m.get("sex"))) {
//            m.put("sex", "男");
//        }else  if ("2".equals(m.get("sex"))) {
//            m.put("sex","女");
//        } else if ("0".equals(m.get("sex"))) {
//            m.put("sex","未知");
//        }
//
//        if("1".equals(m.get("married"))) {
//            m.put("married", "未婚");
//        }else  if ("2".equals(m.get("married"))) {
//            m.put("married","已婚");
//        }
//
//        if("1".equals(m.get("workorderType"))) {
//            m.put("workorderType", "咨询工单");
//        }else  if ("2".equals(m.get("workorderType"))) {
//            m.put("workorderType","投诉工单");
//        }
//
//        if("0".equals(m.get("finished"))) {
//            m.put("finished", "未完成");
//        }else  if ("1".equals(m.get("finished"))) {
//            m.put("finished","完成");
//        }
//        }
        map.put("rows",mapList);
        map.put("errorCode", 0);
        map.put("errorMessage", "查询成功");
        return map;
    }

    //将javabean转为map类型，然后返回一个map类型的值
    public Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!StringUtils.equals(name, "class")) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }
    @Override
    public Map insertWorkOrderByExcel(Map map) {
        String methodName = "insertWorkOrderByExcel(),";
        Map<String,Object> mapper = new HashMap<String,Object>();
        CustomerManageExample customerManageExample = new CustomerManageExample();
        try {
            List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
            for (Map m : (List<Map>)map.get("rows")){
                CustomerManage customerManage = JsonUtils.map2pojo(m,CustomerManage.class);
                WorkOrder workOrder = JsonUtils.map2pojo(m,WorkOrder.class);
                customerManageExample.createCriteria().andCustomerPhoneEqualTo(customerManage.getCustomerPhone());
                List<CustomerManage> customerManages = customerManageMapper.selectByExample(customerManageExample);
                if (customerManages.size() > 0) {
                    workOrder.setCustomerId(customerManages.get(0).getId().toString());
                }
                workOrders.add(workOrder);
            }
            batchInsertWorkOrders(workOrders);
            mapper.put("errorCode", 0);
            mapper.put("errorMessage", "执行成功");
        }catch (Exception e){
            logger.error(methodName, e);
            mapper.put("errorCode", 9);
            mapper.put("errorMessage", "未知错误");
        }
        return mapper;
    }

    public void batchInsertWorkOrders(List<WorkOrder> list) throws CustomException {
        // process
        try{
            for (WorkOrder workOrder : list){
                insertWorkOrder(workOrder);
            }
        }catch(CustomException e){
            throw e;
        }catch (Exception e){
            throw new CustomException(9,"未知错误");
        }
    }



//    @Override
//    public void downLoadWorkOrder(WorkorderQueryVo workorderQueryVo,HttpServletResponse response){
//        HSSFWorkbook hssfwb = new HSSFWorkbook();
//        HSSFSheet sheet = hssfwb.createSheet("工单表");
//        HSSFRow row = sheet.createRow((int) 0 );
//        HSSFCellStyle style = hssfwb.createCellStyle();
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        HSSFCell cell = row.createCell((short) 0);
//        cell.setCellValue("流水号");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 1);
//        cell.setCellValue("用户姓名");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 2);
//        cell.setCellValue("电话");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 3);
//        cell.setCellValue("性别");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 4);
//        cell.setCellValue("婚否");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 5);
//        cell.setCellValue("卡号");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 6);
//        cell.setCellValue("卡余额");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 7);
//        cell.setCellValue("冻结余额");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 8);
//        cell.setCellValue("可用余额");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 9);
//        cell.setCellValue("卡状态");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 10);
//        cell.setCellValue("创建时间");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 11);
//        cell.setCellValue("开始时间");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 12);
//        cell.setCellValue("结束时间");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 13);
//        cell.setCellValue("工单类型");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 14);
//        cell.setCellValue("咨询内容");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 15);
//        cell.setCellValue("处理状态");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 16);
//        cell.setCellValue("处理人");
//        cell.setCellStyle(style);
//
//        List<WorkOrder> list = selectListByVo(workorderQueryVo);
//
//        for(int i=0;i<list.size();i++){
//            row = sheet.createRow((int) i+1);
//            WorkOrder workOrder = list.get(i);
//            Field[] fields=new WorkOrder().getClass().getDeclaredFields();
//            String[] fieldNames=new String[fields.length];
//            for(int j=0;j<fields.length;j++){
////                System.out.println(fields[j].getType());
//                fieldNames[j]=fields[j].getName();
//            }
//            for (int k = 0; k<fieldNames.length;k++){
//                if("mobile2".equals(fieldNames[k])){
//                    continue;
//                }
//                try {
//                    String firstLetter = fieldNames[k].substring(0, 1).toUpperCase();
//                    String getter = "get" + firstLetter + fieldNames[k].substring(1);
//                    Method method = workOrder.getClass().getMethod(getter, new Class[] {});
//                    Object value = method.invoke(workOrder, new Object[] {});
//                    if(value==null){
//                        continue;
//                    }
//                    if("createtime".equals(fieldNames[k]) || "begintime".equals(fieldNames[k]) || "endtime".equals(fieldNames[k])){
//                        row.createCell((short) k).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
//                    }else if ("sex".equals(fieldNames[k])){
//                        if ("1".equals(value.toString())){
//                            row.createCell((short) k).setCellValue("男");
//                        } else if ("2".equals(value.toString())) {
//                            row.createCell((short) k).setCellValue("女");
//                        }else {
//                            row.createCell((short) k).setCellValue("未知");
//                        }
//                    }else if ("married".equals(fieldNames[k])){
//                        if ("1".equals(value.toString())){
//                            row.createCell((short) k).setCellValue("未婚");
//                        } else if ("2".equals(value.toString())) {
//                            row.createCell((short) k).setCellValue("已婚");
//                        }else {
//                            row.createCell((short) k).setCellValue("未知");
//                        }
//                    }else if ("cardStatus".equals(fieldNames[k])){
//                        if ("0".equals(value.toString())){
//                            row.createCell((short) k).setCellValue("未激活");
//                        } else if ("1".equals(value.toString())) {
//                            row.createCell((short) k).setCellValue("已激活，正常状态");
//                        }else if ("6".equals(value.toString())) {
//                            row.createCell((short) k).setCellValue("挂失状态");
//                        }else if ("8".equals(value.toString())) {
//                            row.createCell((short) k).setCellValue("锁定状态");
//                        }else if ("9".equals(value.toString())) {
//                            row.createCell((short) k).setCellValue("作废");
//                        }else {
//                            row.createCell((short) k).setCellValue("未知");
//                        }
//                    }else if ("workorderType".equals(fieldNames[k])){
//                        if ("1".equals(value.toString())){
//                            row.createCell((short) k).setCellValue("咨询工单");
//                        } else if ("2".equals(value.toString())) {
//                            row.createCell((short) k).setCellValue("投诉工单");
//                        }
//                    }else if ("finished".equals(fieldNames[k])){
//                        if ("0".equals(value.toString())){
//                            row.createCell((short) k).setCellValue("未完成");
//                        } else if ("1".equals(value.toString())) {
//                            row.createCell((short) k).setCellValue("完成");
//                        }
//                    }else {
//                        row.createCell((short) k).setCellValue((String) value);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        try {
//            String fileName = "workOrder";
//            OutputStream out = response.getOutputStream();
//            response.reset();
//            response.setHeader("content-disposition",
//                    "attachment;filename="
//                            + new String((fileName).getBytes("gb2312"), "ISO8859-1") + ".xls");
//            response.setContentType("APPLICATION/msexcel");
//            hssfwb.write(out);
//            out.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }







    public void setWorkOrderMapper(WorkOrderMapper workOrderMapper) {
        this.workOrderMapper = workOrderMapper;
    }


    public void setCrmOptionItemMapper(CrmOptionItemMapper crmOptionItemMapper) {
        this.crmOptionItemMapper = crmOptionItemMapper;
    }

    public void setWorkorderCustomMapper(WorkorderCustomMapper workorderCustomMapper) {
        this.workorderCustomMapper = workorderCustomMapper;
    }


    public void setCustomerManageMapper(CustomerManageMapper customerManageMapper) {
        this.customerManageMapper = customerManageMapper;
    }
}
