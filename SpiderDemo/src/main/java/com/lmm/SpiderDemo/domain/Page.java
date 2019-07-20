package com.lmm.SpiderDemo.domain;
//创建一个实体包domain  里面都是实体类

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 	页面实体类，存储页面相关的属性信息
 *
 */
public class Page {
	
	
	/**
	 * 1.页面内容  即我们下载下来的页面的内容 定义内容属性
	 * */
	private String content;
	
	
	
	
    /**
     * 2.保存解析出来的各种有用的数据--标题 ，价格，图片地址，规格参数
     *  通过自己定义的addFiled方法
     *  page.addFiled("title", title);
     *  page.addFiled("price", price);
     *  page.addFiled("pic_url", pic_url);
     *  
     *  将规格参数以json数组的格式存入map集合
     *  page.addFiled("spec", specArr.toJSONString());
     * */
	private Map<String,String> values = new HashMap<>()	;
	
	
	
	/**
	 * 存储页面原始的url  之后存数据是希望能存入url 同时根据原始url分析价格的url 
	 * */
	private String url;
	
	
	
	/**
	 * 商品id
	 * */
	private String goodsId;
	
	
	/**
	 * url的临时存储仓库  存储的是解析出来的列表页面列表的url和下一页的url
	 * */
	private List<String> urls = new ArrayList<>();
	
	
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	/**
	 * 向map中添加数据
	 * @param field
	 * @param value
	 * */
	 public void addFiled(String filed,String value) {
		 this.values.put(filed, value);
	 }
	 

	 
	 /**
	  * 从map中取出数据
	  * @return
	  * */
	 public Map<String,String> getValues(){
		return this.values; 
	 }

	 
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	 
	 
	 public void addUrl(String url) {
		 this.urls.add(url);
	 }

	public List<String> getUrls() {
		return urls;
	}
	 
	 
	
	 
	 
	 
	 
	 
	 
	 
}
