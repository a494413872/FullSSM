package com.lvmama.scenic.comm.utils.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);
	/**
	 * 获取异常详细信息
	 * @param e
	 * @return
	 */
	public static String getExceptionDetails(Throwable e){   
		StringWriter sw = new StringWriter();   
		PrintWriter pw = new PrintWriter(sw, true);   
		e.printStackTrace(pw);   
		pw.flush();   
		sw.flush(); 
		String msg = sw.toString(); 
		pw.close();
		try {
			sw.close();
		} catch (IOException exception) {
			logger.error("StringWriter关闭异常：" + exception.getMessage());
		}
		return msg;
	} 
	
	/**
	 * 获取异常详细信息
	 * @param e
	 * @return
	 */
	public static String getExceptionDetails(Throwable e, int length){   
		String exceptoinMsg = getExceptionDetails(e);
		if(exceptoinMsg != null && exceptoinMsg.length() > length) {
			return exceptoinMsg.substring(0, length);
		}
		return exceptoinMsg;
	} 
	
//	public static void main(String[] args) {
//		List<String> list = new ArrayList<String>();
//		try {
//			list.get(0);
//		} catch(Exception e) {
//			System.out.println(getExceptionDetails(e));
//		}
//	}
}
