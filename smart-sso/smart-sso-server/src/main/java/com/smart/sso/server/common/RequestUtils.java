package com.smart.sso.server.common;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtils {

    /**
     * 用户KEY
     */
    public static final String USER_KEY = "_user_key";

    /**
     * 判断是否微信浏览器
     *
     * @param request
     * @return
     */
    public static boolean isFromWeiXinBrowser(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        if (ua.contains("MicroMessenger"))
            return true;
        else
            return false;
    }

    public static String getRootDomain(HttpServletRequest request) {
        String domain = request.getServerName();
        String[] parts = domain.split("\\.");
        int len = parts.length;
        if (len > 2) {
            return parts[len - 2] + "." + parts[len - 1];
        }
        return domain;
    }

    public static boolean inSameRootDomain(HttpServletRequest request, String url) {
        return getRootDomain(request).equals(UrlUtils.getRootDomain(url));
    }

    /**
     * 获取请求访问的完整地址
     *
     * @param request
     * @return
     */
    public static String getRequestFullUrl(HttpServletRequest request) {
        String fullUrl = request.getRequestURL().toString();
        if (request.getQueryString() != null) {
            fullUrl += "?" + (request.getQueryString()); // 参数
        }
        return fullUrl;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
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
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getSiteContextPath(HttpServletRequest request) {
        return getServerUrl(request) + request.getContextPath();
    }

    public static String getServerUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName()
                + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
    }

    public static String buildFullUrl(String siteContextPath, String path) {
        String fullUrl = siteContextPath + path;
        return fullUrl;
    }

    public static String buildFullUrl(HttpServletRequest request, String path) {
        String fullUrl = getSiteContextPath(request) + path;
        return fullUrl;
    }

    private static ServletContext servletContext;

    public static ServletContext getServletContext() {
        if (servletContext == null) {
            WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
            if (webApplicationContext != null) {
                ServletContext servletContext = webApplicationContext.getServletContext();
                return servletContext;
            } else {
                return null;
            }
        } else {
            return servletContext;
        }
    }
}
