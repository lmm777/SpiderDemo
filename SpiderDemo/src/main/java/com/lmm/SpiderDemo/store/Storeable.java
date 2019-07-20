package com.lmm.SpiderDemo.store;

import com.lmm.SpiderDemo.domain.Page;
/**
 * 代表存储功能的接口
 * */
public interface Storeable {
	void store(Page page);
}
