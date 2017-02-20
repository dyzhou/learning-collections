/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-01
 * Project        : learning-collections
 * File Name      : TaskTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.juc;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-01
 * @since learning-collections 1.0
 */
public class TaskTest
{

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException
    {
        /***
         * FutureTask提供isDone回调，当子线程执行完毕后
         */
        FutureTask<String> task = new FutureTask<String>(new Callable<String>()
        {
            @Override
            public String call() throws Exception
            {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("...........");
                return "hello,world";
            }
        })
        {
            @Override
            protected void done()
            {
                try
                {
                    System.out.println(get() + ":  finished");
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
            }
        };
        Executors.newCachedThreadPool().submit(task);


    }
}
