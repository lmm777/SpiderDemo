package com.lmm.SpiderDemo.download;

import com.lmm.SpiderDemo.domain.Page;

/**
 * 	实现下载功能的接口
 *  
 *  所有关于下载的功能（方法）都定义在该接口内
 *  并在其对应的是实现类内完成具体实现  
 *  
 *  适用于
 *  万一httpclint的下载方法出现问题或者有更好的下载方法的出现，则httoclint就不能用了 改动将会很麻烦
 *  所以将httpclient实现的下载功能抽取到下载接口的一个实现类里面，对外提供调取方法来执行下载。则之后如果想换 
 *  再接口中再定义一个新的下载功能 ，并在对应实现类中实现 ，则只需要改动爬虫类代码中的调取方法即可
 *
 */
public interface Downloadable {
	//httpClient实现下载的方法
	Page download(String url);  //接口中的方法不需要方法体
}
