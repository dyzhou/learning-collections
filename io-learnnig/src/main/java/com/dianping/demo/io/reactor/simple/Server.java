/*
 * Create Author  : dengyun.zhou
 * Create Date    : 2017-02-17
 * Project        : learning-collections
 * File Name      : Server.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.io.reactor.simple;

import com.dianping.demo.consts.Constants;

import java.io.IOException;

/**
 * 功能描述:  <p>
 *
 * @author : dengyun.zhou <p>
 * @version 1.0 2017-02-17
 * @since learning-collections 1.0
 */
public class Server
{
    public static void main(String[] args) throws IOException
    {
        new Reactor(Constants.PORT,true).run();
    }
}
