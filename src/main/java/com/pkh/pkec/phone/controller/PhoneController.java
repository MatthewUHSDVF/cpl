package com.pkh.pkec.phone.controller;

import com.pkh.pkcore.commons.JsonUtils;
import com.pkh.pkcore.exception.CustomException;
import com.pkh.pkec.phone.service.PhoneMoileActivateService;
import com.pkh.pkec.phone.util.MD5Util32LowerCase;
import com.pkh.pkec.phone.util.PWUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/31.
 */
@Controller
@RequestMapping("phone")
public class PhoneController {
    private static Logger logger = LoggerFactory.getLogger(PhoneController.class);
    @Autowired
    private PhoneMoileActivateService phoneMoileActivateService;

    private String strKey="P683DF7035478562";
    private String MD5Key="M683DF7035478562";
//    电话服务激活-发送验证码
    @RequestMapping(value = "/activateCaptcha")
    @ResponseBody
    public String sendCaptchaWithoutValidate (String mobile,String MD5) throws Exception {
        try {
            logger.debug("sendCaptchaWithoutValidate begin.........");
            logger.debug("RequstParam:mobile = {},MD5 = {}",mobile,MD5);
            String m = "mobile="+mobile+"M683DF7035478562";

        if(MD5Util32LowerCase.encryption(m).equals(MD5)){
            String jsonStr =  phoneMoileActivateService.SendCaptcha(mobile);
            Map<String, Object> result = JsonUtils.json2map(jsonStr);
            String s = result.get("errorCode").toString();
            if("0".equals(s)){
                return "200";
            }
        }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return "400";
        }
        return "400";
    }

//    验证激活
    @RequestMapping(value = "/checkUpActivateCode")
    @ResponseBody
    public String checkUpActivateCode(String idCard, String mobile, String captcha, String cardCode, String Password, String MD5, HttpServletRequest httpServletRequest){
        try {
            logger.debug("checkupActivateCode begin.........");
            logger.debug("RequstParam:idCard = {}",idCard);
            logger.debug("RequstParam:mobile = {}",mobile);
            logger.debug("RequstParam:captcha = {}",captcha);
            logger.debug("RequstParam:cardCode = {}",cardCode);
            logger.debug("RequstParam:MD5 = {}",MD5);
            logger.debug("RequstParam:password = {}",Password);
            for(Map.Entry<String, String[]> entry : httpServletRequest.getParameterMap().entrySet())
            {
                logger.debug(entry.getKey()+": "+entry.getValue());
            }
            String m = "idCard="+idCard+"&mobile="+mobile+"&captcha="+captcha+"&cardCode="+cardCode+"&Password="+Password+"M683DF7035478562";
            logger.debug("localMD5={}",MD5Util32LowerCase.encryption(m));

            String m2 = "idCard="+idCard+"&mobile="+mobile+"&captcha="+captcha+"&cardCode="+cardCode+"M683DF7035478562";
            logger.debug("localMD5WithOutPassword={}",MD5Util32LowerCase.encryption(m2));
            if(MD5Util32LowerCase.encryption(m).equals(MD5)){
                if(phoneMoileActivateService.checkUpCaptcha(mobile, captcha)){
                    if(phoneMoileActivateService.activeCard(cardCode, idCard)){
                        if(phoneMoileActivateService.bindMobile(mobile, cardCode)){
                            if (phoneMoileActivateService.checkPassword(cardCode,Password)) {
                                return "200";
                            }
                        }
                    }
                }
            }else {
                logger.error("MD5信息有误:{}",MD5);
            }
        }catch (CustomException ce){
            logger.error("返回有误:{}",ce.getErrorMessage());
            return "400";
        }
        catch (Exception e) {
            logger.error("返回有误:{}",e);
            return "400";
        }
        return "400";
    }

    public static void main(String[] args){
        String m = "idCard="+"342201199111304436"+"&mobile="+"15651309318"+"&captcha="+"4412"+"&cardCode="+"6286090800105937"+"&Password="+"666666"+"M683DF7035478562";
        System.out.println(MD5Util32LowerCase.encryption(m));
    }

//    余额查询
    //由於對端系統不支持AES加密，措意暫時採用明文傳輸
    /*@RequestMapping(value = "/getBalance")
    @ResponseBody
    public String getBalance(String cardCode,String encryptPassword,String MD5){
        String encryptResult = Test.aes_encrypt("246996", "P683DF7035478562");
        System.out.println(encryptResult);
        logger.debug("getBalance begin.........");
        logger.debug("RequstParam:cardCode = {}",cardCode);
        logger.debug("RequstParam:encryptPassword = {}",encryptPassword);
        logger.debug("RequstParam:MD5 = {}",MD5);
        String m = "cardCode="+cardCode+"&encryptPassword="+encryptPassword+"M683DF7035478562";

        if(MD5Util.MD5(m).equals(MD5)){
            try {
                String password = PWUtil.pwutil(encryptPassword,strKey);
                String balance = phoneMoileActivateService.getBalance(cardCode,password);
                return balance;
            } catch (Exception e) {
                return "400";
            }
        }
        return "400";
    }*/


//      密碼修改
/*    @RequestMapping(value = "/changePassword")
    @ResponseBody
    public String changePassword(String cardCode,String encryptOldPassword,String encryptNewPassword,String MD5){

        String m = "cardCode="+cardCode+"&encryptOldPassword="+encryptOldPassword+"&encryptNewPassword="+encryptNewPassword+"M683DF7035478562";
        if(MD5Util.MD5(m).equals(MD5)){
            try {
                String oldPassword = PWUtil.decrypt(encryptOldPassword,strKey);
                String newPassword = PWUtil.decrypt(encryptNewPassword,strKey);
                if(phoneMoileActivateService.changePassword(cardCode,oldPassword,newPassword)) {
                    return "200";
                }
            } catch (Exception e) {
                return "400";
            }
        }
        return "400";
//        return "200";
    }*/


    //    余额查询
    @RequestMapping(value = "/getBalance")
    @ResponseBody
    public String getBalance(String cardCode,String password,String MD5){
        try {
            logger.debug("getBalance begin.........");
            logger.debug("RequstParam:cardCode = {}", cardCode);
            logger.debug("RequstParam:MD5 = {}", MD5);
            String m = "cardCode=" + cardCode + "&password=" + password + "M683DF7035478562";

        if(MD5Util32LowerCase.encryption(m).equals(MD5)){
            String balance = phoneMoileActivateService.getBalance(cardCode, password);
            return balance;
        }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return "400";
        }
        return "400";
    }


    //      密碼修改
    @RequestMapping(value = "/changePassword")
    @ResponseBody
    public String changePassword(String cardCode,String oldPassword,String newPassword,String MD5){
        try {
            if(newPassword.length()!=6){
                throw new Exception("新密码不是6位数");
            }
            String m = "cardCode="+cardCode+"&oldPassword="+oldPassword+"&newPassword="+newPassword+"M683DF7035478562";
            String rt = "";
            if(MD5Util32LowerCase.encryption(m).equals(MD5)){
                rt = phoneMoileActivateService.changePassword(cardCode,oldPassword,newPassword);
                if("0".equals(rt)) {
                    return "200";
                }
            }
            return "400-"+rt;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return "400";
        }
    }


//    掛失
    @RequestMapping(value = "/reportLoss")
    @ResponseBody
    public String reportLoss(String personCertId,String MD5){
        try {
            String m = "personCertId="+personCertId+"M683DF7035478562";
            if(MD5Util32LowerCase.encryption(m).equals(MD5)){
                if(phoneMoileActivateService.reportLoss(personCertId)){
                    return "200";
                }
            }
            return "400";
        } catch (Exception e){
            logger.debug(e.getMessage());
            return "400";
        }
    }





}


