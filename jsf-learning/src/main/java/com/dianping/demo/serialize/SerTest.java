/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-05
 * Project        : learning-collections
 * File Name      : SerTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.serialize;

import java.io.*;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-05
 * @since learning-collections 1.0
 */
public class SerTest implements Serializable
{

    private String name = "hello";

    private int age = 10;

    public static void main(String[] args) throws IOException
    {
        FileOutputStream fout = new FileOutputStream("/data/serTest.out");
        ObjectOutputStream oout = new ObjectOutputStream(fout);
        oout.writeObject(new SerTest());
        oout.close();
    }
}
