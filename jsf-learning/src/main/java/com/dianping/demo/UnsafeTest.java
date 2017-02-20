/*
 * Create Author  : shang.gao
 * Create Date    : 2016-06-02
 * Project        : learning-collections
 * File Name      : Test.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-06-02
 * @since learning-collections 1.0
 */
public class UnsafeTest
{

    public Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException
    {
        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        // return (Unsafe) theUnsafeInstance.get(null);是等价的
        return (Unsafe) theUnsafeInstance.get(Unsafe.class);
    }

    public static void main(String[] args)
    {
        Class k = TestObject.class;
        try
        {
            Unsafe UNSAFE = new UnsafeTest().getUnsafe();
            long nameOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("name"));
            long ageOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("age"));
            TestObject object = new TestObject();
            object.setName("hello");
            object.setAge("12");
            UNSAFE.compareAndSwapObject(object, nameOffset, object.getName(), "dyzhou");
            System.out.println(object.toString());


            Integer i=10;
            UNSAFE.compareAndSwapInt(i,0,10,20);
            System.out.println(i);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

    }

    public static class TestObject
    {
        private String name;

        private String age;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getAge()
        {
            return age;
        }

        public void setAge(String age)
        {
            this.age = age;
        }

        @Override
        public String toString()
        {
            return "TestObject{" +
                    "name='" + name + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }
}
