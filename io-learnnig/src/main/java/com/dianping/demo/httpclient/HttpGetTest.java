/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-28
 * Project        : learning-collections
 * File Name      : HttpGetTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: http发起get请求，访问有cookie限制的站点,demo:访问csdn账号中心<p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-28
 * @since learning-collections 1.0
 */
public class HttpGetTest
{
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HttpGetTest.class);

    public void doGet(String path)
    {
        URL url;
        BufferedReader reader = null;
        try
        {
            url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(3000);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Content-Type", "text/html");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
            connection.setRequestProperty("Cookie", "uuid_tt_dd=-2332430863138799216_20160419; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1463124486; bdshare_firstime=1463124634271; download_first=1; lzstat_uv=33578652303538958844|3550616@2981463@3517495@3497199@3311294@3602465@666888@2671462; __utma=17226283.1271394943.1463124488.1469717230.1469717230.1; __utmc=17226283; __utmz=17226283.1469717230.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.1271394943.1463124488; _gat=1; UserName=qq_28846841; UserInfo=fPfW1LCQ0DGOlN7NE%2Bkkl1kLAIYPKoseZKSiKJ69g%2BkIgrtmG6TYmeWB%2FC5HT6BtWA%2F3aurHDN0Q6gBVpR5tvw%3D%3D; UserNick=qq_28846841; AU=22F; UN=qq_28846841; UE=\"\"; BT=1469756097879; access-token=3c6fbca9-94a8-4863-b0cf-5be003aba624; dc_tos=ob1zh2; dc_session_id=1469756630644");
            connection.connect();
            System.out.println(connection.getResponseCode());
            if (200 != connection.getResponseCode())
            {
                logger.error(String.format("http request fail, header line:%s", connection.getResponseMessage()));
                return;
            }
            //PS:response返回头信息
            for (Map.Entry<String, List<String>> stringListEntry : connection.getHeaderFields().entrySet())
            {
                if (stringListEntry.getValue().toString().contains("HTTP/1.1") || stringListEntry.getValue().toString().contains("HTTP/1.0"))
                {
                    if (!stringListEntry.getValue().toString().contains("200"))
                    {
                        logger.error(String.format("http request fail, header line:%s", stringListEntry.getValue()));
                        return;
                    }
                }
                System.out.println(String.format("key:%s,value:%s", stringListEntry.getKey(), stringListEntry.getValue()));
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                System.out.println(line);
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
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            }
        }

    }

    public static void main(String[] args) throws IOException
    {
        new HttpGetTest().doGet("http://download.csdn.net/my/downloads");
    }

}
