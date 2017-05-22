package com.pkh.pkec.phone.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Created by lujiao on 16/8/3.
 */
public class SubjectUtil {
//  常量声明  

//  静态变量声明  

//  成员变量声明  

//  构造和回收函数声明  

//  public 和 protect成员方法  

    public static void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            //logger.debug("setSession(),Session.getTimeout = " + session.getTimeout() + "毫秒");
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }

    public static Object getSession(Object key){
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            //logger.debug("setSession(),Session.getTimeout = " + session.getTimeout() + "毫秒");
            if (null != session) {
              return  session.getAttribute(key);
            }
        }
        return null;
    }
//  private 成员方法  

//  静态方法   
}
