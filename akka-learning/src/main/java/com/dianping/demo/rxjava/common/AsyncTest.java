/*
 * Create Author  : shang.gao
 * Create Date    : 2016-01-14
 * Project        : akka-learning
 * File Name      : AsyncTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rxjava.common;

import com.google.common.collect.Lists;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  Observable和subscriber默认是在当前线程中执行，
 * 通过Observable.observeOn()和Observable.subscribeOn()指定运行线程<p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-01-14
 * @since akka-learning 1.0
 */
public class AsyncTest
{

    public int getIntService(int i)
    {
        try
        {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (InterruptedException e)
        {
        }
        return new Random(100).nextInt();
    }

    public static void main(String[] args) throws IOException
    {
        AsyncTest test = new AsyncTest();
//        Observable.from(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).map(new Func1<Integer, String>()
//        {
//            @Override
//            public String call(Integer integer)
//            {
//                return test.getIntService(integer) + " hello";
//            }
//        }).subscribeOn(Schedulers.io()).subscribe(new Action1<String>()
//        {
//            @Override
//            public void call(String s)
//            {
//                System.out.println(s);
//            }
//        });   S

        Observable.from(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).flatMap(new Func1<Integer, Observable<String>>()
        {
            @Override
            public Observable<String> call(Integer integer)
            {
                return Observable.create(new Observable.OnSubscribe<String>()
                {
                    @Override
                    public void call(Subscriber<? super String> subscriber)
                    {
                        try
                        {
                            subscriber.onNext(test.getIntService(integer) + " hello");
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                }).subscribeOn(Schedulers.io());
            }
        });

        System.in.read();

    }


}
