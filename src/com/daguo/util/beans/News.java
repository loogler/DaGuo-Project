package com.daguo.util.beans;

public class News {
    private String id;
    private String school_id;
    private String title;
    private String title2;
    private String img_path;
    private String content;
    private String status;
    private String menu_id;
    private String img_src;
    private String feedbackCount;
    private String goodCount;
    private String create_time ;

    public String getSchool_id() {
	return school_id;
    }

    public void setSchool_id(String school_id) {
	this.school_id = school_id;
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

    public String getImg_path() {
	return img_path;
    }

    public void setImg_path(String img_path) {
	this.img_path = img_path;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getMenu_id() {
	return menu_id;
    }

    public void setMenu_id(String menu_id) {
	this.menu_id = menu_id;
    }

    public String getImg_src() {
	return img_src;
    }

    public void setImg_src(String img_src) {
	this.img_src = img_src;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    /**
     * @return the feedbackCount
     */
    public String getFeedbackCount() {
	return feedbackCount;
    }

    /**
     * @param feedbackCount
     *            the feedbackCount to set
     */
    public void setFeedbackCount(String feedbackCount) {
	this.feedbackCount = feedbackCount;
    }

    /**
     * @return the goodCount
     */
    public String getGoodCount() {
	return goodCount;
    }

    /**
     * @param goodCount
     *            the goodCount to set
     */
    public void setGoodCount(String goodCount) {
	this.goodCount = goodCount;
    }

    /**
     * @return the create_time
     */
    public String getCreate_time() {
	return create_time;
    }

    /**
     * @param create_time the create_time to set
     */
    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

}
