package com.lmm.SpiderDemo.Util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	//使用单例设计模式
	//1.私有化构造函数   外人无法创建对象，因为创建对象是要调用构造方法
	private RedisUtil() {}
	//2.创建一个私有并且静态的本类的对象
	private static JedisPool jedisPool = null;
	//3.创建一个公共的static方法返回该对象
	public static synchronized Jedis getJedis() {
		if(jedisPool == null) {
			JedisPoolConfig poolConfig = new JedisPoolConfig(); //ctrl t  得到GenericObjectPoolConfig的层级关系
			
			//连接池中最大空闲连接数
			poolConfig.setMaxIdle(10);
			//连接池中最多允许创建的连接数
			poolConfig.setMaxTotal(100);
			//创建连接的最大等待时间
			poolConfig.setMaxWaitMillis(2000);
			//表示从连接池中取出链接时都会测试一下，这样保证去除的连接都是可用的
			poolConfig.setTestOnBorrow(true);
			
			//获取连接池
		 jedisPool = new JedisPool(poolConfig, "192.168.79.101", 6379);//对于poolConfig 直接创建即可

		}
		//返回一个连接
		return jedisPool.getResource();
		
		
	}
	//返回连接池连接
	public static void returnResource(Jedis jedis) {
		jedis.close();
	}
	//关闭连接池
	public static void closePool() {
		jedisPool.close();
	}
}
