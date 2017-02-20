/*
 * Create Author  : dengyun.zhou
 * Create Date    : 2017-02-20
 * Project        : learning-collections
 * File Name      : Server.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.io.reactor.multi;

import com.dianping.demo.consts.Constants;

import java.io.IOException;

import static com.dianping.demo.io.reactor.multi.ServerContext.start;

/**
 * 功能描述:  <p>
 *
 * @author : dengyun.zhou <p>
 * @version 1.0 2017-02-20
 * @since learning-collections 1.0
 */
public class Server
{
    public static void main(String[] args) throws IOException
    {
        start(Constants.PORT,3);
    }
}
