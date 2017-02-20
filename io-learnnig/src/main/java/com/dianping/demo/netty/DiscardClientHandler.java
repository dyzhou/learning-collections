/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-04
 * Project        : learning-collections
 * File Name      : DiscardClientHandler.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.netty;

import com.dianping.demo.domain.Person;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-04
 * @since learning-collections 1.0
 */
public class DiscardClientHandler extends SimpleChannelInboundHandler<Person>
{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        Person p = new Person();
        p.setAge(10);
        p.setName("dyzhou");
        ctx.writeAndFlush(p);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Person msg) throws Exception
    {
        System.out.println(msg.toString());
        ctx.close();
    }
}
