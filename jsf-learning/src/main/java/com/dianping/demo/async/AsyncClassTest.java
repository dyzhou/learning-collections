/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-19
 * Project        : learning-collections
 * File Name      : AsyncClassTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.async;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述: synchronized(class)很特别，它会让另一个线程在任何需要获取class做为monitor的地方等待。
 * class与this作为不同的监视器可以同时使用，不存在一个线程获取了class，另一个线程就不能获取该class的一切实例。 <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-19
 * @since learning-collections 1.0
 */
public class AsyncClassTest
{
    public static void classLockTest()
    {
        synchronized (AsyncClassTest.class)
        {
            System.out.println(Thread.currentThread().getId() + "begin lock class");
            try
            {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("release class load");
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void instanceLockTest()
    {
        synchronized (this)
        {
            System.out.println("get  instance lock");
        }
    }

    public static void main(String[] args)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                AsyncClassTest.classLockTest();
            }
        }).start();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                new AsyncClassTest().classLockTest();
            }
        }).start();
        try
        {
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
