<%@ page import="com.lvmama.scenic.comm.utils.MemcachedUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>测试页面</title>
</head>
<body>
<%= new java.util.Date() %>
<br/>
<%
    if(request.getParameter("MemcachedKey") != null){
        String key = request.getParameter("MemcachedKey");
        MemcachedUtil memcachedUtil = MemcachedUtil.getInstance();
        if(request.getParameter("isDelete") != null){
            memcachedUtil.remove(key);
        }
        out.println("MemcachedUtil:" + memcachedUtil.get(key)+"<br>");
//        out.println("Session:" + memcachedUtil.get(key, true)+"<br>");
    }
%>
</body>
</html>