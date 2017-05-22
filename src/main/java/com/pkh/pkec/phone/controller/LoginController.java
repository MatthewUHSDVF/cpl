package com.pkh.pkec.phone.controller;

import com.pkh.pkec.phone.controller.shiro.UserShiroRealm;
import com.pkh.pkec.phone.po.User;
import com.pkh.pkec.phone.service.UserService;
import com.pkh.pkec.phone.util.SubjectUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/checkUser")
    @ResponseBody
    public Map<String, Object> checkUser(User user, HttpSession session){
        Map<String,Object> map = new HashMap<String, Object>();
        String userName = user.getUserName();
        String userPassword = user.getUserPassword();

        if(!userService.checkUser(userName,userPassword)){
            throw new AuthenticationException();
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, userPassword);
        try {
            subject.login(token);
            map.put("errorCode","0");
        }catch (Exception e){
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            UserShiroRealm userRealm = (UserShiroRealm)securityManager.getRealms().iterator().next();
            userRealm.clearCachedAuthorizationInfo(subject.getPreviousPrincipals());
            userRealm.clearCachedAuthenticationInfo(subject.getPreviousPrincipals());
            subject.login(token);
            e.printStackTrace();
            map.put("errorCode","1");
            map.put("error","用户名/密码错误");
        }
        return map;
    }
}
