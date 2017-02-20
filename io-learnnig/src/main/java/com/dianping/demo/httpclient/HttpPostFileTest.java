/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-29
 * Project        : learning-collections
 * File Name      : HttpPostFileTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.httpclient;

import com.dianping.demo.httpclient.meituan.ImageResult;
import com.dianping.demo.httpclient.meituan.MeituanBAUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * 功能描述:  美团云文件上传示例,通过java原始的URLConnection实现post文件上传<p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-29
 * @since learning-collections 1.0
 */
public class HttpPostFileTest
{
    private String urlBasic = "http://pic-in.meituan.com/storage/";

    private static final String bucket = "beautyimg";

    private static final String client_id = "beautytechnician";

    private static final String client_secret = "9fa1a8damrj0ewhracuracur818djy1w";

    String request_path = this.urlBasic + this.bucket;

    public ImageResult doPost(String filePath, String fileName)
    {
        // 换行符
        final String boundaryPrefix = "--";
        // 定义数据分隔线
        String BOUNDARY = "========7d4a6d158c9";
        // 服务器的域名
        try
        {
            URL url = new URL(request_path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            //PS:美团认证方式
            Date date = new Date();
            String response = url.getPath();
            String dateString = MeituanBAUtils.toGmtString(date);
            String authorization = MeituanBAUtils.toAuthorization(dateString, response, this.client_secret, this.client_id);
            conn.setRequestProperty("Date", dateString);
            conn.setRequestProperty("Authorization", authorization);

            conn.connect();


            OutputStream out = new DataOutputStream(conn.getOutputStream());


            //PS:普通字段发送，name=filename,value=dyzhou.png
            StringBuilder sb2 = new StringBuilder();
            sb2.append(boundaryPrefix).append(BOUNDARY).append("\r\n");
            sb2.append("Content-Disposition: form-data;name=\"" + "filename" + "\"" + "\r\n");
            sb2.append( "\r\n");
            sb2.append("dyzhou.png");
            sb2.append("\r\n");
            out.write(sb2.toString().getBytes());

            //PS:文件发送
            File file = new File(filePath);
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix).append(BOUNDARY).append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\r\n" + "\"" + "\r\n");
            sb.append("Content-Type:application/octet-stream");
            sb.append("\r\n").append("\r\n");
            out.write(sb.toString().getBytes());
            out.write(IOUtils.toByteArray(new FileInputStream(file)));
            out.write("\r\n".getBytes());
            // 写上结尾标识
            byte[] end_data = ("\r\n" + boundaryPrefix + BOUNDARY + boundaryPrefix + "\r\n").getBytes();
            out.write(end_data);
            out.flush();
            out.close();
            if (200 != conn.getResponseCode())
            {
                System.out.println(IOUtils.toString(conn.getErrorStream()));
                return null;
            } else
            {
                return ImageResult.toImageResult(IOUtils.toString(conn.getInputStream()));
            }

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args)
    {
       ImageResult result= new HttpPostFileTest().doPost("/Users/zhoudengyun/2465143B-5935-41FF-9AC7-1043FC1D03A5_960x1280.png","test.png");
        System.out.println(result);
    }

}
