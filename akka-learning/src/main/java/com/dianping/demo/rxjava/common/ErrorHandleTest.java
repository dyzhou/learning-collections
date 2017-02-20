/*
 * Create Author  : shang.gao
 * Create Date    : 2016-09-28
 * Project        : learning-collections
 * File Name      : ErrorHandleTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rxjava.common;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-09-28
 * @since learning-collections 1.0
 */
public class ErrorHandleTest
{
    public void errorTest()
    {
        int i=0;
        Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(1);
                    if(i==0){
                        throw new RuntimeException("1...test");
                    }
                    subscriber.onNext("hello");
                }
                catch (Exception e)
                {
                    System.out.println("1-------------" + e);
                    subscriber.onError(e);
                }
            }
        }).onErrorReturn(new Func1<Throwable, String>()
        {
            @Override
            public String call(Throwable throwable)
            {
                System.out.println("throwable........");
                return null;
            }
        }).flatMap(new Func1<String, Observable<String>>()
        {
            @Override
            public Observable<String> call(String s)
            {
                return Observable.create(new Observable.OnSubscribe<String>()
                {
                    @Override
                    public void call(Subscriber<? super String> subscriber)
                    {
                        try
                        {
                            TimeUnit.SECONDS.sleep(2);
                            subscriber.onNext(s + "------world");
                        }
                        catch (Exception e)
                        {
                            System.out.println("2 error-------------" + e);
                            subscriber.onError(e);
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                System.out.println("3-------------" + s);
            }
        });
    }

    public static void main(String[] args) throws IOException
    {
        try
        {
            new ErrorHandleTest().errorTest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.in.read();
    }
}
