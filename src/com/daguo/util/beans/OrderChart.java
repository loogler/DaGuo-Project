package com.daguo.util.beans;

import java.io.Serializable;

/**
 * 商品订单表 用于支付宝订单参数
 * @author Bugs_Rabbit
 *  時間： 2015-8-15 下午11:24:27
 */
public class OrderChart implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String price;
	private String detail;

	public OrderChart(String name, String price, String detail) {
		this.detail = detail;
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
