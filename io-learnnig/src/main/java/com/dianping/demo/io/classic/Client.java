/*
 * Create Author  : shang.gao
 * Create Date    : 2016-01-15
 * Project        : akka-learning
 * File Name      : IOClient.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo.io.classic;

import com.dianping.demo.consts.Constants;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 功能描述: 普通IO处理Socket内容，一个请求对应一个Thread，客户端。
 *
 *
 * @author : dengyun.zhou <p>
 *
 * @version 1.0 2016-01-15
 *
 * @since akka-learning 1.0
 */
public class Client
{

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), Constants.PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()),10);
        char[] buffer = new char[2048];
        reader.read(buffer);
        System.out.println(new String(buffer));
        while (true) {
            System.out.print("请输入要发送的内容：");
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String message = console.readLine();
            writer.write(message);
            writer.write("\n\t");
            writer.flush();
        }
    }
}
