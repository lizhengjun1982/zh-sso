package com.smart.sso.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.mvc.config.ConfigUtils;
import com.smart.mvc.model.Result;
import com.smart.mvc.util.StringUtils;
import com.smart.sso.server.common.HttpUtils;
import com.smart.sso.server.common.RequestUtils;
import com.smart.sso.server.model.User;
import com.smart.sso.server.service.UserService;
import com.smart.sso.server.weixin.WeiXinUserInfo;
import com.smart.sso.server.weixin.WeixinApiException;
import com.smart.sso.server.weixin.WeixinBackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

//import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Controller
@RequestMapping("/weixin")
public class WeiXinController {

    private static Logger Log = LoggerFactory.getLogger(WeiXinController.class);

    public static final String GRANT_TYPE = "authorization_code";
    /**
     * 微信公众号授权
     */
    public final String SOURCE_WEI_XIN = "weixin";
    /**
     * 微信网页授权
     */
    public final String SOURCE_WEI_XIN_WEB = "weixinweb";

    private static String preCode;
    private static String appId = ConfigUtils.getProperty("weixin.appId");
    private static String appSecret = ConfigUtils.getProperty("weixin.appSecret");
    private static String webAppId = ConfigUtils.getProperty("weixin.web.appId");
    private static String webAppSecret = ConfigUtils.getProperty("weixin.web.appSecret");
    private static String scope = "snsapi_userinfo";
    private static String webScope = "snsapi_login";

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private UserService userService;

    @Autowired
    private LoginController loginController;


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "/login";
    }

    @RequestMapping(value = "/bindAccount", method = RequestMethod.GET)
    public String bindAccount(HttpServletRequest request, HttpServletResponse response,
                              @ModelAttribute("wxOpenId") String wxOpenId, @ModelAttribute("wxOpenId") String wxUnionId, @ModelAttribute("backUrl") String backUrl) {
        request.setAttribute("wxOpenId", wxOpenId);
        request.setAttribute("wxUnionId", wxUnionId);
        request.setAttribute("backUrl", wxUnionId);
        return "bind_account_m";
    }

    /**
     * 微信登录自动创建账号并绑定
     *
     * @param request
     * @param response
     * @param wxOpenId
     * @param wxUnionId
     * @param backUrl
     * @return
     */
    @RequestMapping(value = "/bindAutoCreatedAccount", method = RequestMethod.POST)
    public String bindAutoCreatedAccount(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
                                         @RequestParam("wxOpenId") String wxOpenId, @RequestParam("wxOpenId") String wxUnionId, @RequestParam("backUrl") String backUrl) throws UnsupportedEncodingException {
        String accessToken = (String) httpSession.getAttribute("accessToken");
        WeiXinUserInfo userInfo = getUserInfo(accessToken, wxOpenId);
        if (userInfo != null) {
            User user = new User(userInfo);
            userService.save(user);
            return login(request, response, user, backUrl);
        } else {
            return showError(request, "获取用户信息失败（1001）");
        }
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String auth(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "sessionId", required = false) String sessionId,
                       @RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "state", required = false) String state, RedirectAttributes attributes) throws UnsupportedEncodingException {
        // code说明 ：
        // code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
        if (code == null) {
            return showError(request, "code为null");
        }
        // 用户同意授权
        if (!"authdeny".equals(code)) {
            // 第二步：通过code换取网页授权access_token
            String jsonText = "";
            StringBuffer url = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token?");
            url.append("appid=").append(appId).append("&");
            url.append("secret=").append(appSecret).append("&");
            url.append("code=").append(code).append("&");
            url.append("grant_type=").append(GRANT_TYPE);
            WeixinBackInfo weixinBackInfo = null;
            try {
                System.out.println("get:" + url.toString());
                jsonText = HttpUtils.getStaticString(url.toString());
                System.out.println("response:" + jsonText);
                weixinBackInfo = mapper.readValue(jsonText, WeixinBackInfo.class);
            } catch (IOException e) {
                Log.error(e.getMessage(), e);
                return showError(request, e.getMessage());
            }
            if (weixinBackInfo != null) {
                if (weixinBackInfo.getErrcode() != null) {
                    return showError(request, weixinBackInfo.getErrcode() + ":" + weixinBackInfo.getErrmsg());
                }
                String backUrl = URLDecoder.decode(state, "utf-8");
                String openId = weixinBackInfo.getOpenid();
                User user = userService.findByWeiXinOpenId(openId);
                // 如果该微信已绑定用户账号，便以所绑定的账号登录系统
                if (user != null) {
                    return login(request, response, user, backUrl);
                } else {
                    attributes.addAttribute("wxOpenId", openId);
                    attributes.addAttribute("wxUnionId", weixinBackInfo.getUnionid());
                    attributes.addAttribute("backUrl", backUrl);
                    request.getSession().setAttribute("accessToken", weixinBackInfo.getAccess_token());
                    return "redirect:/weixin/bindAccount";
                }
            } else {
                request.setAttribute("message", "code为null");
            }
            String returnUrl = null;
            preCode = code;
            return "redirect:" + returnUrl;
        } else {
            return "error/auth_error";
        }
    }

    private String showError(HttpServletRequest request, String message) {
        request.setAttribute("message", message);
        return "auth_error";
    }

    @RequestMapping(value = "/web/auth", method = RequestMethod.GET)
    public String webAuth(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "sessionId", required = false) String sessionId,
                          @RequestParam(value = "code", required = false) String code,
                          @RequestParam(value = "state", required = false) String state) throws UnsupportedEncodingException {
        // code说明 ：
        // code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
        if (code == null) {
            Log.warn("code为null");
            return "auth_error";
        }
        // 用户同意授权
        if (!"authdeny".equals(code)) {
            // 第二步：通过code换取网页授权access_token
            String jsonText = "";
            StringBuffer url = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token?");
            url.append("appid=").append(webAppId).append("&");
            url.append("secret=").append(webAppSecret).append("&");
            url.append("code=").append(code).append("&");
            url.append("grant_type=").append(GRANT_TYPE);
            WeixinBackInfo weixinBackInfo = null;
            try {
                jsonText = HttpUtils.getStaticString(url.toString());
                weixinBackInfo = mapper.readValue(jsonText, WeixinBackInfo.class);
            } catch (IOException e) {
                Log.error(e.getMessage(), e);
                return "error/auth_error";
            }
            if (weixinBackInfo != null) {
                if (weixinBackInfo.getErrcode() != null) {
                    Log.error(weixinBackInfo.getErrcode() + ":" + weixinBackInfo.getErrmsg());
                    return "error/auth_error";
                }
                String backUrl = URLDecoder.decode(state, "utf-8");
                String openId = weixinBackInfo.getOpenid();
                User user = userService.findByWeiXinWebOpenId(weixinBackInfo.getOpenid());
                // 如果该微信已绑定用户账号，便以所绑定的账号登录系统
                if (user != null) {
                    return login(request, response, user, backUrl);
                } else {
                    request.setAttribute("wxWebOpenId", openId);
                    request.setAttribute("wxUnionId", weixinBackInfo.getUnionid());
                    return "bind_account";
                }
            }
            String returnUrl = null;
            preCode = code;
            return "redirect:" + returnUrl;
        } else {
            return "error/auth_error";
        }
    }

    private String login(HttpServletRequest request, HttpServletResponse response, User user, String backUrl) throws UnsupportedEncodingException {
        Result result = userService.login(RequestUtils.getIpAddr(request), user);
        return loginController.login(request, response, result, backUrl);
    }

    private WeiXinUserInfo getUserInfo(String accessToken, String openId) {
        StringBuffer url = new StringBuffer("https://api.weixin.qq.com/sns/userinfo?");
        String jsonText = "";
        url.append("access_token=").append(accessToken).append("&");
        url.append("openid=").append(openId).append("&");
        url.append("lang=zh_CN");
        WeiXinUserInfo userInfo = null;
        try {
            jsonText = HttpUtils.getStaticString(url.toString());
            if (!jsonText.contains("\"errcode\"")) {
                userInfo = mapper.readValue(jsonText, WeiXinUserInfo.class);
            } else {
                throw new WeixinApiException(url.toString(), jsonText);
            }
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
        return userInfo;
    }

    private void login(String accessToken, String openId, String unionid, String sessionId, HttpServletRequest request,
                       HttpServletResponse response, String source) {
        if (StringUtils.isNotBlank(openId)) {
            // 先拉取用户信息
            WeiXinUserInfo userInfo = getUserInfo(accessToken, openId);
        }
    }

    public static String getWeiXinAuthorizeURL(HttpServletRequest request, String backUrl) {
        String authorizeURL = null;
        try {
            if (RequestUtils.isFromWeiXinBrowser(request)) {
                String redirectUri = RequestUtils.buildFullUrl(request, "/weixin/auth");
                // 使用urlencode对链接进行处理
                redirectUri = URLEncoder.encode(redirectUri, "utf-8");
                String state = URLEncoder.encode(backUrl, "utf-8");
                authorizeURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                        + redirectUri + "&response_type=code&scope=" + scope + "&state=" + state + "#wechat_redirect";
            } else {
                String redirectUri = RequestUtils.buildFullUrl(request, "/weixin/web/auth");
                redirectUri = URLEncoder.encode(redirectUri, "utf-8");
                String state = URLEncoder.encode(backUrl, "utf-8");
                authorizeURL = "https://open.weixin.qq.com/connect/qrconnect?appid=" + webAppId + "&redirect_uri=" + redirectUri
                        + "&response_type=code&scope=" + webScope + "&state=" + state + "#wechat_redirect";
            }
        } catch (UnsupportedEncodingException e) {
            Log.error(e.getMessage(), e);
            return null;
        }
        return authorizeURL;
    }

}
