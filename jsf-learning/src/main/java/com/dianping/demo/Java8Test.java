/*
 * Create Author  : shang.gao
 * Create Date    : 2016-03-09
 * Project        : learning-collections
 * File Name      : Java8Test.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 功能描述:  <p>
 *
 *
 * @author : shang.gao <p>
 *
 * @version 1.0 2016-03-09
 *
 * @since learning-collections 1.0
 */
public class Java8Test {
    public static void main(String[] args) {
        List<Integer> ls = Lists.newArrayList(1, 2, 3, 4, 5);
        List<Integer> ls2 = Lists.newArrayList(1, 2, 3, 4, 5);
        List<Integer> ls3 = Lists.newArrayList(1, 2, 3, 4, 5);

//        int sum = Stream.of(ls, ls2, ls3).flatMap(item -> item.stream()).mapToInt(i -> i.intValue()).sum();
//        System.out.println(sum);
    }
}
