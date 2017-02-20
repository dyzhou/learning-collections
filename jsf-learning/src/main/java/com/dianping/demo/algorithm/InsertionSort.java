/*
 * Create Author  : shang.gao
 * Create Date    : 2016-08-11
 * Project        : learning-collections
 * File Name      : InsertionSort.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.algorithm;

import java.util.Arrays;

/**
 * 插入排序:时间复杂度未O(n^2),实现简单，只适用于小数据量的排序
 * 功能描述:  有一个已经有序的数据序列，要求在这个已经排好的数据序列中插入一个数，但要求插入后此数据序列仍然有序
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-08-11
 * @since learning-collections 1.0
 */
public class InsertionSort
{
    /**
     * 第一个元素作为有序数组，一次将剩下的元素插入有序数组，并保证插入之后的数组依然是有序数组。
     *
     * @param src  待排序数组
     * @param low  无序队列的第一个元素下标
     * @param high 最后一个元素的下标
     */
    public void insertSort(int[] src, int low, int high)
    {
        // Insertion sort on smallest arrays
        for (int i = low; i < high; i++)
        {
            for (int j = i; j > low && src[j - 1] > src[j]; j--)
            {
                int tmp = src[j];
                src[j] = src[j - 1];
                src[j - 1] = tmp;
            }
        }
        return;

    }

    public static void main(String[] args)
    {
        int[] data = new int[]{1, 5, 6, 4, 2, 3, 4, 5, 2, 3, 4, 3, 4, 2, 1, 2, 3, 5, 7, 8, 9, 9, 9, 10, 9, 11, 9, 2, 111};
        new InsertionSort().insertSort(data, 0, data.length);
        System.out.println(Arrays.toString(data));
    }
}
