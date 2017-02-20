/*
 * Create Author  : dengyun.zhou
 * Create Date    : 2017-02-12
 * Project        : learning-collections
 * File Name      : Reactor.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.io.reactor.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 功能描述:  NIO reactor模型，单线程版本实现，reactor部分：主要完成client请求连接处理和分发<p>
 *
 * @author : dengyun.zhou <p>
 * @version 1.0 2017-02-12
 * @since learning-collections 1.0
 */
public class Reactor implements Runnable
{
    final Selector selector;

    final ServerSocketChannel serverSocket;

    private boolean  multiThreadHandler;

    Reactor(int port) throws IOException
    {
        this(port, false);
    }

    Reactor(int port, boolean multiThreadHandler) throws IOException
    {
        this.multiThreadHandler = multiThreadHandler;
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
        System.out.println("server is running.....");
    }

    @Override
    public void run()
    {
        try
        {
            while (!Thread.interrupted())
            {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext())
                {
                    SelectionKey key = it.next();
                    it.remove();
                    dispatch(key);
//                    System.out.println("select:,key:" + key.isReadable() + ":" + key.isWritable());
                }
            }
        }
        catch (IOException ex)
        { /* ... */ }
    }

    void dispatch(SelectionKey k)
    {
        Runnable r = (Runnable) (k.attachment());
        r.run();
    }

    class Acceptor implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                SocketChannel c = serverSocket.accept();
                if (c != null)
                {
                    if (multiThreadHandler)
                    {
                        new MultiThreadHandler(selector, c);
                    } else
                    {
                        new Handler(selector, c);
                    }
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
