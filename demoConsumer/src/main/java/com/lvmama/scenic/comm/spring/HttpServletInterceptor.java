/**
 * 
 */
package com.lvmama.scenic.comm.spring;

import com.lvmama.scenic.comm.utils.HttpServletLocalThread;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *
 */
public class HttpServletInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpServletLocalThread.set(request, response);
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HttpServletLocalThread.clean();
		super.afterCompletion(request, response, handler, ex);
	}

	
}
