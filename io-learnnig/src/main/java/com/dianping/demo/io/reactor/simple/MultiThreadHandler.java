/*
 * Create Author  : dengyun.zhou
 * Create Date    : 2017-02-12
 * Project        : learning-collections
 * File Name      : Handler.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.io.reactor.simple;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  NIO reactor模型，多线程版本实现，handler部分，一个client对应一个Handler对象，非线程安全
 *           handler线程具体职责：读写数据，具体业务处理由业务线程池来做。
 *
 * @author : dengyun.zhou <p>
 * @version 1.0 2017-02-17
 * @since learning-collections 1.0
 */
public class MultiThreadHandler implements Runnable
{
    final SocketChannel socket;

    final SelectionKey sk;

    ByteBuffer input = ByteBuffer.allocate(2048);

    static final Object SENDING = 1, READING = 2, PROCESSING = 3;

    Object state = SENDING;

    private static ExecutorService workerPool = Executors.newCachedThreadPool();

    MultiThreadHandler(Selector sel, SocketChannel c) throws IOException
    {
        socket = c;
        c.configureBlocking(false);
        sk = socket.register(sel, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_WRITE);
        sel.wakeup();
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println(sk.isReadable() + ":" + sk.isWritable());
            if (state == READING)
            {
                read();
            } else if (state == SENDING)
            {
                send();
            } else if (state == PROCESSING)
            {
            }
        }
        catch (IOException ex)
        { /* ... */ }
    }

    synchronized void read() throws IOException
    {
        state = PROCESSING;
        sk.interestOps(0);
        StringBuilder builder = new StringBuilder();
        while (socket.read(input) > 0)
        {
            if (input.hasArray())
            {
                builder.append(new String(input.array()));
            } else
            {
                input.flip();
                byte[] datas = new byte[input.remaining()];
                input.get(datas);
                builder.append(new String(datas));
            }
            input.clear();
        }
        if (StringUtils.isNotEmpty(builder.toString()))
        {
            workerPool.submit(new ReadProcessor(builder.toString()));
        }
    }

    synchronized void send() throws IOException
    {
        state = PROCESSING;
        try
        {
            socket.write(ByteBuffer.wrap(new String("connected to server successed").getBytes()));
        }
        catch (IOException e)
        {/*do something*/}
        finally
        {
            state = READING;
            sk.interestOps(SelectionKey.OP_READ);
            System.out.println("writeProcessor finished.............");
        }
    }

    synchronized void doRead(String request)
    {
        try
        {
            if(StringUtils.isEmpty(request)){
                System.out.println("request is empty");
            }
            System.out.println(request);
            System.out.println("readProcessor finished.............");
            TimeUnit.MILLISECONDS.sleep(500);
        }
        catch (InterruptedException e)
        {/*do something*/}
        finally
        {
            sk.cancel();
        }
    }

    class ReadProcessor implements Runnable
    {
        private String request;

        public ReadProcessor(String request)
        {
            this.request = request;
        }

        @Override
        public void run()
        {
            doRead(request);

        }
    }

/* 测试失败，服务端收不到客户端的内容，handler会被多次调用

   class WriteProcessor implements Runnable
    {
        @Override
        public void run()
        {
            doWrite();
        }
    }*/
}
