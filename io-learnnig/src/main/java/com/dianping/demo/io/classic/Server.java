/*
 * Create Author  : dengyun.zhou
 * Create Date    : 2016-01-15
 * Project        : akka-learning
 * File Name      : IOServer.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.io.classic;

import com.dianping.demo.consts.Constants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述: 普通IO处理Socket内容，一个请求对应一个Thread，服务端。
 *
 * read()方法we为阻塞执行，只有到遇到\n\t，或者文件结尾EOF，或者socket.close方法执行之后，才会返回。
 * ps:BufferedRead对象的readline方法，阻塞执行
 * read(char[])方法，当获取的内容长<char.length时，返回。
 *
 * @author : dengyun.zhou。<p>
 * @version 1.0 2016-01-15
 * @since akka-learning 1.0
 */
public class Server
{

    private ServerSocket serverSocket;

    private List<Socket> currentOnline = new ArrayList<Socket>();

    public Server() throws IOException
    {
        serverSocket = new ServerSocket(Constants.PORT);
    }

    public void start()
    {
        System.out.println("server has been started,waiting for client");
        while (!Thread.interrupted())
        {
            try
            {
                Socket socket = serverSocket.accept();
                currentOnline.add(socket);
                doHandle(socket);
            }
            catch (IOException e)
            {
            }
        }
    }

    private void doHandle(Socket socket)
    {
        System.out.println(socket.getInetAddress() + ":" + socket.getPort());
        new Thread(new Handler(socket)).start();
    }

    public static void main(String[] args) throws IOException
    {
        Server server = new Server();
        server.start();
    }

    /***
     * 业务逻辑处理线程
     */
    class Handler implements Runnable
    {

        private Socket client;

        private BufferedReader reader;

        private BufferedWriter writer;

        public Handler(Socket client)
        {
            try
            {
                this.client = client;
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()), 10);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        @Override
        public void run()
        {
            try
            {
                writer.write("connected to server success!");
                writer.flush();
                String message = "";
                while (!Thread.interrupted())
                {
                    char[] buffer = new char[2048];
                    reader.read(buffer); //同步阻塞
                    message = new String(buffer);
                    if ("end".equalsIgnoreCase(message))
                    {
                        break;
                    }
                    System.out.println(message);
                }
                System.out.println("goodbye");
                reader.close();
                writer.close();
                client.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}


