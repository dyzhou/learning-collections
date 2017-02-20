package com.dianping.demo.demo;/*
 * Create Author  : chao.ji
 * Create  Time   : 15/3/2 下午5:45
 * Project        : portal
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

import java.io.Serializable;

public class DemoModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    public String id;

    public String mobile;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }


    @Override
    public String toString() {
        return "DemoModel{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
