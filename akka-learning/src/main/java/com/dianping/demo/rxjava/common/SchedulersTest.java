/*
 * Create Author  : shang.gao
 * Create Date    : 2016-05-31
 * Project        : learning-collections
 * File Name      : SchedulersTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rxjava.common;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述: Schedulers可以用来执行自己的异步任务 ,
 * 可以通过Subscription的isUnsubscribed和worker.unsubscribe()方法取消订阅或查询是否订阅完成。<p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-05-31
 * @since learning-collections 1.0
 */
public class SchedulersTest
{
    public static void main(String[] args)
    {
        Scheduler.Worker worker = Schedulers.io().createWorker();
        Subscription result = worker.schedule(new Action0()
        {
            @Override
            public void call()
            {
                try
                {
                    System.out.println("thread:" + Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("hello");
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        while (!result.isUnsubscribed())
        {
            ;
        }
        System.out.println("out");
        //周期性执行,500毫秒后，每个10000毫秒执行一次
        Schedulers.io().createWorker().schedulePeriodically(new Action0()
        {
            @Override
            public void call()
            {
                System.out.println("hello,world");
            }
        }, 500, 1000, TimeUnit.MILLISECONDS);
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
