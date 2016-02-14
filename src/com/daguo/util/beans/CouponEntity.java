package com.daguo.util.beans;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-14 上午10:51:57
 * @function 功能:优惠卷信息实体类
 */
public class CouponEntity {
	private String id;// id
	private String img_path;// 标志图片路径
	private String img_src;// 内容图片路径
	private String content;// 内容
	private String title;// 标题1
	private String title2;// 标题2
	private String status;// 查看全部列表时为是否领取，查看个人优惠卷信息时为是否已使用

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getImg_src() {
		return img_src;
	}

	public void setImg_src(String img_src) {
		this.img_src = img_src;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
