package com.pkh.pkec.phone.controller;

import com.pkh.pkcore.commons.PKExcelTemplate;
import com.pkh.pkcore.po.card.InsuCardCustom;
import com.pkh.pkcore.po.insurance.InsuPerson;
import com.pkh.pkec.order.model.CrmSubscribe;
import com.pkh.pkec.order.po.PKECOrderTag;
import com.pkh.pkec.order.po.QueryOrderListVo;
import com.pkh.pkec.order.service.OrderService;
import com.pkh.pkec.phone.po.*;
import com.pkh.pkec.phone.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/9/2.
 */
@Controller
public class CrmController {

    private static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Autowired
    private PhoneMoileActivateService phoneMoileActivateService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private CustomerManageService customerManageService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubscribeMecOrderService subscribeMecOrderService;

    @RequestMapping(value = "/getOptionItemList")
    @ResponseBody
    public Map<String, List<CrmOptionItem>> getOptionItemList(){
        Map<String,List<CrmOptionItem>> map = new HashMap<String, List<CrmOptionItem>>();
        map = workOrderService.selectAllCrmOptionItem();
        return map;
    }

    /***
     * 来电唤醒crm弹屏
     * @param phoneTransfer
     * @param request
     * @return
     */
    @RequestMapping(value = "/crmWake")
    @ResponseBody
    public Map<String,Object> cpmWake(PhoneTransfer phoneTransfer, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        InsuCardCustom insuCard = new InsuCardCustom();
        InsuPerson insuPerson = new InsuPerson();
        Map<String,List<CrmOptionItem>> crmOptionItemMap = new HashMap<String, List<CrmOptionItem>>();
        if (phoneTransfer.getFromCid()!=null && !"".equals(phoneTransfer.getFromCid())){
            crmOptionItemMap = getOptionItemList();
//            for (int i = 1 ; i < 6 ; i++){
//                List<CrmOptionItem> list = workOrderService.selectCrmOptionItemByOptionTeamId(i);
//                crmOptionItemList.add(list);
//            }
            try{
                List<CustomerManage> customerManages = customerManageService.selectCustomerByCustomerPhone(phoneTransfer.getFromCid());
                if (customerManages.size()>0){
                    CustomerManage customerManage = customerManages.get(0);
                    map.put("isCrm",1);
                    map.put("customerManage",customerManage);
                }else {
                    insuCard = phoneMoileActivateService.getInsuCardByMobile(phoneTransfer.getFromCid());
                    if (insuCard.getCardCode() != null && !"".equals(insuCard.getCardCode())){
                        insuPerson = phoneMoileActivateService.selectPersonInfo(insuCard.getCardPersoncode());
                        map.put("isCrm",2);
                        map.put("insuCard",insuCard);
                        map.put("insuPerson",insuPerson);
                    }
                }
            } catch (Exception e) {
                map.put("errorCode","1");
                map.put("errorMessage","运行过程发生异常");
                return map;
            }
        }
        map.put("crmOptionItemList",crmOptionItemMap);
        return map;
    }

    /***
     * 提交工单
     * @param params
     * @return
     */
    @RequestMapping(value = "/commitWorkOrder")
    @ResponseBody
    public Map<String,Object> commitWorkOrder(WorkOrderCustom params) {
        Map<String,Object> map = new HashMap<String, Object>();
        String customerCode = "";

        try {
//            查看客户表中是否已存在，不存在则新增
            List<CustomerManage> customerManages = customerManageService.queryCustomerByCustomerPhoneAndCustomerName(params.getCustomerName(), params.getMobile());
            if (!(customerManages.size() > 0)) {
                CustomerManage customerManage = new CustomerManage();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                customerManage.setCustomerCode(format.format(new Date()));
                customerManage.setCustomerName(params.getCustomerName());
                customerManage.setCustomerPhone(params.getMobile());
                customerManage.setCustomerSex(params.getSex());
                customerManage.setCustomerType(params.getCustomerType());
                customerManage.setCustomerAddress(params.getCustomerAddress());
                customerManage.setCustomerEmail(params.getCustomerEmail());
                customerManage.setBindCard(params.getCardCode());
                customerManage.setCustomerHandler(params.getCustomerHandler());
                customerManage.setCustomerRemark(params.getCustomerRemark());
                customerManage.setCustomerRank(params.getCustomerRank());
                customerManage.setWeixin(params.getWeixin());
                customerManageService.insertCustomerManage(customerManage);
                customerCode = customerManage.getCustomerCode(); //获取到的即为新插入记录的ID
            } else {
                customerCode = customerManages.get(0).getCustomerCode();
            }
        }catch (Exception e){
            map.put("errorCode",1);
            map.put("errorMessage","新用户添加失败！请重新添加并及时更新工单");
            e.printStackTrace();
        }
    try{
            WorkOrder workOrder = new WorkOrder();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Random random = new Random();
            workOrder.setSerialNo(format.format(new Date())+(random.nextInt(90)+10));
            workOrder.setCreatetime(new Date());
            workOrder.setCustomerId(customerCode);
            workOrder.setBegintime(params.getBegintime());
            workOrder.setEndtime(params.getEndtime());
            workOrder.setChannel(params.getChannel());
            workOrder.setCallStates(params.getCallStates());
            workOrder.setWorkorderType(params.getWorkorderType());
            workOrder.setFeedback(params.getFeedback());
            workOrder.setStatus(params.getStatus());
            workOrder.setHandler(params.getHandler());
            workOrder.setSoundRecording(params.getSoundRecording());
            workOrderService.insertWorkOrder(workOrder);
            map.put("errorCode",0);
            return map;
        } catch (Exception e) {
            map.put("errorCode",1);
            e.printStackTrace();
            return map;
        }
    }

    /***
     * 根据条件查询工单
     * @param workorderQueryVo
     * @return
     */
    @RequestMapping(value = "/selectListByWorkorderQueryVo")
    @ResponseBody
    public List<WorkOrder> selectListByWorkorderQueryVo(WorkorderQueryVo workorderQueryVo){
        return workOrderService.selectListByVo(workorderQueryVo);
    }

    /***
     * 根据流水号删除工单
     * @param serialNo
     * @return
     */
    @RequestMapping(value = "deleteWorkOrderBySerialNo")
    @ResponseBody
    public Map deleteWorkOrderBySerialNo(String serialNo){
        Map resultMap = new HashMap();
        if(serialNo != null){
            try{
               int i = workOrderService.deleteWorkOrderBySerialNo(serialNo);
                resultMap.put("list",i);
                resultMap.put("errorCode",0);
            }catch (Exception e){
                resultMap.put("errorCode",1);
                resultMap.put("errorMessage",e.getMessage());
            }
        }
        return resultMap;
    }

    /**
     * 工单详情
     * @param serialNo
     * @return
     */
    @RequestMapping(value = "/selectInfo")
    @ResponseBody
    public WorkOrder selectInfo(String serialNo){
        return workOrderService.selectInfo(serialNo);
    }

    /**
     * 上传excel
     * @param file
     * @param map
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "uploadWorkOrderInExcel",method = RequestMethod.POST)
    @ResponseBody
    public Map uploadWorkOrderI (@RequestParam("file") MultipartFile file, @RequestParam Map map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String methodName = "uploadWorkOrderInExcel(),";
        String templateFileName = "/excel/upLoadWorkOrder_template.xls";

        Map resultMap = new HashMap();
        templateFileName = request.getSession().getServletContext().getRealPath(templateFileName);
        Map dest = new HashMap();

        PKExcelTemplate.pumpExcelStream2Map(file.getInputStream(),templateFileName,dest);

        resultMap = workOrderService.insertWorkOrderByExcel(dest);

        return resultMap;
    }

    @RequestMapping(value = "/downLoadWorkOrder")
    @ResponseBody
    public void downLoadWorkOrder(HttpServletRequest request,HttpServletResponse response,WorkorderQueryVo workorderQueryVo){
        // init
        String methodName = "downLoadWorkOrder(),";
        String templateFileName = "/excel/downLoadWorkOrder.xls";
        String targetFileName = "普康宝系统工单信息表.xls";
        logger.debug(methodName.concat("mapBusiTransQueryVo = "),workorderQueryVo.toString());

        // process
        try{
            // 文件输出
            Map resultMap = workOrderService.selectListByVoWithoutPage(workorderQueryVo);
            response.reset();
            response.setContentType("application/x-msdownload");
            response.setContentType("application/x-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(targetFileName.getBytes("utf-8"), "ISO8859-1"));
            PKExcelTemplate.pumpMap2ExcelStream(resultMap, request.getSession().getServletContext().getRealPath(templateFileName), response.getOutputStream());
        }catch(Exception e){
            logger.error(methodName, e);
        }
    }

    /***
     * 查看预约列表
     */
    @RequestMapping(value = "/subscribeInfo")
    @ResponseBody
    public Map<String ,Object> getMecOrder(QueryOrderListVo queryOrderListVo){
        Map<String,Object> map = new HashMap<String, Object>();
        queryOrderListVo.setCurrentPage((queryOrderListVo.getCurrentPage() -1) * 10);
        List<CrmSubscribe> orderList = orderService.getMECOrderByQueryOrderListVo(queryOrderListVo);
        map.put("orderList",orderList);
        return map;
    }

    @RequestMapping(value = "/getMECOrderByQueryOrderListVo")
    @ResponseBody
    public Map<String ,Object> getMECOrderByQueryOrderListVo(QueryOrderListVo queryOrderListVo){
        Map<String,Object> map = new HashMap<String, Object>();
//        queryOrderListVo.setCurrentPage(1);
//        queryOrderListVo.setPerPage(10);
//        queryOrderListVo.setUserTelephone("15651309318");
//        queryOrderListVo.setOrderStates("1");
        queryOrderListVo.setCurrentPage((queryOrderListVo.getCurrentPage() -1) * 10);
        map = subscribeMecOrderService.getMECOrderByQueryOrderListVo(queryOrderListVo);
//        map.put("orderList",orderList);
        return map;
    }


    /**
     * 預約詳情
     */
    @RequestMapping(value = "/subscribeInfoByOrderCode")
    @ResponseBody
    public Map<String ,Object> getMecOrderinfo(String orderCode){
        Map<String,Object> map = new HashMap<String, Object>();
        List<PKECOrderTag> orderTagList = orderService.getOrderTagList(orderCode);
        map.put("orderTagList",orderTagList);
        return map;
    }

    /**
     * 通话记录流水
     */
    @RequestMapping(value = "/callRecords")
    @ResponseBody
    public Map<String ,Object> insertCallRecords(String orderCode){
        Map<String,Object> map = new HashMap<String, Object>();
        List<PKECOrderTag> orderTagList = orderService.getOrderTagList(orderCode);
        map.put("orderTagList",orderTagList);
        return map;
    }

    /**
     * 添加客户
     */
    @RequestMapping(value = "/insertCustomerManage")
    @ResponseBody
    public  Map<String,Object> insertCustomerManage(CustomerManage customerManage){
        Map<String,Object> map = new HashMap<String, Object>();
        int result = customerManageService.insertCustomerManage(customerManage);
        map.put("result",result);
        return map;
    }

    @RequestMapping(value = "/updateCustomerManage")
    @ResponseBody
    public Map<String,Object> updateCustomerManage(CustomerManage customerManage){
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            customerManageService.updateCustomerManage(customerManage);
            map.put("result","1");

        } catch (Exception e){
            e.printStackTrace();
            map.put("result","0");
            map.put("errorMessage","更新过程出现异常,请尝试重新操作！");
        }
        return map;
    }

    @RequestMapping(value = "/queryCustomerManage")
    @ResponseBody
    public Map<String,Object> queryCustomerManage(String customerName,String customerPhone){
        Map<String,Object> map = new HashMap<String, Object>();
        List<CustomerManage> customerManages = customerManageService.queryCustomerByCustomerPhoneAndCustomerName(customerName,customerPhone);
        if (customerManages.size()>0){
            map.put("errorCode",0);
            map.put("customerManages",customerManages);
            return map;
        }
        map.put("errorCode",1);
        map.put("errorMessage","您所输入的信息不存在!");
        return map;
    }
}
