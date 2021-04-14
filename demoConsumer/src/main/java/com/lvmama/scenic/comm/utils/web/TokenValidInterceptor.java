/**
 * 
 */
package com.lvmama.scenic.comm.utils.web;

import com.alibaba.fastjson.JSONObject;
import com.lvmama.scenic.comm.utils.ExceptionFormatUtil;
import com.lvmama.scenic.comm.utils.MD5;
import com.lvmama.scenic.comm.utils.MemcachedUtil;
import com.lvmama.scenic.comm.web.ServletUtil;
import com.lvmama.scenic.comm.web.TokenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author pengyayun
 *
 */
/*@Controller*/
public class TokenValidInterceptor implements HandlerInterceptor {
	
	private static final Logger LOG = LoggerFactory.getLogger(TokenValidInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	/*@RequestMapping(value="/order/",method=RequestMethod.POST)*/
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object object) throws Exception {
		// TODO Auto-generated method stub
		if(!TokenHandler.validToken(request,response)){
			try {
				String md5 = getParamMd5(request);
				Object oldmd5 = ServletUtil.getSession(request, response, "SPRINGMVC.ORDER.FORM");
				//订单没有记录直接生成新订单
				if(oldmd5 == null){
					LOG.info("重复提交，获取原来的Md5失败");
					ServletUtil.putSession(request, response, "SPRINGMVC.ORDER.FORM", md5);
					request.setAttribute("orderRepeatedMd5", ServletUtil.getLvSessionId(request, response)+md5);
					return true;
				}
				//当前订单参数与前一个订单参数不同生成新订单
				if(!oldmd5.equals(md5)){
					LOG.info("重复提交，生成的md5不同，生成新订单");
					//如果订单验证码正确
					if(!request.getParameterMap().containsKey("pic_checkCode")){
						LOG.info("重复提交，订单验证码正确");
						request.setAttribute("orderRepeatedMd5", ServletUtil.getLvSessionId(request, response)+md5);
						ServletUtil.putSession(request, response, "SPRINGMVC.ORDER.FORM", md5);
					}
					return true;
				}
				//当前订单参数与前一个订单参数相同直接跳转
				else{
					LOG.info("重复提交，生成的md5相同，直接跳转");
					ServletUtil.putSession(request, response, "SPRINGMVC.ORDER.FORM", md5);
					Long orderId = MemcachedUtil.getInstance().get(ServletUtil.getLvSessionId(request, response)+md5);
					if(orderId != null){
						String path = request.getContextPath();
						response.sendRedirect(path+"/order/view.do?orderId="+orderId);
					}else{
						request.setAttribute("orderRepeatedMd5", ServletUtil.getLvSessionId(request, response)+md5);
						return true;
					}
					
				}
				
			} catch (NoSuchAlgorithmException e) {
				LOG.error(ExceptionFormatUtil.getTrace(e));
				return false;
			}
//			String path = request.getContextPath();
//			response.sendRedirect(path+"/order/repeatSubmitOrder.do");
            return false;
        }
		else{
			try {
				String md5 = getParamMd5(request);
				ServletUtil.putSession(request, response, "SPRINGMVC.ORDER.FORM", md5);
				request.setAttribute("orderRepeatedMd5", ServletUtil.getLvSessionId(request, response)+md5);
			} catch (NoSuchAlgorithmException e) {
				LOG.error(ExceptionFormatUtil.getTrace(e));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 把请求的参数生成一个md5值
	 * @param request
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@SuppressWarnings("rawtypes")
	private String getParamMd5(HttpServletRequest request) throws NoSuchAlgorithmException{
		Map map = request.getParameterMap();
		JSONObject jsonObject = new JSONObject(map);
		//表单验证码不控制
		jsonObject.remove("checkCode2");
		String jsonString = jsonObject.toString();
		LOG.info("创建订单生成的参数："+jsonString);
		String md5 = MD5.encode(jsonString);
		return md5;
	}
}
