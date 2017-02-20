/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-04
 * Project        : learning-collections
 * File Name      : Person.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.domain;

import java.io.Serializable;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-04
 * @since learning-collections 1.0
 */
public class Person implements Serializable
{
    private String name;
    private int age;

    private byte[] test=new byte[1024*1024*1024*5];

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }


}
