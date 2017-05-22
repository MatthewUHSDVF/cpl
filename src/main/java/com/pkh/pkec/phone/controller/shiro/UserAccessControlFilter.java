package com.pkh.pkec.phone.controller.shiro;

import com.pkh.pkec.phone.service.UserService;
import com.pkh.pkcore.exception.CustomException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAccessControlFilter extends
		org.apache.shiro.web.filter.AccessControlFilter {

	private Logger logger = LoggerFactory.getLogger(UserAccessControlFilter.class);

//	@Autowired
//	private CardService cardService;

	@Autowired
	private UserService userService;
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		logger.info("uri:{}",httpServletRequest.getRequestURI());
		logger.info("sid:", SecurityUtils.getSubject().getSession().getId());
		// init
		Subject subject = getSubject(request, response);
		if (subject == null) {
			HttpServletResponse res = WebUtils.toHttp(response);
			res.sendError(401);
			return false;
		}

/*		// preprocess
		if (subject.isAuthenticated()){
			logger.debug("subject.isAuthenticated() = true");
			return true;
		}*/
		
		if (subject.isAuthenticated()||subject.isRemembered()){
			logger.debug("subject.isRemembered() = true");
			Session session = subject.getSession();
			Integer userId = (Integer) session.getAttribute("userId");
			if (userId == null || "".equals(userId)) {
				logger.debug("session已经失效，开始重新装入相关信息");
				String userCode = (String) subject.getPrincipal();
			}
			return true;
		}
		// process
		try {
			HttpServletResponse res = WebUtils.toHttp(response);
			res.sendError(401);
			return false;
		} catch (CustomException ce) {
			subject.logout();
			logger.error(ce.getMessage());
		} catch (Exception e) {
			subject.logout();
			e.printStackTrace();
		}
		// return
		return true;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		return true;
	}



}
