/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-22
 * Project        : learning-collections
 * File Name      : ThreadLocalTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.async;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-22
 * @since learning-collections 1.0
 */
public class ThreadLocalTest
{
    private static ThreadLocal<String> AllNames = new ThreadLocal<>();

    private void setThreadName(String name)
    {
        AllNames.set(name);
    }

    private String getThreadName()
    {
        return AllNames.get();
    }

    public static void main(String[] args) throws InterruptedException
    {
        final ThreadLocalTest test = new ThreadLocalTest();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("my thread id:" + Thread.currentThread().getId());
                test.setThreadName(Thread.currentThread().getId() + "");
            }
        }).start();
        //
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("my thread id:" + Thread.currentThread().getId());
                test.setThreadName(Thread.currentThread().getId() + "");
            }
        }).start();

        TimeUnit.SECONDS.sleep(2);

        //
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("my thread id:" + Thread.currentThread().getId());
                test.setThreadName(Thread.currentThread().getId() + "");
                System.out.println("get from thread local:" + test.getThreadName());
            }
        }).start();

        TimeUnit.SECONDS.sleep(2);
    }

}
