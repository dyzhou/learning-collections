/*
 * Create Author  : shang.gao
 * Create Date    : 2016-02-19
 * Project        : learning-collections
 * File Name      : ForkJoinTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo;

import java.util.concurrent.ForkJoinPool;

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
public class ForkJoinTest {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        });
    }
}
