/**
 *
 */
package com.lvmama.scenic.comm.web;

import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * @author pengyayun
 *
 */
public class TokenHandler {

    private static Logger logger = Logger.getLogger(TokenHandler.class);

    /**
     *
     * @param session
     * @return
     */
    public synchronized static String getGUID(ServletRequest servletRequest,ServletResponse servletResponse){
        String token = "";
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            Object obj =  ServletUtil.getSession(request, response, "SPRINGMVC.TOKEN");
            logger.info("sessionId = "+ ServletUtil.getLvSessionId(request, response)+"     time = "+System.currentTimeMillis());
            if(obj!=null){
                token = (String)obj;
            }else{
//	        	   new BigInteger(165, new Random()).toString(36)
//                   .toUpperCase()
                token = UUID.randomUUID().toString();
                ServletUtil.putSession(request, response, "SPRINGMVC.TOKEN", token);
            }
        }catch (IllegalStateException e) {
            logger.error("generateGUID() mothod find bug,by token session...");
        }
        return token;
    }

    /**
     *
     * @param request
     * @return
     */
    public static boolean validToken(HttpServletRequest request,HttpServletResponse response){
        String inputToken = getInputToken(request);
        if (inputToken == null) {
            logger.error("token is not valid!inputToken is NULL");
            return false;
        }
        Object obj =  ServletUtil.getSession(request, response, "SPRINGMVC.TOKEN");
        if (obj == null) {
            logger.error("sessionId = "+ ServletUtil.getLvSessionId(request, response)+"     time = "+System.currentTimeMillis());
            logger.error("Got a null or empty token name.");
            return false;
        }
        String sessionToken=String.valueOf(obj);
        if (!inputToken.equals(sessionToken)) {
            logger.error("token is not valid!inputToken='" + inputToken
                    + "',sessionToken = '" + sessionToken + "'");
            return false;
        }
        ServletUtil.removeSession(request, response, "SPRINGMVC.TOKEN");
        return true;
    }

    public static String getInputToken(HttpServletRequest request) {
		/*String tokenName=request.getParameter("tokenName");
        if (StringUtils.isEmpty(tokenName)) {
            logger.warn("Could not find token name in params.");
            return null;
        }
        return tokenName;*/
        Map params = request.getParameterMap();
        if (!params.containsKey("tokenName")) {
            logger.error("Could not find token name in params.");
            return null;
        }
        String[] tokens = (String[]) (String[]) params
                .get("tokenName");
        if ((tokens == null) || (tokens.length < 1)) {
            logger.error("Got a null or empty token name.");
            return null;
        }
        return tokens[0];
    }
}
