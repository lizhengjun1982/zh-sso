package com.smart.sso.server.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;

public class HttpUtils {

    public static String getStaticString(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String jsonText = "";
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                        String tempbf;
                        StringBuffer html = new StringBuffer(100);
                        while ((tempbf = br.readLine()) != null) {
                            html.append(tempbf + "\n");
                        }
                        jsonText = html.toString();
                    } finally {
                        instream.close();
                    }
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return jsonText;
    }

    public static JSONObject getJson(String url) throws IOException {
        return JSON.parseObject(getStr(url));
    }

    public static String getStr(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    }

    public static String postData(String url, String data, String format) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(data, "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            if (format != null) {
                entity.setContentType(format);
            }
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                String resData = EntityUtils.toString(response.getEntity());
                return resData;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    public static JSONObject postJson(String url, JSONObject json) throws IOException {
        JSONObject result = null;
        String resData = postData(url, json.toJSONString(), "application/json");
        result = JSON.parseObject(resData);
        return result;
    }

    public static JSONObject postJson(String url, String json) throws IOException {
        JSONObject result = null;
        String resData = postData(url, json, "application/json");
        result = JSON.parseObject(resData);
        return result;
    }

    /**
     * 发送请求
     *
     * @throws UnsupportedEncodingException
     */
    public static String postXml(String url, String xmlParam) throws UnsupportedEncodingException {
        String reslt = "";
        try {
            StringEntity se = new StringEntity(xmlParam, "utf-8");
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(30000)
                    .build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(se);
            httpPost.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            reslt = EntityUtils.toString(entity, "UTF-8");
        } catch (ParseException | IOException e1) {
            e1.printStackTrace();
        }
        return reslt;
    }

    /**
     * 需要加密执行的
     *
     * @param url
     * @param xmlInfo
     * @return
     * @throws Exception
     */
    public static String postHttplientNeedSSL(String url, String xmlInfo, String cretPath, String mrchId)
            throws Exception {
        // 选择初始化密钥文件格式
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 得到密钥文件流
        FileInputStream instream = new FileInputStream(new File(cretPath));
        try {
            // 用商户的ID 来解读文件
            keyStore.load(instream, mrchId.toCharArray());
        } finally {
            instream.close();
        }
        // 用商户的ID 来加载
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mrchId.toCharArray()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        // 用最新的httpclient 加载密钥
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        StringBuffer ret = new StringBuffer();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(xmlInfo));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        ret.append(text);
                    }
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return ret.toString();
    }

    public static String readString(String filePath) {
        String str = "";
        File file = new File(filePath);
        try {
            FileInputStream in = new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer, "UTF-8");
        } catch (IOException e) {
            return null;
        }
        return str;

    }
}
