/*
 * Create Author  : shang.gao
 * Create Date    : 2016-05-31
 * Project        : learning-collections
 * File Name      : TransferTest.java
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

import java.util.ArrayList;

/**
 * 功能描述: flatMap拉平关系,将元素observable发送的每一个数据项转换为一个新的observable对象，然后再将所有的observable数据集合并成一个observable <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-05-31
 * @since learning-collections 1.0
 */
public class TransferTest
{
    public static void main(String[] args)
    {
        /***
         * map操作，映射关系
         */
        Observable.from(Lists.newArrayList(Lists.newArrayList(1, 2), Lists.newArrayList(3, 4, 5, 6))).map(new Func1<ArrayList<Integer>, ArrayList<Integer>>()
        {
            @Override
            public ArrayList<Integer> call(ArrayList<Integer> integers)
            {
                return integers;
            }
        }).subscribe(new Action1<ArrayList<Integer>>()
        {
            @Override
            public void call(ArrayList<Integer> integers)
            {
                System.out.println(integers);
            }
        });
        /***
         * flatMap拉平关系
         */
        Observable.from(Lists.newArrayList(Lists.newArrayList(1, 2), Lists.newArrayList(3, 4, 5, 6))).flatMap(new Func1<ArrayList<Integer>, Observable<Integer>>()
        {
            @Override
            public Observable<Integer> call(ArrayList<Integer> integers)
            {
                return Observable.create(new Observable.OnSubscribe<Integer>()
                {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber)
                    {
                        integers.forEach(i -> subscriber.onNext(i));
                    }
                });
            }
        }).subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer o)
            {
                System.out.println(o);
            }
        });
        System.out.println("------------------------------------------------------------------");
        Observable.from(Lists.newArrayList(Lists.newArrayList(1, 2, 3, 4, 5))).flatMap(new Func1<Integer, Observable<Integer>>()
        {
            @Override
            public Observable<Integer> call(Integer integer)
            {
                return Observable.create(new Observable.OnSubscribe<Integer>()
                {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber)
                    {
                        if (integer == 3 || integer == 4)
                        {
                            subscriber.onCompleted();
                            return;
                        }
                        subscriber.onNext(integer);
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer integer)
            {
                System.out.println(integer);
            }
        });
        System.out.println("__________________________********_________________________");

        Observable.from(Lists.newArrayList(1, 2, 3)).flatMap(new Func1<Integer, Observable<Integer>>()
        {
            @Override
            public Observable<Integer> call(Integer integer)
            {
                return Observable.create(new Observable.OnSubscribe<Integer>()
                {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber)
                    {
                        for (int i = 0; i <= integer; i++)
                        {
                            subscriber.onNext(i);
                        }
                    }
                });
            }
        }).subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer o)
            {
                System.out.println(o);
            }
        });
    }
}
