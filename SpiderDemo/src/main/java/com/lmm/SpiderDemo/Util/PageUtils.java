package com.lmm.SpiderDemo.Util;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmm.SpiderDemo.domain.Page;

/**
 * 	页面工具类
 * */
public class PageUtils {
	
	/**
	 	获取页面内容
	 * @param url
	 * @return
	 * */
	//如果不是静态的 则不能在下面静态方法重用
	private static Logger logger = LoggerFactory.getLogger(PageUtils.class);
	
	
	public static String getContent(String url) {
		
		//使用httpclient 下载网页内容   
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient httpclient = builder.build();
		String result =null;
		try {
			//开始下载时间
			long start_time = System.currentTimeMillis();
			
			HttpGet get = new HttpGet(url);
			
			//执行get请求 获取指定url的页面的内容
			CloseableHttpResponse response = httpclient.execute(get);
	    	//从responce中解析页面内容
			 result = EntityUtils.toString(response.getEntity());
			 
			 logger.info("页面下载成功：{},消耗{}毫秒",url ,System.currentTimeMillis()-start_time);
			 
		
		} catch (IOException e) {
			logger.error("页面下载失败:{}",url);
			e.printStackTrace();
		}
		return result;
	}
}
