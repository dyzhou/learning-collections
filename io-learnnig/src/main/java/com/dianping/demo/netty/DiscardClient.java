/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-04
 * Project        : learning-collections
 * File Name      : DiscardClient.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.netty;

import com.dianping.demo.domain.Person;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-04
 * @since learning-collections 1.0
 */
public class DiscardClient
{
    public void request() throws InterruptedException, UnknownHostException
    {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>()
        {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception
            {
                ch.pipeline()
//                        .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                        .addLast(new ObjectEncoder());
//                        .addLast(new DiscardClientHandler());
            }
        });
        ChannelFuture f = bootstrap.connect(new InetSocketAddress(InetAddress.getLocalHost(), 3838)).sync();
        int i = 0;
        while (i<50)
        {
            Person p = new Person();
            p.setAge(10 + i);
            p.setName("dyzhou" + i++);
            f.channel().writeAndFlush(p);
//            TimeUnit.SECONDS.sleep(1);
        }
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException, UnknownHostException
    {
        new DiscardClient().request();
    }
}
