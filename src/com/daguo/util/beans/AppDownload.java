/**
 * 互相学习 共同进步
 */
package com.daguo.util.beans;

import java.util.List;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-12 上午11:56:23
 * @function ： 实体类，app下载的内容。
 */
public class AppDownload {

	private String type;// 分类
	private List<DLInfo> lists;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public List<DLInfo> getLists() {
		return lists;
	}

	public void setLists(List<DLInfo> lists) {
		this.lists = lists;
	}

	

}
