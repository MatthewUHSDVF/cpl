package com.pkh.pkec.phone.controller.shiro;

import com.pkh.pkec.phone.service.UserService;
import com.pkh.pkcore.exception.CustomException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserShiroRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger(UserShiroRealm.class);


	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.debug("doGetAuthorizationInfo(),Pricipals="+principals.toString());

		SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
		simpleAuthorInfo.addRoles(null);
		simpleAuthorInfo.addStringPermissions(null);
		return simpleAuthorInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		logger.debug("doGetAuthentication(),authcToken="+authcToken.toString());
		try{
			// 检查账户(cardCode)与密码
			String userName = (String) authcToken.getPrincipal();
//			String userPassword =  (String )authcToken.getCredentials();
			if (userName == null || "".equals(userName)){
				logger.error("用户账户不能为空");
				throw new AuthenticationException("用户账户不能为空");
			}
			String userPassword = new String((char[]) authcToken.getCredentials());
/*			在login之前做验证
			cardService.checkPassword(userCode,userPassword);*/
			// person
//			InsuCardCustom insuCardCustom = cardService.getCardInfoByLoginId(userCode);
//			PKECUser pkecUser = userService.getUserByPersonCode(insuCardCustom.getCardPersoncode());
//			SubjectUtil.setSession("cardCode", userCode);
//			SubjectUtil.setSession("userId", pkecUser.getUserId());
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, userPassword,"");
			return authenticationInfo;
		}catch(CustomException ce){
			logger.error(ce.getErrorMessage());
			throw new CustomException(ce.getErrorCode(),ce.getErrorMessage());
		}catch(AuthenticationException ae){
			logger.error(ae.getMessage());
			throw ae;
		}catch(Exception e){
			e.printStackTrace();
			throw new AuthenticationException("认证过程中发生未知错误");
		}
	}



	@Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}