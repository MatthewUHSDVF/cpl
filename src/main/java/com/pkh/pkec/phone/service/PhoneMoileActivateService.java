package com.pkh.pkec.phone.service;

import com.pkh.pkcore.po.card.InsuCardCustom;
import com.pkh.pkcore.po.insurance.InsuPersonCustom;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/31.
 */
public interface PhoneMoileActivateService {


    String SendCaptcha(String mobile) throws Exception;

    Boolean checkUpCaptcha(String mobile, String captcha);

    Boolean activeCard(String cardCode, String idCard);

    Boolean bindMobile(String mobile, String cardCode);

    String getBalance(String cardCode, String password);

    String changePassword(String cardCode, String oldPassword, String newPassword);

    Boolean reportLoss(String personCertId);

    InsuCardCustom getInsuCardByMobile(String mobile);

    boolean checkPassword(String cardNo, String password);

    Map<String, Object>  getCardAccountAmt(String cardCode);

    InsuPersonCustom selectPersonInfo(String personCode);

}
