/**
 * 互相学习 共同进步
 */
package com.daguo.util.beans;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-14 下午1:32:05
 * @function ： 实体类 商品属性
 */
public class Shop_GoodsItem {
    private String goods_desc;
    private String img_path;// 展示图片
    private String img_src;// 内容详情图片
    private String name;// 商品名称
    private String price;// 价格
    private String score;// 积分值
    private String thumb_path;//
    private String type_id;// 分类id
    private String type_name;// 分类名
    private String id;// 商品id
    
    //秒杀 商品
    private String end_time ;
    private String start_time ;
    

    /**
     * @return the goods_desc
     */
    public String getGoods_desc() {
	return goods_desc;
    }

    /**
     * @param goods_desc
     *            the goods_desc to set
     */
    public void setGoods_desc(String goods_desc) {
	this.goods_desc = goods_desc;
    }

    /**
     * @return the img_path
     */
    public String getImg_path() {
	return img_path;
    }

    /**
     * @param img_path
     *            the img_path to set
     */
    public void setImg_path(String img_path) {
	this.img_path = img_path;
    }

    /**
     * @return the img_src
     */
    public String getImg_src() {
	return img_src;
    }

    /**
     * @param img_src
     *            the img_src to set
     */
    public void setImg_src(String img_src) {
	this.img_src = img_src;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the price
     */
    public String getPrice() {
	return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(String price) {
	this.price = price;
    }

    /**
     * @return the score
     */
    public String getScore() {
	return score;
    }

    /**
     * @param score
     *            the score to set
     */
    public void setScore(String score) {
	this.score = score;
    }

    /**
     * @return the thumb_path
     */
    public String getThumb_path() {
	return thumb_path;
    }

    /**
     * @param thumb_path
     *            the thumb_path to set
     */
    public void setThumb_path(String thumb_path) {
	this.thumb_path = thumb_path;
    }

    /**
     * @return the type_id
     */
    public String getType_id() {
	return type_id;
    }

    /**
     * @param type_id
     *            the type_id to set
     */
    public void setType_id(String type_id) {
	this.type_id = type_id;
    }

    /**
     * @return the type_name
     */
    public String getType_name() {
	return type_name;
    }

    /**
     * @param type_name
     *            the type_name to set
     */
    public void setType_name(String type_name) {
	this.type_name = type_name;
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @return the start_time
     */
    public String getStart_time() {
	return start_time;
    }

    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(String start_time) {
	this.start_time = start_time;
    }

    /**
     * @return the end_time
     */
    public String getEnd_time() {
	return end_time;
    }

    /**
     * @param end_time the end_time to set
     */
    public void setEnd_time(String end_time) {
	this.end_time = end_time;
    }

}
