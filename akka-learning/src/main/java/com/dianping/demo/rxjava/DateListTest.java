/*
 * Create Author  : shang.gao
 * Create Date    : 2016-01-14
 * Project        : akka-learning
 * File Name      : DateListTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo.rxjava;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  <p>
 *
 *
 * @author : shang.gao <p>
 *
 * @version 1.0 2016-01-14
 *
 * @since akka-learning 1.0
 */
public class DateListTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        Observable observable = Observable.from(new String[]{"hello", "world", "!", "1", "2", "3", "4", "5"})
                .subscribeOn(Schedulers.io())
                .skip(1)//跳过前num个元素
                .take(6).map(new Func1() {
                    @Override
                    public Object call(Object o) {
                        System.out.println("observable thread id: " + Thread.currentThread().getId());
                        return o;
                    }
                });//只返回前num个元素

        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                try {
                    System.out.println("Thread id:" + Thread.currentThread().getId() + ":" + s);
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                }
            }
        });

        TimeUnit.SECONDS.sleep(7);

        observable.subscribe(new Observer() {
            @Override
            public void onCompleted() {
                System.out.println("finished");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                System.out.println("Thread id:" + Thread.currentThread().getId() + ":" + o.toString());
            }
        });

        System.out.println("main Thread id:" + Thread.currentThread().getId() + ": ended");
        System.in.read();
    }
}
