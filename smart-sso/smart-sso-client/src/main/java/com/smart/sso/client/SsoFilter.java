package com.smart.sso.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.smart.sso.rpc.RpcUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单点登录及Token验证Filter
 *
 * @author Joe
 */
public class SsoFilter extends ClientFilter {

    private static Logger Log = LoggerFactory.getLogger(SsoFilter.class);

    // sso授权回调参数token名称
    public static final String SSO_TOKEN_NAME = "__vt_param__";

    public static final String COOKIE_TOKEN_NAME = "fuxiyun_sso_token";

    private String appCode;
    private UserMng userMng;

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = getLocalToken(request);
        if (token == null) {
            token = getCookie(request, COOKIE_TOKEN_NAME);
            if (token != null) {
                if (authenticationRpcService.validate(token)) {
                    RpcUser rpcUser = invokeAuthInfoInSession(request, token);
                    if (userMng != null) {
                        int userId = rpcUser.getUserId();
                        // 如果本地用户不存在，就应该创建用户
                        if (!userMng.existsUser(userId)) {
                            JSONObject userData = getUser(rpcUser.getUserId());
                            userMng.syncAddUserFromSSO(userId, userData);
                        }
                    }
                    return true;
                }
            } else {
                token = request.getParameter(SSO_TOKEN_NAME);
                if (token != null) {
                    invokeAuthInfoInSession(request, token);
                    // 再跳转一次当前URL，以便去掉URL中token参数
                    String url = deleteCodeFromUrl(request.getRequestURL().toString());
                    response.sendRedirect(url);
                    return false;
                }
            }
        } else if (authenticationRpcService.validate(token)) {// 验证token是否有效
            return true;
        }
        redirectLogin(request, response);
        return false;
    }

    private JSONObject getUser(int userId) {
        String host = ssoServerUrl;
        String path = "/api/getUser";
        String method = "GET";
        String appcode = appCode;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("userId", String.valueOf(userId));
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            if (result != null) {
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 去除url中的code参数
     *
     * @param url
     * @return
     */
    private String deleteCodeFromUrl(String url) {
        return url.replaceAll("(&token=([\u4e00-\u9fa5\uf900-\ufa2d0-9\\w]+))|(token=([\u4e00-\u9fa5\uf900-\ufa2d0-9\\w]+)&)", "");
    }

    /**
     * 按名称获取cookie
     *
     * @param request
     * @param name
     * @return
     */
    private static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || name == null || name.isEmpty()) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 获取Session中token
     *
     * @param request
     * @return
     */
    private String getLocalToken(HttpServletRequest request) {
        SessionUser sessionUser = SessionUtils.getSessionUser(request);
        return sessionUser == null ? null : sessionUser.getToken();
    }


    /**
     * 存储sessionUser
     *
     * @param request
     * @return
     * @throws IOException
     */
    private RpcUser invokeAuthInfoInSession(HttpServletRequest request, String token) throws IOException {
        RpcUser rpcUser = authenticationRpcService.findAuthInfo(token);
        if (rpcUser != null) {
            SessionUtils.setSessionUser(request, new SessionUser(token, rpcUser.getAccount()));
        }
        return rpcUser;
    }

    /**
     * 跳转登录
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void redirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isAjaxRequest(request)) {
            responseJson(response, SsoResultCode.SSO_TOKEN_ERROR, "未登录或已超时");
        } else {
            SessionUtils.invalidate(request);

            StringBuffer backUrl = request.getRequestURL().append((request.getQueryString() != null) ? "?" + request.getQueryString() : "");
            String ssoLoginUrl = new StringBuilder().append(isServer ? request.getContextPath() : ssoServerUrl)
                    .append("/login?backUrl=").append(backUrl).toString();
            response.sendRedirect(ssoLoginUrl);
        }
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public UserMng getUserMng() {
        return userMng;
    }

    public void setUserMng(UserMng userMng) {
        this.userMng = userMng;
    }
}