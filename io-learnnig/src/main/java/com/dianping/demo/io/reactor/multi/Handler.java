package com.dianping.demo.io.reactor.multi;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/***
 *
 */
public class Handler extends Thread {

	private State state;
	protected final SocketChannel clientChannel;
	protected final SelectionKey key;
	
	protected final ByteBuffer readBuf = ByteBuffer.allocate(1024);
	protected final StringBuilder readData = new StringBuilder();

	public Handler(Selector selector, SocketChannel clientChannel){
		this.state = State.INIT;
		SelectionKey key = null;
		try {
			clientChannel.configureBlocking(false);
			key = clientChannel.register(selector, this.state.opBit);
			key.attach(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.clientChannel = clientChannel;
		this.key = key;
		System.out.println(selector+" connect success...");
	}

	@Override
	public void run() {
		switch (state) {
			case INIT:
				interestOps(State.READ);
				break;
			case READ:
				readAndProcess();
				break;
			case WRITE:
				write();
				break;
			default:
				System.out.println("\nUnsupported State: "+state+" ! overlap processing with IO...");
		}
	}

	private synchronized void readAndProcess(){
		int readSize;
		try {
			while((readSize = clientChannel.read(readBuf)) > 0){
				readData.append(new String(Arrays.copyOfRange(readBuf.array(), 0, readSize)));
				readBuf.clear();
			}
			if(readSize == -1){
				disconnect();
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}

		state = State.PROCESSING;
		Processor processor = new Processor();
		processor.run();
	}

	private synchronized void processAndHandOff(){
		try
		{
			//do your biz
			System.out.println("processAndHandOff, read data:" + readData.toString());
			readData.delete(0,readData.length());
			interestOps(State.WRITE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


	}

	private final class Processor implements Runnable{
		@Override 
		public void run() { 
			processAndHandOff(); 
		}
	}


	
	private void write(){
		try {
			do{
				clientChannel.write(ByteBuffer.wrap(new String("connected to server successed").getBytes()));
			}while(!writeIsComplete());
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
		
		interestOps(State.READ);
	}
	
	/**
	 * 事件和事件处理器的绑定
	 * <ul>
	 * <li>类似AWT中的addActionListener添加监听器/观察者</li>
	 * </ul>
	 * 不需要重置key的附件（key.attach）是因为key一直绑定使用的是当前this实例，
	 * 在Reactor dispatch的时候如果是接受（accept）该附件就是Acceptor实例，
	 * 否则就是绑定到该key的同一个Handler实例
	 */
	private void interestOps(State state){
		this.state = state;
		key.interestOps(state.opBit);
	}
	
	private void disconnect(){
		try {
			clientChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\nclient Address=【"+clientAddress(clientChannel)+"】 had already closed!!! ");
	}

	private static SocketAddress clientAddress(SocketChannel clientChannel)
	{
		return clientChannel.socket().getRemoteSocketAddress();
	}

	public boolean writeIsComplete()
	{
		return true;
	}

	private enum State
	{
		INIT(0),
		READ(SelectionKey.OP_READ),
		PROCESSING(2),
		WRITE(SelectionKey.OP_WRITE);

		private int opBit;

		State(int operateBit)
		{
			opBit = operateBit;
		}
	}


}
