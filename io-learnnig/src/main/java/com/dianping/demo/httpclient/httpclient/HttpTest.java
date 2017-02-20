/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-29
 * Project        : learning-collections
 * File Name      : HttpTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.httpclient.httpclient;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-29
 * @since learning-collections 1.0
 */
public class HttpTest
{
    private void httpEntityTest() throws UnsupportedEncodingException
    {
        HttpEntity myEntity = new StringEntity("hello,world!", ContentType.create("text/plain", Consts.UTF_8));
        HttpEntity myEntity2= new StringEntity("hello,world!!");
        System.out.println(myEntity.getContentLength());
        System.out.println(myEntity.getContentType());
    }

}
