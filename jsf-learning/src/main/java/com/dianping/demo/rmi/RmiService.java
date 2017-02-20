/*
 * Create Author  : shang.gao
 * Create Date    : 2016-06-30
 * Project        : learning-collections
 * File Name      : RmiService.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.rmi;

import javafx.concurrent.Task;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-06-30
 * @since learning-collections 1.0
 */
public interface RmiService extends Remote
{
    public void sayHello(String name) throws RemoteException;

}
