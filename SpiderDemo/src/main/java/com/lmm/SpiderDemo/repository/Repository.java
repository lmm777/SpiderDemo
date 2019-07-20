package com.lmm.SpiderDemo.repository;
/**
 * 	url	仓库
 * */
public interface Repository {
	//向厂库添加url
	void add(String url);
	
	//从厂库中取出url
	String poll();
}
