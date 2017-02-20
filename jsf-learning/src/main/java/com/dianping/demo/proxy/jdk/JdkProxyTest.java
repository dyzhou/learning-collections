/*
 * Create Author  : shang.gao
 * Create Date    : 2016-06-27
 * Project        : learning-collections
 * File Name      : JdkProxyTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.proxy.jdk;

import com.dianping.demo.domain.SayHelloService;
import com.dianping.demo.domain.SayHelloServiceImpl;
import com.sun.deploy.net.proxy.ProxyHandler;
import sun.misc.ProxyGenerator;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-06-27
 * @since learning-collections 1.0
 */
public class JdkProxyTest implements InvocationHandler
{
    private Object target;

    public JdkProxyTest(Object target)
    {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        System.out.println(method.getName() + " begining...");
        Object result = method.invoke(target, args);
        System.out.println(method.getName() + "end.....");
        return result;
    }

    public static void main(String[] args) throws IOException
    {
        Class[] interfaces = new Class[1];
        interfaces[0] = SayHelloService.class;
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        SayHelloService service = (SayHelloService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new JdkProxyTest(new SayHelloServiceImpl()));
        String result = service.sayHello("dyzhou");
        System.out.println(result);


        /***
         * 获取动态代理生成的.class文件
         */
        byte[] proxyClassFile = ProxyGenerator.generateProxyClass("$Proxy11",interfaces);
        FileOutputStream outputStream= new FileOutputStream(new File("/data/$Proxy11.class"));
        outputStream.write(proxyClassFile);
        outputStream.flush();
        outputStream.close();
    }
}
