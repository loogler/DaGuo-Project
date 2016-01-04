/**
 * 互相学习 共同进步
 */
package com.daguo.util.beans;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-3 下午3:26:40
 * @function ： 積分商品实体类，
 */
public class CentGoods {
    private String id;
    private String name;
    private String thumb_path;
    private String img_path;
    private String score;
    private String good_desc;

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
     * @return the good_desc
     */
    public String getGood_desc() {
	return good_desc;
    }

    /**
     * @param good_desc
     *            the good_desc to set
     */
    public void setGood_desc(String good_desc) {
	this.good_desc = good_desc;
    }

}
