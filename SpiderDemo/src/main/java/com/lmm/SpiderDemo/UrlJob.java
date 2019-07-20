package com.lmm.SpiderDemo;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.lmm.SpiderDemo.Util.RedisUtil;

import redis.clients.jedis.Jedis;
/**
 * 再redis中执行
 *  lpush startUrl https://list.jd.com/list.html?cat=9987,653,655
 * */
public class UrlJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("定时任务执行了");
		
		Jedis jedis = RedisUtil.getJedis();
		List<String> urlList = jedis.lrange("startUrl", 0, -1);
		for(String url : urlList) {
			jedis.lpush("urlList", url);
		}
	}

}
