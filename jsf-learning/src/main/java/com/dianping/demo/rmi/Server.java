/*
 * Create Author  : shang.gao
 * Create Date    : 2016-06-30
 * Project        : learning-collections
 * File Name      : Server.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rmi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-06-30
 * @since learning-collections 1.0
 */
public class Server
{
    public static void main(String[] args) throws IOException
    {
        try
        {
            RmiServiceImpl service = new RmiServiceImpl();
            LocateRegistry.createRegistry(1099);
            Naming.bind("rmi://localhost:1099/test", service);
            System.out.println("注册远程对象成功");

        }
        catch (AlreadyBoundException e)
        {
            e.printStackTrace();
        }
    }
}
