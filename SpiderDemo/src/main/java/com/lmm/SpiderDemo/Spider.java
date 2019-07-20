package com.lmm.SpiderDemo;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.RuntimeErrorException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lmm.SpiderDemo.Util.Config;
import com.lmm.SpiderDemo.Util.HtmlUtils;
import com.lmm.SpiderDemo.Util.PageUtils;
import com.lmm.SpiderDemo.Util.SleepUtils;
import com.lmm.SpiderDemo.domain.Page;
import com.lmm.SpiderDemo.download.Downloadable;
import com.lmm.SpiderDemo.download.HttpclientableImpl;
import com.lmm.SpiderDemo.process.JdProcessableImpl;
import com.lmm.SpiderDemo.process.Processable;
import com.lmm.SpiderDemo.repository.QueueRepositoryImpl;
import com.lmm.SpiderDemo.repository.RedisRepositoryImpl;
import com.lmm.SpiderDemo.repository.Repository;
import com.lmm.SpiderDemo.store.ConsoleStoreableImpl;
import com.lmm.SpiderDemo.store.Storeable;

public class Spider {
	// 声明下载功能的接口
	private Downloadable downloadable = new HttpclientableImpl();
	
	//声明解析功能的接口
	private Processable processable;
	
	//声明存储的接口
	private Storeable storeable = new ConsoleStoreableImpl();

	//声明url仓库的接口
	private Repository repository = new QueueRepositoryImpl();
	
	//创建一个logger对象记录日志信息
	private Logger logger = LoggerFactory.getLogger(Spider.class);
	
	//创建一个固定大小线程池
	ExecutorService threadPool = Executors.newFixedThreadPool(Config.nThread);

	public void start() {
		check();
		logger.info("开始抓取数据了");
		
		//用一个死循环使其一致执行  取url -- 下载 -- 解析--存 url 
		while(true){
			//从url仓库中取url--第一次队列中是没有元素的 需要有一个原始的url 种子地址
			final String url = repository.poll();
			if(url!=null) { //防止取到最后没有url了 
				
				threadPool.execute(new Runnable() {
					public void run() {
						//将取出来的url进行下载 得到页面对象（使用接口内的下载方法 在之后可以指向任何实现类 更加方便）
						Page page = Spider.this.download(url);
						//对下载下来的页面进行解析  解析的内容业存在page对象中  1.上面的url时下一页的url 解析出来的就是列表商品url和下一页url 2.如果上商品url解析出来的就是商品信息 
						Spider.this.process(page);
						//将解析过程中封存到page对象中的临时url取出来   只可能是1.解析的内容就是url
						List<String> urls = page.getUrls();
						for (String nextUrl : urls) {
							//将解析出来的临时的url添加到url仓库中 --到最后一页时 取出最后一页列表上所有的商品url之后就没有url了 也没法在存到队列中则队列就空了
							Spider.this.repository.add(nextUrl);
						}
						//如果是商品明细页面 则需要将存到page中的解析内容存储起来  执行store
						if (url.startsWith("https://item.jd.com/")) {
							Spider.this.store(page);
						}
						
						//为避免出问题  这里让每个线程跑到这的时候 都歇息几秒 --休息这项功能花费很长代码  而且很重复 所以可以提取成工具类
						SleepUtils.sleep(Config.millions_1);
						
					}
				});
				
			
			}else {
				logger.info("没有url了，休息一会。。。");
				SleepUtils.sleep(Config.millions_5);
				
			}
			
		}
	}
	
	
	/**
	 * 	检测功能 检查爬虫的运行环境是否正常
	 * 
	 * 可以在上面接口声明出指定默认的实现类
	 * 如果不想用默认的再改  直接再main方法中制定即可
	 * 本次 除解析类之外 都是设置成默认的实现类
	 * 则解析类就有可能忘记设置 
	 * */
	private void check() {
		//如果忘了设置解析类
		if(processable == null) {
		String msg = "请设置解析类！";
		logger.error(msg);
		//抛出一个运行时异常，直接停止程序
		throw new RuntimeException(msg);
		}
		logger.info("=================配置检查start...====================");
		//注意{} 表示后面的
		logger.info("downloadable的实现类是：{}",downloadable.getClass().getSimpleName());
		logger.info("processable的实现类是：{}",processable.getClass().getSimpleName());
		logger.info("storeable的实现类是：{}",storeable.getClass().getSimpleName());
		logger.info("repository的实现类是：{}",repository.getClass().getSimpleName());
		logger.info("=================配置检查end...====================");
		
	}



	/**
	 * 向url厂库添加种子地址
	 * */
	
	public void setSeedUrl(String url) {
		this.repository.add(url);
	}
	
	
	/**
	 * 	下载指定url的页面
	 * */
	public Page download(String url) {
		return this.downloadable.download(url);
	}

	/**
	 * 	解析指定页面的内容
	 * */
	public void process(Page page) {
		 this.processable.process(page);
	}
	
	
	public void store(Page page) {
		this.storeable.store(page);

	}

	// 通过set方法 设置接口的实现类 再测试类调用set方法传一个实现类过去
	public void setDownloadable(Downloadable downloadable) {
		this.downloadable = downloadable;
	}

	public void setProcessable(Processable processable) {
		this.processable = processable;
	}

	public void setStoreable(Storeable storeable) {
		this.storeable = storeable;
	}
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}



	//设置程序执行的入口
	public static void main(String[] args) {
		//创建一个爬虫 （自己）
		Spider spider = new Spider();
		//设置初始url 
		String url ="https://list.jd.com/list.html?cat=9987,653,655";
		

		//给接口设置实现类
		
		spider.setProcessable(new JdProcessableImpl());
		spider.setRepository(new RedisRepositoryImpl());
		//添加种子地址--添加原始url
		spider.setSeedUrl(url);     //要放到设置实现类的下面  因为用到了仓库接口
		
		//开始爬虫
		spider.start();
	}
	
	
	
	
	
	
	
	

}
