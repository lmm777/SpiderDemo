package com.lmm.SpiderDemo.repository;

import com.lmm.SpiderDemo.Util.RedisUtil;

import redis.clients.jedis.Jedis;

public class RedisRepositoryImpl implements Repository{
	
	//定义一个key 其对应值为list类型
	public String urlList = "urlList";
	
	@Override
	public void add(String url) {
		// TODO Auto-generated method stub
		//从连接池中获取连接
		Jedis jedis = RedisUtil.getJedis();
		//将url添加到redis中
		jedis.lpush(urlList, url);
		jedis.close();
	}

	@Override
	public String poll() {
		Jedis jedis = RedisUtil.getJedis();
		return jedis.rpop(urlList);
	}

}
