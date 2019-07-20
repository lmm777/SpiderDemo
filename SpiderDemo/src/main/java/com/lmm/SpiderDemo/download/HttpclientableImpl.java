package com.lmm.SpiderDemo.download;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.lmm.SpiderDemo.Util.PageUtils;
import com.lmm.SpiderDemo.domain.Page;

//通过httpclient 实现下载功能的实现类

/**
 * 通过   httpclient实现下载功能    的实现类
 * 根据一个url即可获得该url代表的内容---提取成 工具类
 * */
public class HttpclientableImpl implements Downloadable{
//将之前的方法抽取到这里即可  //ctrl shift  f  实现代码的格式化
	@Override
	public Page download(String url) {
		Page page = new Page();
		page.setContent(PageUtils.getContent(url));
		page.setUrl(url);//在此处将原始的url存到了page对象中
		return page;
	
	}

}
