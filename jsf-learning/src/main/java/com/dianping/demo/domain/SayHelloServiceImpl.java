/*
 * Create Author  : shang.gao
 * Create Date    : 2016-06-27
 * Project        : learning-collections
 * File Name      : SayHelloServiceImpl.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.domain;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-06-27
 * @since learning-collections 1.0
 */
public class SayHelloServiceImpl implements SayHelloService
{

    @Override
    public String sayHello(String name)
    {
        return "hello,".concat(name);
    }
}
