package com.pkh.pkec.phone.service.impl;

import com.pkh.pkcore.commons.HttpClientUtils;
import com.pkh.pkcore.commons.JsonUtils;
import com.pkh.pkec.order.po.QueryOrderListVo;
import com.pkh.pkec.phone.service.SubscribeMecOrderService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by slipkinem on 2016/9/29.
 */
public class SubscribeMecOrderServiceImpl implements SubscribeMecOrderService {

    private String PKEC;

    public SubscribeMecOrderServiceImpl(String PKEC){
        this.PKEC = PKEC;
    }

    @Override
    public Map<String,Object> getMecOrder(String cardCode,String refCode) {
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,String> param = new HashMap<String,String>();
//        param.put("cardCode",cardCode);
//        param.put("refCode",refCode);
        String json = HttpClientUtils.doPost(PKEC + "/order/query_order",param);
        return map;
    }

    @Override
    public Map<String,Object> getMECOrderByQueryOrderListVo(QueryOrderListVo queryOrderListVo) {
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            String json = HttpClientUtils.doPost(PKEC + "/order/getMECOrderByQueryOrderListVo", setConditionMap(queryOrderListVo));
            return  JsonUtils.json2map(json);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("errorMsg",e);
            return map;
        }
    }

    public static Map<String, String> setConditionMap(Object obj){
        Map<String, String> map = new HashMap<String, String>();
        if(obj==null){
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            String fieldName =  field.getName();
            if(getValueByFieldName(fieldName,obj)!=null)
                map.put(fieldName, String.valueOf(getValueByFieldName(fieldName,obj)));
        }

        return map;

    }
    /**
     * 根据属性名获取该类此属性的值
     * @param fieldName
     * @param object
     * @return
     */
    private static Object getValueByFieldName(String fieldName,Object object){
        String firstLetter=fieldName.substring(0,1).toUpperCase();
        String getter = "get"+firstLetter+fieldName.substring(1);
        try {
            Method method = object.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(object, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }

    }


//    public static Map<String,String> convertBean(Object bean)
//            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
//        Class type = bean.getClass();
//        Map<String,String> returnMap = new HashMap<String,String>();
//        BeanInfo beanInfo = Introspector.getBeanInfo(type);
//
//        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
//        for (int i = 0; i< propertyDescriptors.length; i++) {
//            PropertyDescriptor descriptor = propertyDescriptors[i];
//            String propertyName = descriptor.getName();
//            if (!propertyName.equals("class")) {
//                Method readMethod = descriptor.getReadMethod();
//                String result = (String) readMethod.invoke(bean, new Object[0]);
//                if (result != null) {
//                    returnMap.put(propertyName, result);
//                } else {
//                    returnMap.put(propertyName, "");
//                }
//            }
//        }
//        return returnMap;
//    }
}
