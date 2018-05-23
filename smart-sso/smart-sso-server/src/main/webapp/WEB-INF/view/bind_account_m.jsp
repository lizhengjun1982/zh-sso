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
    <title>账号绑定</title>
    <jsp:include page="common/weui.jsp"/>
</head>
<body>
<div style="padding: 15px;">
    <h3>绑定已有伏羲云书院账号</h3>
    <div class="title-description">绑定后可同步学习记录，并直接用微信登录</div>
    <div class="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" placeholder="请输入账号"/>
            </div>
        </div>
    </div>
    <div class="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <input class="weui-input" type="password" placeholder="请输入密码"/>
            </div>
        </div>
    </div>
    <p class="weui-btn-area">
        <a href="#"
           class="weui-btn weui-btn_primary">绑定账号</a>
    </p>
    <h3 style="margin-top: 35px;">没有伏羲云书院账号</h3>
    <form name="form2" action="bindAutoCreatedAccount" method="post">
        <input type="hidden" name="wxOpenId" value="${wxOpenId}"/>
        <input type="hidden" name="wxUnionId" value="${wxUnionId}"/>
        <input type="hidden" name="backUrl" value="${backUrl}"/>
        <p class="weui-btn-area">
            <a href="javascript:document.form2.submit();"
               class="weui-btn weui-btn_default">用微信登录创建新账号</a>
        </p>
        <p class="title-description">将以在系统中第一次留下的手机号作为账号，以后不可再绑定其他账号</p>
        </p>
    </form>
</div>
</body>
</html>
