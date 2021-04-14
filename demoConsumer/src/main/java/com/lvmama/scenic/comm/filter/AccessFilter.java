/**
 * 登陆验证跳转
 * @author william liu
 * @modify courage,bing
 */
package com.lvmama.scenic.comm.filter;

import com.lvmama.user.UserUser;

import com.lvmama.scenic.comm.vo.Constant;
import com.lvmama.scenic.comm.web.ServletUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

public class AccessFilter implements Filter {

	//用户管理
    private static final Logger log = LoggerFactory.getLogger(AccessFilter.class);
	private static String casLoginUrl;
	private static String[] urls;
	private static String[] excludedUrls; //排除在外的url

	public void destroy() {
	}
	
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		HttpServletResponse res=(HttpServletResponse) arg1;
		
		//add the code to tracking login issue
		if(req.getParameter("cookie_test") != null) {
			log.info("request url: " + req.getRequestURL().toString());
			
			Cookie[] cookies = req.getCookies();
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					log.info("cookie test, name: " + cookie.getName() + ", value: " + cookie.getValue());
				}
			}
			
			Enumeration<String> enums = req.getHeaderNames();
			while(enums.hasMoreElements()) {
				String headerName = enums.nextElement();
				log.info("header info, key: " + headerName + ", value: " + req.getHeader(headerName));
			}
		}
		
		ServletUtil.initLvSessionId(req, res);
		String path = req.getRequestURI();
		if(log.isDebugEnabled()){
			log.debug("filter::::::::::::::"+path);
		}
		if (isInCheck(path,req.getContextPath())) {
			if (!isLogon(req, res)) {
				log.info(" AccessFilter getRequestURL " + req.getRequestURL());
				
				String reqUrl = req.getRequestURL().toString();
				@SuppressWarnings("unchecked")
				Map<String, String[]> paras = req.getParameterMap();
				if (paras != null) {
					Set<String> set = paras.keySet();
					int i=0;
					for (String key : set) {
						if (!key.equalsIgnoreCase("ticket")) {
							if (i==0) {
								reqUrl = reqUrl + "?" + key + "=" + paras.get(key)[0];
							}else{
								reqUrl = reqUrl + "&" + key + "=" + paras.get(key)[0];
							}
							i++;
						}
					}
				}
				log.info(" AccessFilter reqUrl " + reqUrl);
				
				res.sendRedirect(casLoginUrl+"?service="+URLEncoder.encode(reqUrl, "UTF-8"));
				return;
			}
		}
		
		//do not cache cookie
		res.addHeader("Cache-Control", "no-cache=Set-Cookie");
		
		long beginTime = System.currentTimeMillis();
		try {
			arg2.doFilter(arg0, arg1);
		} catch (Exception e) {
			try {
				// 现在偶尔会出现从request中取不到Cookie和Parameter的问题，该问题会导致重新创建lvsessionid，
				// 并且会覆盖客户端浏览器Cookie中的lvsessionid，导致登录退出
				// 现临时解决办法：当程序报错时，清空response(主要是response中的Set-Cookie)
				if(res.containsHeader("Set-Cookie")) {
					res.reset();
				}
			} catch (Exception e1) {
				log.error("response reset got failed!", e1);
			}
			throw e;
		}
		long count = System.currentTimeMillis() - beginTime;
		if (log.isDebugEnabled()) {
			log.debug(req.getRequestURI()+" spent " + count + " ms.");
		}
		
	}

	private boolean isLogon(HttpServletRequest req, HttpServletResponse resp) {
		UserUser userUser = (UserUser) ServletUtil.getSession(req, resp, Constant.SESSION_FRONT_USER);
		if(log.isDebugEnabled()){
			log.info("userUser " + userUser);
		}
		if (userUser == null || (userUser != null && userUser.getUserId() == null)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 解析urlrewrite path.
	 * @param path.
	 * @return String 解析后的url.
	 */
	private boolean isInCheck(final String uri,final String contextPath) {
		String path=uri.replaceFirst(contextPath, "");
		if(checkUrl(path) && !isExcludedUrlMatched(path)){
			if (urls == null)
				return false;
			if (urls.length == 0)
				return false;
//			if(path.contains(".do")){
			for (int i = 0; i < urls.length; i++) {
				if(pathMatcher.match(urls[i], path)){
					return true;
				}
			}
//				return false;
//			}
			
		}
		return false;
	}
	
	/**
	 * 当前请求路径是否能与排除URL中某一个进行匹配
	 * @param path
	 * @return
	 */
	private boolean isExcludedUrlMatched(String path) {
		if(excludedUrls == null || excludedUrls.length <= 0) {
			return false;
		}
		
		for(String excluedUrl : excludedUrls) {
			if(pathMatcher.match(excluedUrl, path)) {
				return true;
			}
		}
		
		return false;
	}
	/**
	 * 以下后缀的不需要过滤 
	 * @param str
	 * @return
	 */
	private boolean checkUrl(String str){
		String[] s = new String[]{".jpg",".js",".gif",".css"};
		for (int i = 0; i < s.length; i++) {
			if(str.toLowerCase().lastIndexOf(s[i])>0){
				return false;
			}
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		casLoginUrl=arg0.getInitParameter("casLoginUrl");
		String temp = arg0.getInitParameter("url");
		String excludedUrlParam = arg0.getInitParameter("excludedUrl");
		if (log.isDebugEnabled())
			log.debug("get init parameter");
		if (temp != null) {
			urls = temp.split(",");
		} 
		if(StringUtils.isNotBlank(excludedUrlParam)) {
			excludedUrls = excludedUrlParam.split(",");
		
			for(int i = 0; i < excludedUrls.length; i++) {
				excludedUrls[i] = excludedUrls[i].trim();
			}
		}
	}
	
	private PathMatcher pathMatcher = new AntPathMatcher();
}
