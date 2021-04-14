package com.lvmama.scenic.comm.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lvmama.scenic.comm.utils.ExceptionFormatUtil;
import com.lvmama.scenic.comm.utils.MemcachedUtil;
import com.lvmama.scenic.comm.utils.StringUtil;
import org.apache.commons.lang.StringUtils;


import com.alibaba.fastjson.JSON;
import com.lvmama.scenic.comm.vo.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletUtil {

    private static Logger log = LoggerFactory.getLogger(ServletUtil.class);

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(name)) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie cookie = ServletUtil.getCookie(request, cookieName);
        return (cookie == null ? null : cookie.getValue());
    }

    /**
     * 添加指定有效期的COOKIE
     * @param res
     * @param key
     * @param value
     * @param validDays
     */
    public static void addCookie(HttpServletResponse res, String key, String value, int validDays) {
        addCookie(res, key, value, false);
    }
    public static void addCookie(HttpServletResponse res, String key, String value, int validDays,boolean use_lvmama_host) {
        Cookie cookie = new Cookie(key,value);
        if(use_lvmama_host){
            cookie.setDomain(".lvmama.com");
        }
        cookie.setMaxAge(validDays*3600*24);
        cookie.setPath("/");
        res.addCookie(cookie);
    }

    public static void addCookies(HttpServletResponse res, String key, String value, boolean use_lvmama_host) {
        Cookie cookie = new Cookie(key,value);
        if(use_lvmama_host){
            cookie.setDomain(".lvmama.com");
        }
        cookie.setMaxAge(30*60);
        cookie.setPath("/");
        res.addCookie(cookie);
    }

    public static void clearCookie(HttpServletRequest request,HttpServletResponse response,String name) {
        Cookie[] cookies = request.getCookies();
        try {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(name)) {
                    Cookie cookie = new Cookie(cookies[i].getName(), cookies[i].getValue());
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        } catch (Exception ex) {
            logWrite(request,name,"删除Cookies发生异常！" );
            log.error("清除cookies异常",ex);
        }
    }

    /**
     * 添加一个生命周期是当前浏览器的COOKIE
     * @param res
     * @param key
     * @param value
     */
    public static void addCookie(HttpServletResponse res, String key, String value) {
        addCookie(res, key, value,false);
    }

    public static void addCookie(HttpServletResponse res, String key, String value,boolean use_lvmama_host) {
        Cookie cookie = new Cookie(key,value);
        if(use_lvmama_host){
            cookie.setDomain(".lvmama.com");
        }
        cookie.setPath("/");
        res.addCookie(cookie);
    }

    public static String getRequestHost(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if (host==null) {
            return Constant.DEFAULT_LOCATION;
        }if (host.indexOf(".")!=-1) {
            return host.substring(0,host.indexOf("."));
        }else{
            return host;
        }
    }

    public static String getRequestUrlWithDomain(HttpServletRequest request, String uri, String domain) {
        int port = request.getLocalPort();
        if (port==80) {
            return "http://" + domain + "/" + uri;
        }else{
            return "http://" + domain + ":" + port+"/" + uri;
        }
    }

    public static String getRequestUrlWithDomain(HttpServletRequest request, String domain) {
        String url = request.getRequestURL().toString();
        int port = request.getLocalPort();
        if (port==80) {
            return "http://" + domain + url.substring(url.indexOf("/", 8));
        }else{
            return "http://" + domain + ":" + port+"/" + url.substring(url.indexOf("/", 8));
        }
    }

    public static String getRefererUrlWithDomain(HttpServletRequest request, String domain,boolean isStationDomain) {
        String url = request.getHeader("Referer");
        if (url==null || !isStationDomain) {
            return "http://" + domain;
        }else{
            return "http://" + domain + url.substring(url.indexOf("/", 8));
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getSessionMap(String lvsessionid) {
        Map<String, Object> map = null;
        if ( lvsessionid!=null ) {
            map = (Map<String, Object>) MemcachedUtil.getInstance().get(lvsessionid,true);
        }
        boolean result = false;
        if (map!=null) {
            result = true;
        }
        if(log.isDebugEnabled()){
            //log.info("get object from memcached, session:" + lvsessionid+ " result: " + result);
        }
        if (map==null) {
            log.info("getSessionMap get session from memcached is null, SessionID: " + lvsessionid);
            map = new HashMap<String, Object>();
            map.put("LAST_UPDATE_TIME", System.currentTimeMillis());
        }
        if (map.get("ver")==null) {
            log.info("getSessionMap get session ver from memcached is null, SessionID: " + lvsessionid);
            map.put("ver", 0);
        }
        return map;
    }

    /**
     * 把对象放置到Session
     * @param request
     * @param key
     * @param obj
     */
    public static void putSession(HttpServletRequest request,HttpServletResponse response,String key, Object obj) {
        String lvsessionId = getLvSessionId(request, response);
//		String lvsessionId = request.getSession().getId();
        Map<String, Object> map = getSessionMap(lvsessionId);
        map.put("LAST_UPDATE_TIME", System.currentTimeMillis());
        map.put(key, obj);
        boolean result = MemcachedUtil.getInstance().set(lvsessionId, 1800, map,true);
        if(!result) {
            log.info("putSession fail, lvsessionId=" + lvsessionId);
        }
        if(log.isDebugEnabled()){
            //log.info("put object to memcached, session:" + lvsessionId+ " result: " + result);
        }
    }

    /**
     * 从Session中取对象
     * @param request
     * @param key
     * @return
     */
    public static Object getSession(HttpServletRequest request, HttpServletResponse response, String key) {
        try{
            String lvsessionId = getLvSessionId(request, response);
//			String lvsessionId = request.getSession().getId();
            Map<String, Object> map = getSessionMap(lvsessionId);

            Map<String, Object> map1 = null;
            if ( lvsessionId!=null ) {
                map1 = (Map<String, Object>)MemcachedUtil.getInstance().get(lvsessionId,true);
                if(map1 == null){
                    map1 =new HashMap<String,Object>();
                    map1.put("lvsessionId", lvsessionId);
                    logWrite(request, "Get Memcached Map is null By lvsessionId", JSON.toJSONString(map1));
                }
            }else{
                logWrite(request, "lvsessionId is null", lvsessionId);
            }

            Object obj = map.get("LAST_UPDATE_TIME");
            if (obj!=null) {
                long updateTime = Long.parseLong(obj.toString());
                long period = System.currentTimeMillis()-updateTime;
                if (period>300000) {
                    int ver = Integer.parseInt(map.get("ver").toString());
                    map.put("ver", ++ver);
                    MemcachedUtil.getInstance().replace(lvsessionId, 1800, map,true);
                }
            }
            return map.get(key);
        }catch(Exception e) {
            log.error(ExceptionFormatUtil.getTrace(e));
        }
        return null;
    }


    /**
     * 从Session中删除对象
     * @param request
     * @param key
     */
    public static void removeSession(HttpServletRequest request,HttpServletResponse response, String key) {
        String lvsessionId = getLvSessionId(request, response);
        Map<String, Object> map = getSessionMap(lvsessionId);
        Object obj = map.get(key);
        if(log.isDebugEnabled()){
            log.debug("REMOVE SESSION : key: " + key + " value : " + obj + " from " + lvsessionId);
        }
        log.info("REMOVE SESSION : key: " + key + " value : " + obj + " from " + lvsessionId);
        map.remove(key);
        int ver = Integer.parseInt(map.get("ver").toString());
        map.put("ver", ++ver);
        MemcachedUtil.getInstance().replace(lvsessionId, 1800, map,true);
    }



    public static String getLvSessionId(HttpServletRequest request, HttpServletResponse response) {

        if(request.getAttribute(Constant.LV_SESSION_ID)!=null){
            String lvSessionId = request.getAttribute(Constant.LV_SESSION_ID).toString();
            if(StringUtils.isBlank(lvSessionId)) {
                log.info("getLvSessionId get sessionId from attribute of request is null, lvSessionId=" + lvSessionId);
            }
            return lvSessionId;
        }else {
            String lvSessionId = getCookieValue(request, Constant.LV_SESSION_ID);
            if(StringUtils.isBlank(lvSessionId)) {
                log.info("getLvSessionId get sessionId from cookie of request is null, lvSessionId=" + lvSessionId);
            }
            return lvSessionId;
        }
    }

    /**
     * 初始化LvSessionId
     * @param request
     * @param response
     */
    public static void initLvSessionId(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie =ServletUtil.getCookie(request, Constant.LV_SESSION_ID);
        String lvsessionId = cookie==null?null:cookie.getValue();
        if (lvsessionId==null) {
            if(!StringUtil.isEmptyString(request.getParameter(Constant.LV_SESSION_ID))){
                lvsessionId = request.getParameter(Constant.LV_SESSION_ID);
            }else{
                lvsessionId = UUID.randomUUID().toString();
            }
            logWrite(request, "initLvSessionId new lvsessionId", lvsessionId);

            String host=request.getServerName();
            boolean use_lvmama_host=host.contains(".lvmama.com");
            addCookie(response, Constant.LV_SESSION_ID, lvsessionId,use_lvmama_host);
            request.setAttribute(Constant.LV_SESSION_ID, lvsessionId);
        }
    }

    private static void logWrite(HttpServletRequest request, Object key, Object value){
        log.info("\n[user] IP:" + request.getRemoteAddr() +
                "\nURL:" + request.getRequestURL().toString() +
                "\n" + key.toString() + " : " + value.toString());
    }

    public static String getMobileLoginChannel(HttpServletRequest request) {
        String mobileChannel = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("mobileChannel")) {
                    mobileChannel = cookies[i].getValue();
                }
            }
        }
        return mobileChannel;
    }

    public static boolean isMobileLogin(HttpServletRequest request){
        String loginTypeName = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("loginType")) {
                    loginTypeName = cookies[i].getValue();
                }
            }
        }

        if(Constant.LOGIN_TYPE.MOBILE.name().equals(loginTypeName)){
            return true;
        }
        if(Constant.LOGIN_TYPE.MOBILE.name().equals(request.getParameter("loginType"))){
            return true;
        }
        return false;

    }


    public static boolean isMobileDeviceLogin(HttpServletRequest request){
        return isWapLogin(request)||isMobileLogin(request);
    }

    /**
     * 判断是否wap站登录.
     * @param request
     * @return
     */
    public static boolean isWapLogin(HttpServletRequest request){
        String loginTypeName = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("loginType")) {
                    loginTypeName = cookies[i].getValue();
                    break;
                }
            }
        }

        if(Constant.LOGIN_TYPE.HTML5.name().equals(loginTypeName)){
            return true;
        }
        if(Constant.LOGIN_TYPE.HTML5.name().equals(request.getParameter("loginType"))){
            return true;
        }
        return false;

    }
}
