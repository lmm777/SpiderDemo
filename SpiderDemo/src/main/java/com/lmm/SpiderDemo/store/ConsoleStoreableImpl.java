package com.lmm.SpiderDemo.store;

import com.lmm.SpiderDemo.domain.Page;

/**
 * 向控制台打印
 * */
public class ConsoleStoreableImpl implements Storeable{

	@Override
	public void store(Page page) {
		System.out.println(page.getUrl()+"----"+page.getValues().get("price"));
		
	}

}
