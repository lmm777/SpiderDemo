package com.lmm.SpiderDemo.process;

import com.lmm.SpiderDemo.domain.Page;

/**
 * 创建表示解析功能的接口 ，具体实现写到实现类中
 * 之前所写的仅适合解析京东的商品页。如果换一家网站 解析规则将要改变 则在定义一个新的是实现类即可
 * 
 * */
public interface Processable {
	
	void process(Page page);
}
