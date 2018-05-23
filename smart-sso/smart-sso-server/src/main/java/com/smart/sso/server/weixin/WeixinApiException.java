package com.smart.sso.server.weixin;

import com.alibaba.fastjson.JSONObject;

public class WeixinApiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9149148945176800586L;

	public WeixinApiException(String url, JSONObject postData, JSONObject result) {
		super(url + "\r\npost：" + postData.toJSONString() + "\r\nresponse：" + result.toString());
	}

	public WeixinApiException(String url, JSONObject result) {
		super(url + "\r\nresponse：" + result.toString());
	}

	public WeixinApiException(String url, String result) {
		super(url + "\r\nresponse：" + result);
	}

}
