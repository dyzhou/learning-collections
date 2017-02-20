/*
 * Create Author  : shang.gao
 * Create Date    : 2016-08-11
 * Project        : learning-collections
 * File Name      : MergeSort.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.algorithm;

import java.util.Arrays;

/**
 * 功能描述: 归并排序算法
 * 思想   : 将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。若将两个有序表合并成一个有序表，称为二路归并。
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-08-11
 * @since learning-collections 1.0
 */
public class MergeSort
{
    /***
     * 对两个有序数组进行排序，将两个有序的数组合并成一个有序数组
     *
     * @param data   数组内容
     * @param first  数组元素起始下标
     * @param middle 数组中间元素下标
     * @param last   数组最后元素下标
     */
    private void merge(int[] data, int first, int middle, int last)
    {
        int a_first = first, a_last = middle, b_first = middle + 1, b_last = last;
        int tmp[] = new int[data.length];
        int t = 0;
        while (a_first <= a_last && b_first <= b_last)
        {
            if (data[a_first] < data[b_first])
            {
                tmp[t++] = data[a_first++];
            } else
            {
                tmp[t++] = data[b_first++];
            }
        }
        while (a_first <= a_last)
        {
            tmp[t++] = data[a_first++];
        }
        while (b_first <= b_last)
        {
            tmp[t++] = data[b_first++];
        }
        for (int i = 0; i < t; i++)
        {
            data[first + i] = tmp[i];
        }
    }

    /***
     * 将一个数组拆分成两段，每段再进行拆分，一直拆分到一个元素组成的数组，视为有序数组，然后再进行归并
     *
     * @param array
     * @param start
     * @param last
     */
    private void mergeSort(int[] array, int start, int last)
    {
        if (start < last)
        {
            int middle = (start + last) / 2;
            mergeSort(array, start, middle);
            mergeSort(array, middle + 1, last);
            merge(array, start, middle, last);
        }
    }

    public void mergeSort(int[] array)
    {
        mergeSort(array, 0, array.length - 1);
    }

    public static void main(String[] args)
    {
        int[] data = new int[]{1, 5, 6, 4, 2, 3, 4, 5, 2, 3, 4, 3, 4, 2, 1, 2, 3, 5, 7, 8, 9, 9, 9, 10, 9, 11, 9, 2, 111};
//        int[] data = new int[]{1, 5, 6, 4, 2};
        new MergeSort().mergeSort(data);
        System.out.println(Arrays.toString(data));
    }

}
