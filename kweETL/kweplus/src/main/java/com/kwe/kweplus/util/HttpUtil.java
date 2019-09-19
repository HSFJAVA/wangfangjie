package com.kwe.kweplus.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpUtil {


    private String request;

    private String response;


    public boolean upload(String Url, String serviceNo,String customerType, File files,String customerID,String templateID) throws Exception {
        request = String.format("{Url:%s,serviceNo:%s,customerType:%s,files:%s,customerID:%s,templateID:%s}",Url,serviceNo,customerType,files.getName(),customerID,templateID);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String,String> postParam = new HashMap<>();
        postParam.put("serviceNo",serviceNo);
        postParam.put("customerType",customerType);
        postParam.put("customerID",customerID);
        postParam.put("templateID",templateID);
        //把一个普通参数和文件上传给下面这个地址    是一个servlet
        HttpPost httpPost = new HttpPost(Url);
        //把文件转换成流对象FileBody
        FileBody fundFileBin = new FileBody(files);
        //设置传输参数
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        multipartEntity.addPart("files", fundFileBin);//相当于<input type="file" name="media"/>
        //设计文件以外的参数
        Set<String> keySet = postParam.keySet();
        for (String key : keySet) {
            //相当于<input type="text" name="name" value=name>
            multipartEntity.addPart(key, new StringBody(postParam.get(key), ContentType.create("text/plain", Consts.UTF_8)));
        }
        HttpEntity reqEntity =  multipartEntity.build();
        httpPost.setEntity(reqEntity);
        //发起请求   并返回请求的响应
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取响应对象
        this.response = response.toString();
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            this.response = result;
            Map map = (Map) JSON.parse(result);
            if ((int)map.get("code") == 1) {
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }


}
