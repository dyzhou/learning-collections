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
import rx.functions.Func1;
import rx.functions.Func2;

import java.math.BigDecimal;
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
public class ZipOperatorTest
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

    public BigDecimal zip()
    {
        int i = 0;
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(2);
                }
                catch (InterruptedException e)
                {
                }
                if (i == 0)
                {
                    throw new RuntimeException("test..................");
                }
                subscriber.onNext(null);
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
        Observable observable3 = Observable.empty();
        BigDecimal result = Observable.zip(observable1, observable2, new Func2<String, Integer, BigDecimal>()
        {
            @Override
            public BigDecimal call(String s, Integer integer)
            {
                return new BigDecimal(integer);
            }
        }).onErrorReturn(new Func1<Throwable, BigDecimal>()
        {
            @Override
            public BigDecimal call(Throwable throwable)
            {
                return new BigDecimal(12222);
            }
        }).toBlocking().first();
        return result;
    }

    public static void main(String[] args)
    {
        try
        {
            System.out.println(new ZipOperatorTest().zip().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
