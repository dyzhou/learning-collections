/*
 * Create Author  : shang.gao
 * Create Date    : 2016-01-14
 * Project        : akka-learning
 * File Name      : ScheduleTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo.rxjava;

import rx.functions.Action0;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  使用Scheduler调度任务<p>
 *
 *
 * @author : shang.gao <p>
 *
 * @version 1.0 2016-01-14
 *
 * @since akka-learning 1.0
 */
public class ScheduleTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        rx.Scheduler.Worker worker = Schedulers.computation().createWorker();
        worker.schedulePeriodically(new Action0() {
            @Override
            public void call() {
                System.out.println("hello world,thread id:" + Thread.currentThread().getId());
            }
        }, 5, 3, TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(10);
        System.out.println("main ended:" + Thread.currentThread().getId());
        worker.unsubscribe();
        System.in.read();
    }
}
