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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 功能描述:  NIO reactor模型，单线程版本实现，handler部分，主要完成业务处理，一个client对应一个Handler对象，非线程安全<p>
 *
 * @author : dengyun.zhou <p>
 * @version 1.0 2017-02-17
 * @since learning-collections 1.0
 */
public class Handler implements Runnable
{
    final SocketChannel socket;

    final SelectionKey sk;

    ByteBuffer input = ByteBuffer.allocate(2048);

    ByteBuffer output = ByteBuffer.allocate(2048);

    static final int SENDING = 0, READING = 1;

    int state = SENDING;

    Handler(Selector sel, SocketChannel c) throws IOException
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
            if (state == READING)
            {
                read();
            } else if (state == SENDING)
            {
                send();
            }
        }
        catch (IOException ex)
        { /* ... */ }
    }

    void read() throws IOException
    {
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
        }
        System.out.println("data from client:" + builder.toString());
        sk.cancel();
    }

    void send() throws IOException {
        state = READING;
        socket.write(ByteBuffer.wrap(new String("connected to server successed").getBytes()));
        sk.interestOps(SelectionKey.OP_READ);
    }
}
