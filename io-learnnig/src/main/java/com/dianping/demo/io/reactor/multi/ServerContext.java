package com.dianping.demo.io.reactor.multi;

import com.dianping.demo.consts.Constants;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ServerContext
{

	public static final boolean isLog = true;
	
	private static final int subReactorSize = 3;
	public static final long selectTimeOut = TimeUnit.MILLISECONDS.toMillis(10);
	private static final AtomicLong nextIndex = new AtomicLong();
	
	private static ServerSocketChannel serverChannel;
	private static MainReactor mainReactor;
	private static SubReactor[] subReactors;


	public static void start(int port, int subReactorSize) throws IOException
	{
		try {
			mainReactor = new MainReactor(Constants.PORT, selectTimeOut);
			subReactors = new SubReactor[subReactorSize <= 0 ? 2 : subReactorSize];
			for (int i = 0; i < subReactors.length; i++)
			{
				subReactors[i] = new SubReactor(selectTimeOut);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		mainReactor.start();
		for (SubReactor subReactor : subReactors)
		{
			subReactor.start();
		}

		System.in.read();
	}

	public static SubReactor nextSubReactor()
	{
		long nextIndexValue = nextIndex.getAndIncrement();
		if (nextIndexValue < 0)
		{
			nextIndex.set(0);
			nextIndexValue = 0;
		}
		return subReactors[(int) (nextIndexValue % subReactors.length)];
	}

}
