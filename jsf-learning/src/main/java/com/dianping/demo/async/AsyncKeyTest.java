/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-22
 * Project        : learning-collections
 * File Name      : AsyncKeyTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.async;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-22
 * @since learning-collections 1.0
 */
public class AsyncKeyTest
{
    private static Object lock = new Object();

    //同步方法
    public synchronized void test1()
    {
        //TODO something
    }

    //同步代码块
    public void test2()
    {
        //同步代码块
        synchronized (this)
        {
            //TODO something
        }
        //同步代码块
    }

    public void test3()
    {
        //同步代码块
        synchronized (lock)
        {
            //TODO something
        }
    }


    /***************************我是分割线*****************************/


    //静态同步方法
    public static synchronized void test4()
    {
        //TODO something
    }

    public void test5()
    {
        //同步代码块
        synchronized (AsyncClassTest.class)
        {
            //TODO something
        }
        //同步代码块
    }
}
