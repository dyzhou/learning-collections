package com.dianping.demo.io.reactor.multi;


import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * Reactor反应器
 * <ul>
 * <li>及时响应相对于的读/写IO事件</li>
 * <li>并分发到合适的Handler处理器上进行业务处理</li>
 * <li>类似AWT中的单例事件分发线程</li>
 * </ul>
 */
public class SubReactor extends Thread{

	protected final long timeout;
	protected Selector selector;
	
	public SubReactor(long timeout){
		this.timeout = timeout;
	}
	
	@Override
	public void run(){
		try {
			selector = Selector.open();
			System.out.println(selector+ ": subReactor running......");
			while(!Thread.interrupted()){
				//不可以使用阻塞的select方式，否则accept后subReactor的selector在register的时候会一直阻塞
				//但是修改为带有超时的select或者selectNow后，subReactor的selector在register就不会阻塞了
				//最终选择了带有超时的select是因为使用selectNow的无限循环会导致CPU飙高特别快
				//并且如果使用阻塞的select方式，还需要知道在哪里调用wakeup，否则会一直阻塞，使用非阻塞方式就不需要wakeup了
				//selector.select();
				//if(selector.selectNow() > 0){
				if(selector.select(timeout) > 0){
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
					while(iterator.hasNext()){
						SelectionKey key = iterator.next();
						iterator.remove();
						dispatch(key);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 事件和事件处理器的绑定
	 * <ul>
	 * <li>管理IO读/写事件到事件处理器的一一对应的绑定</li>
	 * </ul>
	 */
	private void dispatch(SelectionKey key){
		Runnable r = (Runnable)key.attachment();
		if(r != null){
			r.run();
		}
	}
	
}
