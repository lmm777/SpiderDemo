package com.lmm.SpiderDemo;

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时任务
 * 负责管理种子地址  每天向redis的url仓库中放一个种子地址
 * 
 * */
public class UrlManager {
	public static void main(String[] args)   {
		try {
			//创建一个调度器
			Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
			//开启调度器
			defaultScheduler.start();
			
			//指定具体任务信息  -- 任务名称  默认分组 具体任务
			String simpleName = UrlJob.class.getSimpleName(); //有具体任务类得到的简单类名作为任务名 可变
			JobDetail jobDetail = new JobDetail(simpleName, Scheduler.DEFAULT_GROUP, UrlJob.class);
			Trigger trigger = new CronTrigger(simpleName, Scheduler.DEFAULT_GROUP, "00 00 01 ? * *");
			defaultScheduler.scheduleJob(jobDetail, trigger);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
