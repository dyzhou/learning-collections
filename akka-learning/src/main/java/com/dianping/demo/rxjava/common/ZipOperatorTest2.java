/*
 * Create Author  : shang.gao
 * Create Date    : 2016-06-28
 * Project        : learning-collections
 * File Name      : MergeOperatorTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rxjava.common;

import com.google.common.collect.Lists;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func3;
import scala.util.parsing.combinator.testing.Str;

import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  结合操作，
 * And/Then/When
 * CombineLatest
 * Join
 * Merge
 * StartWith
 * Switch
 * Zip
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-06-28
 * @since learning-collections 1.0
 */
public class ZipOperatorTest2
{
    public void merge()
    {
        Observable o1 = Observable.from(Lists.newArrayList(1, 2, 3));
        Observable o2 = Observable.from(Lists.newArrayList(6, 7, 8));
        Observable.merge(o1, o2).subscribe(new Action1()
        {
            @Override
            public void call(Object o)
            {
                System.out.println(o);
            }
        });
    }

    public String zip()
    {
        int i = 0;
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                subscriber.onNext("hello");
                subscriber.onCompleted();
            }
        });
        Observable<Integer> observable2 = Observable.create(new Observable.OnSubscribe<Integer>()
        {
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(2);
                }
                catch (InterruptedException e)
                {
                }
                subscriber.onNext(23);
                subscriber.onCompleted();
            }
        });
        Observable<Double> observable3 = Observable.just(null);
        String result = Observable.zip(observable1, observable2, observable3, new Func3<String, Integer, Double, String>()
        {
            @Override
            public String call(String t1, Integer t2, Double t3)
            {
                return null;
            }
        }).toBlocking().first();
        return result;
    }

    public static void main(String[] args)
    {
        try
        {
            System.out.println(new ZipOperatorTest2().zip().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
