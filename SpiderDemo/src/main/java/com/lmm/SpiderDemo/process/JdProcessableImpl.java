package com.lmm.SpiderDemo.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lmm.SpiderDemo.Util.HtmlUtils;
import com.lmm.SpiderDemo.Util.PageUtils;
import com.lmm.SpiderDemo.domain.Page;

public class JdProcessableImpl implements Processable {

	@Override
	public void process(Page page) {

		HtmlCleaner htmlCleaner = new HtmlCleaner();
		// 对整个页面进行解析，返回根节点的对象rootNote
		TagNode rootNode = htmlCleaner.clean(page.getContent());
		// 判断是否是列表页面的url
		if (page.getUrl().startsWith("https://list.jd.com/list.html")) {
			// 解析列表页面的数据
			Object[] evaluateXPath;
			try {
				evaluateXPath = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
				for (Object object : evaluateXPath) {
					TagNode aNode = (TagNode) object;
					String url = aNode.getAttributeByName("href");
					page.addUrl("https:" + url);
				}
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//获取下一页的url
			String nextUrl = HtmlUtils.getAttrByName(rootNode, "//*[@id=\"J_bottomPage\"]/span[1]/a[10]", "href");
			if(nextUrl!=null) {
				page.addUrl("https://list.jd.com"+nextUrl);
			}
		
		
		} else {

			parseProduct(page, rootNode);
		}
	}

	private void parseProduct(Page page, TagNode rootNode) {
		try {
			/**
			 * 获取标题
			 */
			String title = HtmlUtils.getText(rootNode, "//div[@class=\"sku-name\"]");
			page.addFiled("title", title);
			/**
			 * 获取价格
			 * 
			 */
			getPrice(page);
			/**
			 * 获取图片地址
			 */
			String tem_url = HtmlUtils.getAttrByName(rootNode, "//*[@id=\"spec-img\"]", "data-origin");
			String pic_url = "https:" + tem_url;
			page.addFiled("pic_url", pic_url);

			/**
			 * 获取规格参数
			 */
			getSpec(page, rootNode);

		} catch (XPatherException e) {
			e.printStackTrace();
		}
	}

	private void getSpec(Page page, TagNode rootNode) throws XPatherException {
		// 先获取大分类的标签信息
		Object[] evaluateXPath3 = rootNode.evaluateXPath("//*[@id=\"detail\"]/div[2]/div[2]/div[1]/div");

		// 保存规格参数的json数组
		JSONArray specArr = new JSONArray();

		// 迭代大分类的标签信息
		for (Object divObj : evaluateXPath3) {
			// 获取具体的大分类的标签
			TagNode divNode = (TagNode) divObj;
			// 在获取的div标签上再进行过滤，获取所有的dl标签
			Object[] evaluateXPath4 = divNode.evaluateXPath("/dl/dl");
			for (Object dlObj : evaluateXPath4) {
				// 获取具体的小分类标签dl
				TagNode dlNode = (TagNode) dlObj;
				// 接下来获取dl标签下的dt标签的内容
				// 定义一个json对象封装具体内容
				JSONObject jsonObj = new JSONObject();
				Object[] dtObj = dlNode.evaluateXPath("/dt");
				if (dtObj.length > 0) {
					TagNode dtNode = (TagNode) dtObj[0];
					jsonObj.put("name", dtNode.getText());
				}
				// 获取dl标签下最后一个dd标签的内容
				Object[] ddObj = dlNode.evaluateXPath("/dd[last()]");
				if (ddObj.length > 0) {
					TagNode ddNode = (TagNode) ddObj[0];
					jsonObj.put("value", ddNode.getText());
				}
				// 把组装好的jsonObj对象添加到isonarr数组中
				specArr.add(jsonObj);
			}
		}
		// 将json数组转为字符串存到map集合中
		page.addFiled("spec", specArr.toJSONString());
	}

	/**
	 * 价格的url不能固定 所以根据其规律 提取出不同价格的差异点 -- 价格的不同是由一个id控制的，提出该id以属性goodsId代替
	 * goodId可有原始url通过正则表达式匹配出来
	 */
	private void getPrice(Page page) {
		// 原始url
		String dataUrl = page.getUrl();
		// 使用正则获取原始数据url中的商品id
		Pattern pattern = Pattern.compile("https://item.jd.com/([0-9]+).html");
		Matcher matcher = pattern.matcher(dataUrl);
		String goodsId = null;
		if (matcher.find()) {
			goodsId = matcher.group(1);
			page.setGoodsId(goodsId);
		}
		// 根据url获取页面内容 -- 用到页面的工具包
		String pricecontent = PageUtils.getContent("https://p.3.cn/prices/mgets?skuIds=J_" + goodsId);
		// 根据json数组相关方法取出价格
		// 得到内容中的json数组
		JSONArray jsonArr = JSONArray.parseArray(pricecontent);
		if (jsonArr.size() > 0) {
			JSONObject jsonObj = jsonArr.getJSONObject(0);// 取出Json数组中的第一个对象
															// {"cbf":"0","id":"J_100005603522","m":"9999.00","op":"2699.00","p":"2699.00"}
			String price = jsonObj.getString("p").replaceAll("\\.00", "");// 去掉小数点后的00
			page.addFiled("price", price);
		}
	}

}
