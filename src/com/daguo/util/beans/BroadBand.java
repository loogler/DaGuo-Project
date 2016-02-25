package com.daguo.util.beans;

/**
 * 实体类 宽带信息
 * 
 * @author Bugs_Rabbit 時間： 2015-8-5 下午3:40:18
 */
public class BroadBand {

	private String busiName;// 运营商
	private String costInfo;// 套餐详情
	private String month;// 服务时间
	private String detailName;//
	private String price;// 套餐价格
	private String school_id;// 学校id
	private String sName;// 学校名字
	private String orderId;// 款单id

	public String getBusiName() {
		return busiName;
	}

	public void setBusiName(String busiName) {
		this.busiName = busiName;
	}

	public String getCostInfo() {
		return costInfo;
	}

	public void setCostInfo(String costInfo) {
		this.costInfo = costInfo;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
