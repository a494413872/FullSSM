package com.lvmama.filter;

import com.lvmama.net.util.NetUtils;
import com.lvmama.pub.DistributedContext;
import com.lvmama.log.util.LogTrackContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ThreadFilter implements Filter {
    private String[] excludeUrls;
    static Logger LOG = LoggerFactory.getLogger(ThreadFilter.class);

    public ThreadFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        String uri = request.getRequestURI().toLowerCase();
        String[] arr$ = this.excludeUrls;
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String str = arr$[i$];
            if (uri.toLowerCase().matches(str.toLowerCase().trim())) {
                chain.doFilter(req, resp);
                return;
            }
        }

        try {
            String trackNumber = req.getParameter("trackNumber");
            String distributedContextStr;
            if (trackNumber == null) {
                if (trackNumber == null) {
                    LogTrackContext.initTrackNumber();
                }
            } else {
                LogTrackContext.setTrackNumber(trackNumber);
                distributedContextStr = req.getParameter("parentAppName");
                LogTrackContext.setParentAppName(distributedContextStr);
            }

            distributedContextStr = req.getParameter("DistributedContext");
            if (distributedContextStr != null && !"".equals(distributedContextStr)) {
                DistributedContext.putToContext(DistributedContext.toObject(distributedContextStr));
            }

            Object webIPObj = DistributedContext.getContext().get("webIP");
            Object broserIPObj = DistributedContext.getContext().get("broserIP");
            if (webIPObj != null && broserIPObj != null) {
                LOG.info("Broser IP=" + broserIPObj + ", Web ip=" + webIPObj);
            } else {
                String broserIP = getRemoteIpAddress(request);
                String webIP = NetUtils.getLocalIP();
                LOG.info("Broser IP=" + broserIP + ", Web ip=" + webIP);
                DistributedContext.put("broserIP", broserIP);
                DistributedContext.put("webIP", webIP);
            }

            LOG.info(request.getRequestURL() + " ------------------ start");
            chain.doFilter(req, resp);
        } finally {
            LOG.info(request.getRequestURL() + " ------------------ end");
            LogTrackContext.remove();
            DistributedContext.remove();
        }

    }

    public void init(FilterConfig config) throws ServletException {
        this.excludeUrls = ".*/msg/initMessage.do,.*/msg/.*,.*/remoting/.*,.*\\.js,.*\\.css,.*\\.gif,.*\\.jpg,.*\\.jpeg,.*\\.jpe,.*\\.jfif,.*\\.bmp,.*\\.tif,.*\\.png,.*\\.ico".split(",");
    }

    public static String getRemoteIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        ip = transferIP(ip);
        return ip;
    }

    private static String transferIP(String ip) {
        if (ip != null && (ip.equals("127.0.0.1") || ip.startsWith("0:0:0:0") || ip.equals("localhost"))) {
            ip = NetUtils.getLocalIP();
        }

        return ip;
    }
}
