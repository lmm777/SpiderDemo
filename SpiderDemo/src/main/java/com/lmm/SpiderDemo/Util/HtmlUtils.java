package com.lmm.SpiderDemo.Util;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class HtmlUtils {
	/**
	  * 
	  * 根据TagNode和xpath获取指定标签的值
	  * @param node
	  * @param xpath
	  * @return 
	  * 
	  * */
	
	public static String getText(TagNode node,String xpath) {
		Object[] evaluateXPath ;
		String result = null;
		try {
		//对根节点执行evaluateXPath 匹配标题对应的规则
		evaluateXPath = node.evaluateXPath(xpath);
		if(evaluateXPath.length>0) {
			//强转为Tagnode类型 获取制定的标签
			TagNode titleNode =	(TagNode)evaluateXPath[0];//在根据xpath匹配原则得到的所有标签中选择第一个 强转成TagNode 类型 方便取出里面的数据
			//获取标签的内容
			result = titleNode.getText().toString().trim();
		}
	} catch (XPatherException e) {
		e.printStackTrace();
	}
		return result;
}	
	
	/**
	 * 	根据tagnode、xpath、attr获取指定标签中指定属性的值
	 * @param node
	 * @param xpath
	 * @param attr
	 * @return
	 */
	
	public static String getAttrByName(TagNode node ,String xpath,String attr) {
		String result = null;
		try {
			Object[] evaluateXPath2 = node.evaluateXPath(xpath);
			if(evaluateXPath2.length>0) {
				TagNode picNode = (TagNode)evaluateXPath2[0];
				result = picNode.getAttributeByName(attr);
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
