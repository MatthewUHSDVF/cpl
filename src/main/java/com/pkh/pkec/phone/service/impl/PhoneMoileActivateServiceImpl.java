package com.pkh.pkec.phone.service.impl;

import com.pkh.pkcore.commons.HttpClientUtils;
import com.pkh.pkcore.commons.JsonUtils;
import com.pkh.pkcore.exception.CustomException;
import com.pkh.pkcore.po.card.InsuCardCustom;
import com.pkh.pkcore.po.insurance.InsuPersonCustom;
import com.pkh.pkec.phone.service.PhoneMoileActivateService;
import com.pkh.pkec.phone.util.CnUpperCaser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/31.
 */
public class PhoneMoileActivateServiceImpl implements PhoneMoileActivateService {

    private String PKCORE;

    public PhoneMoileActivateServiceImpl(String PKCORE){
        this.PKCORE = PKCORE;
    }

    @Override
    public String SendCaptcha(String mobile) throws Exception {
        Map<String,String> params = new HashMap<String,String>();
        params.put("userCode", mobile);
        params.put("mobile", mobile);

        return HttpClientUtils.doPost(PKCORE+"/captcha/requestAndSendCaptcha", params);
    }

    @Override
    public Boolean checkUpCaptcha(String mobile, String captcha) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userCode", mobile);
        params.put("captcha", captcha);
        String json = HttpClientUtils.doPost(PKCORE + "/captcha/validateCaptcha", params);
        JSONObject jo = JSONObject.fromObject(json);
        Integer errorCode = jo.getInt("errorCode");
        if (errorCode == 0) {
            return true;
        } else {
            throw new CustomException(errorCode,jo.getString("errorMessage"));
        }
    }

    @Override
    public Boolean activeCard(String cardCode,String idCard){
        Map params = new HashMap<String, String>();
        if (cardCode.length() == 11 && cardCode.startsWith("1")) {
            params.put("cardCode", getCardCodeByMobile(cardCode));
        } else {
            params.put("cardCode", cardCode);
        }
        params.put("personCertId", idCard);
        String json = HttpClientUtils.doPost(PKCORE + "/card/activeCardByPersonCertInfo", params);
        JSONObject jo = JSONObject.fromObject(json);
        String errorMessage = jo.getString("errorMessage");
        int errorCode = jo.getInt("errorCode");
        if (errorCode != 0) {
            throw new CustomException(errorCode, errorMessage);
        } else {
            return true;
        }
    }


    public String getCardCodeByMobile(String mobile) {
        Map params = new HashMap<String, String>();
        params.put("bindMobile", mobile);
        String json = HttpClientUtils.doPost(PKCORE + "/card/getCardCodeByBindMobile", params);
        JSONObject jo = JSONObject.fromObject(json);
        int errorCode = jo.getInt("errorCode");
        String errorMessage = jo.getString("errorMessage");
        if (errorCode != 0) {
            throw new CustomException(errorCode, errorMessage);
        } else {
            return jo.getString("cardCode");
        }
    }


    @Override
    public Boolean bindMobile(String mobile,String cardCode){
        Map params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("cardCode", cardCode);
        String json = HttpClientUtils.doPost(PKCORE + "/card/bindMobileWithoutVerify", params);
        JSONObject jo = JSONObject.fromObject(json);
        String errorMessage = jo.getString("errorMessage");
        int errorCode = jo.getInt("errorCode");
        if (errorCode != 0) {
            throw new CustomException(errorCode, errorMessage);
        } else {
            return true;
        }
    }


    @Override
    public String getBalance(String cardCode, String password) {
        Map<String,Object> map = new HashMap<String, Object>();
        if (cardCode.length() == 11 && cardCode.startsWith("1")) {
            if (checkUpCaptcha(cardCode,password)){
                cardCode = getCardCodeByMobile(cardCode);
                map = getCardAccountAmt(cardCode);
                return "200-"+CnUpperCaser.numberToChinese(map.get("amt").toString());
            }
        }
        if(checkPassword(cardCode,password)){

            map = getCardAccountAmt(cardCode);

            return "200-"+CnUpperCaser.numberToChinese(map.get("amt").toString());
        }
        return "400";
    }

    @Override
    public String changePassword(String cardCode, String oldPassword, String newPassword) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("cardCode", cardCode);
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        String json = HttpClientUtils.doPost(PKCORE + "/card/changePassword", params);
        try {
            JSONObject jo = JSONObject.fromObject(json);
            if (jo.getInt("errorCode") == 0) {
                return jo.get("errorCode").toString();
            }
            return jo.get("errorMessage").toString();
        } catch (Exception e) {
            return "解析异常";
        }
    }


    @Override
    public Boolean reportLoss(String personCertId){
        Map<String, String> params = new HashMap<String, String>();

        params.put("personCertId", personCertId);
        String json = HttpClientUtils.doPost(PKCORE + "/card/lockCardByIdCard", params);
        try {
            JSONObject jo = JSONObject.fromObject(json);
            if (jo.getInt("errorCode") == 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public boolean checkPassword(String cardNo, String password) {
        Map<String, String> params = new HashMap<String, String>();
        if (cardNo.length() == 11 && cardNo.startsWith("1")) {
            String cardCode = getCardCodeByMobile(cardNo);
            params.put("userCode", cardCode);
            params.put("captcha", password);
            String json = HttpClientUtils.doPost(PKCORE + "/captcha/validateCaptcha", params);
            JSONObject jo = JSONObject.fromObject(json);
            Integer errorCode = jo.getInt("errorCode");
            if (errorCode == 0) {
                return true;
            } else {
                throw new CustomException(jo.getInt("errorCode"), jo.getString("errorMessage"));
            }
        } else {
            params.put("cardCode", cardNo);
            params.put("password", password);
            String json = HttpClientUtils.doPost(PKCORE + "/card/verifyPassword", params);
            JSONObject jo = JSONObject.fromObject(json);
            Integer errorCode = jo.getInt("errorCode");
            if (errorCode == 0) {
                return true;
            } else {
                throw new CustomException(jo.getInt("errorCode"), jo.getString("errorMessage"));
            }
        }
}

    public Map<String, Object> getCardAccountAmt(String cardNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map params = new HashMap<String, String>();
        BigDecimal amt = new BigDecimal(0), freezedAmt = new BigDecimal(0);
        InsuCardCustom insuCardCustom = new InsuCardCustom();
        String mobile = "";
        try{
            params.put("cardCode", cardNo);
            params.put("accountLevel", "210"); //只查看个人账户
            String json = HttpClientUtils.doPost(PKCORE + "/account/getAccountByQueryVo", params);
            JSONObject jo = JSONObject.fromObject(json);
            int errorCode = jo.getInt("errorCode");
            String errorMessage = jo.getString("errorMessage");
            int total = jo.getInt("total");
            JSONArray rows = jo.getJSONArray("rows");
            if (errorCode != 0) {
                throw new CustomException(errorCode, errorMessage);
            }

            if (rows.size() == 0) {
                throw new CustomException(2001, errorMessage);
            }

            for (int i = 0; i < rows.size(); i++) {
                JSONObject account = rows.getJSONObject(i);
                amt = amt.add(new BigDecimal(account.getString("accountCurramt")));
                freezedAmt = freezedAmt.add(new BigDecimal(account.getString("accountLockedamt")));
            }

            //get cardInfo
            params = new HashMap<String, String>();
            params.put("cardCode", cardNo);
            json = HttpClientUtils.doPost(PKCORE + "/card/queryCardByCode", params);
            insuCardCustom = JsonUtils.toObject(json, InsuCardCustom.class);

            if(insuCardCustom!=null){
                mobile = insuCardCustom.getCardBindmobile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        map.put("amt", amt);
        map.put("freezedAmt", freezedAmt);
        map.put("mobile", mobile);
        map.put("insuCardCustom", insuCardCustom);
        return map;


    }



    public InsuPersonCustom selectPersonInfo(String personCode){
        Map<String, String> params = new HashMap<String, String>();
        InsuPersonCustom insuPersonCustom = new InsuPersonCustom();
        try {
            params.put("personCode",personCode);
            String json = HttpClientUtils.doPost(PKCORE+"/person/queryPersonInfo",params);
            JSONArray insuPersonCustoms = JSONArray.fromObject(json);
            insuPersonCustom = JsonUtils.toObject(insuPersonCustoms.getJSONObject(0).toString(),InsuPersonCustom.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insuPersonCustom;


    }

    public InsuCardCustom getInsuCardByMobile(String mobile){
        InsuCardCustom insuCardCustom = new InsuCardCustom();
        try {
            Map params = new HashMap<String, String>();
            params.put("cardBindMobile", mobile);
            String json = HttpClientUtils.doPost(PKCORE + "/card/queryCardByQueryVo", params);
            JSONArray userCards = JSONArray.fromObject(json);
            if (userCards.size() > 0){
                insuCardCustom = JsonUtils.toObject( userCards.getJSONObject(0).toString(),InsuCardCustom.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return insuCardCustom;

    }

}
