/*
 * Create Author  : dengyun.zhou
 * Create Date    : 2017-02-19
 * Project        : learning-collections
 * File Name      : SelectorTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.io.reactor.simple;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  <p>
 *
 * @author : dengyun.zhou <p>
 * @version 1.0 2017-02-19
 * @since learning-collections 1.0
 */
public class SelectorTest
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        List<Selector> ls = new ArrayList<>();
        int i=0;
        while (i<2001){
            ls.add(Selector.open());
            System.out.println(++i);
        }
        TimeUnit.SECONDS.sleep(1000000);
    }
}
