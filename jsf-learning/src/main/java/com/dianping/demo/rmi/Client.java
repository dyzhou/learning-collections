/*
 * Create Author  : shang.gao
 * Create Date    : 2016-06-30
 * Project        : learning-collections
 * File Name      : Client.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-06-30
 * @since learning-collections 1.0
 */
public class Client
{
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException
    {
        RmiService testService = (RmiService) Naming.lookup("rmi://localhost:1234/test");
        testService.sayHello("helo");
    }
}
