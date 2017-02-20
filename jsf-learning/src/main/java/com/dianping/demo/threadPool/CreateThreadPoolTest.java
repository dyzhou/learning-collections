/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-22
 * Project        : learning-collections
 * File Name      : CreateThreadPoolTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.threadPool;

import java.util.concurrent.*;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-22
 * @since learning-collections 1.0
 */
public class CreateThreadPoolTest
{
    private static final int threadSize = 3;

    private static final int corePoolSize = 3;

    private static final int maxPollSize = 3;

    private static final int keepAliveTime = 3;

    private static final int capacity = 100;

    public static void main(String[] args)
    {
        ExecutorService service1 = Executors.newSingleThreadExecutor();
        ExecutorService service2 = Executors.newCachedThreadPool();
        ExecutorService service3 = Executors.newFixedThreadPool(threadSize);
        ScheduledExecutorService service4 = Executors.newScheduledThreadPool(threadSize);
        ExecutorService service5 = new ThreadPoolExecutor(corePoolSize, maxPollSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue(capacity));
    }

}
