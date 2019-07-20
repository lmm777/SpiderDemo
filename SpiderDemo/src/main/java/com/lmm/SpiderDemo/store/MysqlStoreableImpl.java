package com.lmm.SpiderDemo.store;

import java.util.Date;
import java.util.Map;

import com.lmm.SpiderDemo.Util.MyDateUtils;
import com.lmm.SpiderDemo.Util.MyDbUtils;
import com.lmm.SpiderDemo.domain.Page;


/**
 *	把商品信息存储到mysql中
 * @author xuwei.tech
 *
 */
public class MysqlStoreableImpl implements Storeable{

	@Override
	public void store(Page page) {
		String goodsId = page.getGoodsId();
		String dataurl = page.getUrl();
		Map<String, String> values = page.getValues();
		String picUrl = values.get("pic_url");
		String title = values.get("title");
		String price = values.get("price");
		String param = values.get("spec");
		java.lang.String curr_time = MyDateUtils.formatDate2(new Date());
		MyDbUtils.update(MyDbUtils.INSERT_LOG, goodsId,dataurl,picUrl,title,price,param,curr_time);
		
	}
	
}
