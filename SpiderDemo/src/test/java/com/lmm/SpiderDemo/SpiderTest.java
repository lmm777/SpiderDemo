package com.lmm.SpiderDemo;

import java.util.List;

import org.junit.Test;

import com.lmm.SpiderDemo.domain.Page;
import com.lmm.SpiderDemo.download.HttpclientableImpl;
import com.lmm.SpiderDemo.process.JdProcessableImpl;
import com.lmm.SpiderDemo.store.ConsoleStoreableImpl;
import com.lmm.SpiderDemo.store.MysqlStoreableImpl;
//首先编写测试类
public class SpiderTest {

	@Test
	public void testSpider() {
		//创建一个爬虫对象
		Spider spider = new Spider();
		
		//给下载接口设置实现类   后期想用哪个实现类就可以传哪个实现类
		spider.setDownloadable(new HttpclientableImpl());
	   
		//给解析接口设置实现类
		spider.setProcessable(new JdProcessableImpl());
		
		//给存储接口设置实现类
		spider.setStoreable(new MysqlStoreableImpl());
		
		
		//进行下载功能
		String url ="https://list.jd.com/list.html?cat=9987,653,655";
		Page page = spider.download(url);
		//System.out.println(result.getContent());
		spider.process(page);
		//取出解析之后存到page对象中的页面内的url
		List<String> urlList = page.getUrls();
		//System.out.println(page.getValues());
		//spider.store(page);
	}
}
