package com.lmm.SpiderDemo.repository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueRepositoryImpl implements Repository{

	//声明一个url仓库（用队列，主要为了存储url）
		private Queue<String> queue = new ConcurrentLinkedQueue<>();
	@Override
	public void add(String url) {
		this.queue.add(url);
		
	}

	@Override
	public String poll() {
		
		return this.queue.poll();
	}

}
