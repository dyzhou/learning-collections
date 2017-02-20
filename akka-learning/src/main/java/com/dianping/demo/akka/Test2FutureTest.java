/*
 * Create Author  : shang.gao
 * Create Date    : 2016-02-03
 * Project        : akka-learning
 * File Name      : FutureTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo.akka;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.dispatch.OnComplete;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述: 关于future功能， <p>
 *
 *
 * @author : shang.gao <p>
 *
 * @version 1.0 2016-02-03
 *
 * @since akka-learning 1.0
 */
public class Test2FutureTest {

    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create("test");
        System.out.println("main thread id: " + Thread.currentThread().getId());

        /***
         * 直接执行异步任务，无需创建actor，轻量级，效率高
         */
        Future<String> f = Futures.future(new Callable<String>() {
            public String call() throws Exception {
                System.out.println("f thread id: " + Thread.currentThread().getId());
                Thread.sleep(3000);
                return "hello world!";
            }
        }, system.dispatcher()).andThen(new OnComplete<String>() {
            @Override
            public void onComplete(Throwable throwable, String s) throws Throwable {
                if (throwable != null) {
                    System.out.println(throwable.getMessage());
                }
                System.out.println("f onComplete:" + s);

            }
        }, system.dispatcher());

        System.out.println("f finished.............");

        /***
         * 通过现有的future创建新的future，不影响原有future内容
         * PS： 当f对于的任务已经执行完成了，则下面的map方法在当前线程中执行，如果f未执行完成，则map方法交友dispatcher执行。
         *      map内适用于快是完成的计算，不适合处理耗时操作
         */
        Future<Integer> f2 = f.map(new Mapper<String, Integer>() {
            @Override
            public Integer apply(String parameter) {
                System.out.println("f2 thread id: " + Thread.currentThread().getId());
                return parameter.length();
            }
        }, system.dispatcher());

        System.out.println("convert finished........");

        /***
         * flatMap用于处理多个future的情况，完全异步
         */
        final Future<Integer> f3 = f.flatMap(new Mapper<String, Future<Integer>>() {
            @Override
            public Future<Integer> apply(final String parameter) {
                return Futures.future(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        System.out.println("f3: " + Thread.currentThread().getId());
                        return parameter.length() + 10;
                    }
                }, system.dispatcher());
            }
        }, system.dispatcher());

        Future<Integer> f4 = f2.flatMap(new Mapper<Integer, Future<Integer>>() {
            @Override
            public Future<Integer> apply(final Integer f2Value) {
                return f3.map(new Mapper<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer f3Value) {
                        return f2Value * f3Value;
                    }
                }, system.dispatcher());
            }
        }, system.dispatcher());


        String result1 = Await.result(f, Duration.create(5, TimeUnit.SECONDS));
        int result2 = Await.result(f2, Duration.create(5, TimeUnit.SECONDS));
        int result3 = Await.result(f3, Duration.create(5, TimeUnit.SECONDS));
        int result4 = Await.result(f4, Duration.create(5, TimeUnit.SECONDS));


        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println("result4: " + result4);
    }

}
