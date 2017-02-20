/*
 * Create Author  : shang.gao
 * Create Date    : 2016-10-31
 * Project        : learning-collections
 * File Name      : ExecutorServiceTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-10-31
 * @since learning-collections 1.0
 */
public class ExecutorServiceTest
{
    public static void main(String[] args)
    {
        ExecutorService pool= Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++)
        {
            pool.submit(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println(Thread.currentThread().getName()+".....finished");
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.shutdown();
        System.out.println(pool.isShutdown()+"...."+pool.isTerminated());
//        try
//        {
//            pool.awaitTermination(1,TimeUnit.HOURS);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
        while (!pool.isTerminated())
        {
            ;
        }
        System.out.println("all task finished");
    }

}
