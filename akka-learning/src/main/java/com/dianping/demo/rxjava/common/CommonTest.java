/*
 * Create Author  : shang.gao
 * Create Date    : 2016-01-14
 * Project        : akka-learning
 * File Name      : Test.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rxjava.common;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.Arrays;

/**
 * 功能描述:  <p>
 *
 * @author : dengyun.zhou <p>
 * @version 1.0 2016-01-14
 * @since akka-learning 1.0
 */
public class CommonTest
{
    public void create()
    {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>()
                {
                    @Override
                    public void call(Subscriber<? super String> sub)
                    {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );
        Subscriber<String> mySubscriber = new Subscriber<String>()
        {
            @Override
            public void onNext(String s)
            {
                System.out.println(s);
            }

            @Override
            public void onCompleted()
            {
            }

            @Override
            public void onError(Throwable e)
            {
            }
        };
        Subscription subscription = myObservable.subscribe(mySubscriber);
    }

    public void test()
    {
        //from
        Integer list[] = new Integer[]{1, 2, 3, 4, 5, 6};
        Observable.from(list).subscribeOn(Schedulers.io()).subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer integer)
            {
                System.out.println("current thred:" + Thread.currentThread().getId() + ":" + Thread.currentThread().getName());
                System.out.println(integer);
            }
        });
        //just
        Observable.just(list).subscribe(new Action1<Integer[]>()
        {
            @Override
            public void call(Integer[] integers)
            {
                System.out.println(Arrays.toString(integers));
            }
        });
        //create
        Observable observable = Observable.create(new Observable.OnSubscribe()
        {
            @Override
            public void call(Object o)
            {
                System.out.println(o);
            }
        });
        observable.map(new Func1()
        {
            @Override
            public Object call(Object o)
            {
                return o.toString() + "gagagagag";
            }
        }).subscribe(new rx.Subscriber()
        {
            @Override
            public void onCompleted()
            {
            }

            @Override
            public void onError(Throwable e)
            {
            }

            @Override
            public void onNext(Object o)
            {
            }
        });
    }

    public static void main(String[] args)
    {
        new CommonTest().create();
    }
}
