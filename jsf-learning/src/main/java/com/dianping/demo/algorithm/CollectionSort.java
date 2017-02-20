/*
 * Create Author  : shang.gao
 * Create Date    : 2016-08-11
 * Project        : learning-collections
 * File Name      : CollectionSort.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.algorithm;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-08-11
 * @since learning-collections 1.0
 */
public class CollectionSort
{
    public static void main(String[] args)
    {
        List<String> list = Lists.newArrayList("2", "1");
        Collections.sort(list);
        for (String s : list)
        {
            System.out.println(s);
        }
    }

}
