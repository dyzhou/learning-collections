/*
 * Create Author  : shang.gao
 * Create Date    : 2016-02-19
 * Project        : learning-collections
 * File Name      : ConcurrencyLevelTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo;

import com.dianping.demo.demo.DemoModel;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 功能描述:  <p>
 *
 *
 * @author : shang.gao <p>
 *
 * @version 1.0 2016-02-19
 *
 * @since learning-collections 1.0
 */
public class ConcurrencyLevelTest {
    public static void main(String[] args) {
        final ConcurrentLinkedHashMap<DemoModel, String> hashMap = new ConcurrentLinkedHashMap.Builder<DemoModel, String>().
                concurrencyLevel(8).
                initialCapacity(1000).
                maximumWeightedCapacity(Integer.MAX_VALUE).
                listener(new EvictionListener<DemoModel, String>() {
                    @Override
                    public void onEviction(DemoModel key, String value) {
                        System.out.println("I am died," + key);
                    }
                }).build();

        List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
        ExecutorService executorService = Executors.newFixedThreadPool(500);

        Long beginTime = System.currentTimeMillis();

        for (int i = 0; i < 500; i++) {
            Future<Integer> future = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for (int i = 1; i <= 5000; i++) {
                        DemoModel demoModel = new DemoModel();
                        demoModel.setId(Thread.currentThread().getId() + ":" + i);
                        hashMap.put(demoModel, demoModel.toString());
                    }
                    return 1;
                }
            });
            futures.add(future);
        }

        int count = 0;
        for (int i = 0; i < futures.size(); i++) {
            if (futures.get(i).isDone()) {
                try {
                    count += futures.get(i).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                i = i - 1 < 0 ? 0 : i - 1;
            }
        }

        System.out.println("totoal TIme:" + (System.currentTimeMillis() - beginTime));
        System.out.println(count + ":" + hashMap.size());
        System.exit(0);
    }
}
