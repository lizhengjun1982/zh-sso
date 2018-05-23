<%--
  Created by IntelliJ IDEA.
  User: lizhengjun
  Date: 2018/5/12
  Time: 6:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta id="viewport" name="viewport"
          content="width=device-width,minimum-scale=1,maximum-scale=1,initial-scale=1,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <title>登录伏羲云书院</title>
    <jsp:include page="common/weui.jsp"/>
</head>
<body>
<div class="weui-msg">
    <!--
    <div class="weui-msg__icon-area">
        <i class="weui-icon-success weui-icon_msg"></i>
    </div>
     -->
    <div class="weui-msg__text-area">
        <h2 class="weui-msg__title">伏羲云课堂</h2>
        <p class="weui-msg__desc">
        </p>
    </div>
    <div class="weui-msg__opr-area">
        <p class="weui-btn-area">
            <a href="${weiXinAuthorizeURL}"
               class="weui-btn weui-btn_primary">微信登录</a>
            <a href="javascript:history.back();"
               class="weui-btn weui-btn_primary">手机号登录</a>
            <a
                    href="javascript:history.back();" class="weui-btn weui-btn_default">手机号快捷注册</a>
        </p>
    </div>
</div>
</body>
</html>
