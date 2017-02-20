/*
 * Create Author  : shang.gao
 * Create Date    : 2016-05-31
 * Project        : learning-collections
 * File Name      : MatchOperationTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rxjava.common;

import com.google.common.collect.Lists;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.List;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-05-31
 * @since learning-collections 1.0
 */
public class MatchOperationTest
{
    public static void main(String[] args)
    {
        Observable observable = Observable.from(Lists.newArrayList(1, 2, 3, 4, 5, 6));

        observable.subscribe(new Action1()
        {
            @Override
            public void call(Object o)
            {
                System.out.println(o);
            }
        });

        observable.toList().subscribe(new Action1<List<Integer>>()
        {
            @Override
            public void call(List<Integer> integers)
            {
                System.out.println(integers);
            }
        });
    }
}
