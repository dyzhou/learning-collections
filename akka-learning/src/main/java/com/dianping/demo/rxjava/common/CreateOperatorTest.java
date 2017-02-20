/*
 * Create Author  : shang.gao
 * Create Date    : 2016-05-31
 * Project        : learning-collections
 * File Name      : CreateOperatorTest.java
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

import java.util.ArrayList;

/**
 * 功能描述:
 * Create — 通过调用观察者的方法从头创建一个Observable
 * Defer — 在观察者订阅之前不创建这个Observable，为每一个观察者创建一个新的Observable
 * Empty/Never/Throw — 创建行为受限的特殊Observable
 * From — 将其它的对象或数据结构转换为Observable
 * Interval — 创建一个定时发射整数序列的Observable
 * Just — 将对象或者对象集合转换为一个会发射这些对象的Observable
 * Range — 创建发射指定范围的整数序列的Observable
 * Repeat — 创建重复发射特定的数据或数据序列的Observable
 * Start — 创建发射一个函数的返回值的Observable
 * Timer — 创建在一个指定的延迟之后发射单个数据的Observable
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-05-31
 * @since learning-collections 1.0
 */
public class CreateOperatorTest
{
    public static void main(String[] args)
    {
        /**
         * from
         */
        Subscription s = Observable.from(Lists.newArrayList(1, 2, 3, 4, 5)).subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer integer)
            {
                System.out.println(integer);
            }
        });
        /***
         * just
         */
        Subscription s1 = Observable.just(Lists.newArrayList(1, 2, 3, 4, 5)).subscribe(new Action1<ArrayList<Integer>>()
        {
            @Override
            public void call(ArrayList<Integer> integers)
            {
                System.out.println(integers);
            }
        });
        while (!s.isUnsubscribed() || !s1.isUnsubscribed())
        {
            ;
        }
        System.out.println("finished");

        /**
         * create
         */
        Observable.create(new Observable.OnSubscribe<Integer>()
        {
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                try
                {
                    for (Integer i : Lists.newArrayList(1, 2, 3, 4, 5, 6))
                    {
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                }
                catch (Exception e)
                {
                    subscriber.onError(e);
                }
            }
        }).filter(new Func1<Integer, Boolean>()
        {
            @Override
            public Boolean call(Integer integer)
            {
                return integer != 3 && integer != 4;
            }
        }).subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer integer)
            {
                System.out.println(integer);
            }
        });
    }
}
