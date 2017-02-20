/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-28
 * Project        : learning-collections
 * File Name      : HttpPostTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.httpclient;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-28
 * @since learning-collections 1.0
 */
public class HttpPostTest
{

    public void doPost(String path)
    {
        URL url = null;
        PrintWriter writer = null;
        try
        {
            url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(3000);
            connection.setDefaultUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Origin", "http://my.csdn.net");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            connection.setRequestProperty("Cookie", "uuid_tt_dd=-2332430863138799216_20160419; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1463124486; bdshare_firstime=1463124585997; lzstat_uv=33578652303538958844|3550616@2981463@3517495@3497199@3311294@3602465@666888@2671462; __utma=17226283.1271394943.1463124488.1469717230.1469717230.1; __utmc=17226283; __utmz=17226283.1469717230.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.1271394943.1463124488; UserName=qq_28846841; UserInfo=fPfW1LCQ0DGOlN7NE%2Bkkl1kLAIYPKoseZKSiKJ69g%2BkIgrtmG6TYmeWB%2FC5HT6BtWA%2F3aurHDN0Q6gBVpR5tvw%3D%3D; UserNick=qq_28846841; AU=22F; UN=qq_28846841; UE=\"\"; BT=1469756097879; access-token=3c6fbca9-94a8-4863-b0cf-5be003aba624; dc_tos=ob1zmk; dc_session_id=1469756828279");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
            connection.connect();
            String params = "&method=saveUserContact&username=qq_28846841&contactinfo=[18226735510]&pubemail=779653408@qq.com&submobile=";
            writer = new PrintWriter(connection.getOutputStream());
            writer.write(params);
            writer.flush();
            if (200 != connection.getResponseCode())
            {
                System.out.println(connection.getResponseMessage());
                System.out.println(IOUtils.toString(connection.getErrorStream()));
            }
            System.out.println(IOUtils.toString(connection.getInputStream()));

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (writer != null)
            {
                writer.close();
            }

        }
    }

    public static void main(String[] args)
    {
        new HttpPostTest().doPost("http://my.csdn.net/service/main/uc");
    }
}
