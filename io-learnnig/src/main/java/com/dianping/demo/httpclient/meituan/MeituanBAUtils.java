/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-26
 * Project        : technician-thirdparty-server
 * File Name      : MeituanBAUtils.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.httpclient.meituan;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-26
 * @since technician-thirdparty-server 1.0
 */
public class MeituanBAUtils
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MeituanBAUtils.class);

    private static final String HMAC_SHA1 = "HmacSHA1";

    public MeituanBAUtils()
    {
    }

    public static String hmacSHA1(String key, String data)
    {
        try
        {
            SecretKeySpec e = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(e);
            byte[] rawHmac = mac.doFinal(data.getBytes("utf-8"));
            return Base64.encodeBase64String(rawHmac);
        }
        catch (Exception var5)
        {
            logger.error("hmacSHA1 fail: ", var5);
            return null;
        }
    }

    public static String toGmtString(Date date)
    {
        SimpleDateFormat sd = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss \'GMT\'", Locale.ENGLISH);
        sd.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sd.format(date);
    }

    public static String toAuthorization(String date, String path, String client_secret, String client_id)
    {
        StringBuilder sign = (new StringBuilder()).append("POST").append(" ").append(path).append("\n").append(date);
        String token = hmacSHA1(client_secret, sign.toString());
        String authorization = "MWS " + client_id + ":" + token;
        return authorization;
    }
}
