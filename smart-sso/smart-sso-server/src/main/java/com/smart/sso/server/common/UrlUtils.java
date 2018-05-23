package com.smart.sso.server.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

    public static String getHost(String url) {
        if (url == null || url.trim().equals("")) {
            return "";
        }
        String host = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }

    public static String getRootDomain(String url) {
        String domain = getHost(url);
        String[] parts = domain.split("\\.");
        int len = parts.length;
        if (len > 2) {
            return parts[len - 2] + "." + parts[len - 1];
        }
        return domain;
    }
}
