package com.daguo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * http辅助工具，用于传输http对象的实例，并从网络获取json数据。 接口的不同用于定义不同的对象，从而得到不同的处理操作
 * 
 * @author Bugs_Rabbit 時間： 2015-8-3 上午8:53:04
 */
public class HttpUtil {

	// public static String PHPSESSID = null;
	public HttpUtil() {

	}

	/**
	 * 测试ip 122.0.114.55 正式服务器 ip 115.29.224.248
	 */
	// 创建HttpClient对象
	public static HttpClient httpClient = new DefaultHttpClient();

	/**
	 * http://122.0.114.55:8080/XXYYT/service/ 是管理后台
	 * broadband/queryBroadbandList 是接口类型 ?android=1 对接类型是android
	 * &rows=10&page=1是页面 参数 显示第几页 每页显示几条数据（查询的时候需要添加此参数）
	 * 
	 */
	/**
	 * 服务器
	 */
	public static final String URL = "http://115.29.224.248:8080/XYYYT/service/";
	/**
	 * 图片url查看
	 */
	public static final String IMG_URL = "http://115.29.224.248:8080/uploadFiles/";
	/**
	 * 后台图片
	 */
	public static final String IMG_SHOP = "http://115.29.224.248:8080/ueditor/";
	/**
	 * 
	 * 
	 * 更新接口
	 */
	public static final String UPDATE = URL
			+ "version/queryNewVersion?android=1";
	/**
	 * 下载接口
	 */
	public static final String DOWNLOAD = "http://115.29.224.248:8080/DGXYDL/download/DaGuo.apk";
	/**
	 * 登录接口
	 */
	public static final String LOGIN = URL
			+ "personInfo/queryPersonInfoList?android=1&page=1&rows=1";
	/**
	 * 意见反馈接口 传入个人id
	 */
	public static final String USEROPINION = URL
			+ "adviceFeedback/saveOrUpdate?android=1";

	/**
	 * 上传图片url
	 */
	public static final String IMG_URL_SUB = URL
			+ "fileUpload/upload?android=1";
	/**
	 * 个人信息修改// 或者注册
	 */
	public static final String SUBMIT_USERINFO = URL
			+ "personInfo/saveOrUpdate?android=1";
	/**
	 * 个人信息查询
	 */
	public static final String QUERY_USERINFO = URL
			+ "personInfo/queryPersonInfoList?android=1&rows=1&page=1";

	/****** 订单 **************** split line *******************************************/
	/**
	 * 主表订单，所有订单共有接口
	 */
	public static final String SUBMIT_ORDER_PUB = URL
			+ "order/saveOrUpdate?android=1";
	/**
	 * 查询学校ID--学校名称对应的规范
	 */
	public static final String URL_SCHOOLID = URL
			+ "dict/getDictItemsByDictId?android=1&type=163a0ad4-616c-45b9-bc6d-e516de9ebb8c";
	/**
	 * 宽带查询 所有地区
	 */
	public static final String QUERY_BROADBAND = URL
			+ "broadband/queryBroadbandList?android=1&rows=999&page=1";
	/**
	 * 电话号码查询 所有地区
	 */
	public static final String QUERY_NUMBER = URL
			+ "phoneNumber/queryPhoneNumberListByMobile?android=1&page=1&rows=50";

	/**
	 * 宽带订单详细 提交（未付款）
	 */
	public static final String SUBMIT_BROADBAND_DETAIL_UNPAID = URL
			+ "broadbandDetail/saveOrUpdate?android=1";
	/**
	 * 手机号入网订单详细 提交
	 */
	public static final String SUBMIT_NUMBER_DETAIL = URL
			+ "enterNetDetail/saveOrUpdate?android=1";
	/**
	 * 商品订单详细
	 */
	public static final String SUBMIT_GOODS_DETAIL = URL
			+ "goodsDetail/saveOrUpdate?android=1";
	/**
	 * 查询商品列表 需要传入参数page /id用于区分大类
	 */

	public static final String QUERY_GOODSLIST = URL
			+ "goods/queryGoodsList?android=1&rows=50";
	/****** 领奖 活动************************split line *****************************************************/
	/**
	 * 领奖
	 */
	public static final String QUERY_ACCEPTPRIZE = URL
			+ "acceptPrize/queryAcceptPrizeList?android=1&page=1&rows=10";
	public static final String SUBMIT_AWARDSTATUS = URL
			+ "acceptPrize/saveOrUpdate?android=1";
	/**
	 * 新闻查询
	 */
	public static final String QUERY_NEWS = URL
			+ "article/queryArticleList?android=1&menu_id=db94a88d-5c78-448b-a3a7-4af1c3850571";

	/**
	 * 活动查询
	 */
	public static final String QUERY_HUODONG = URL
			+ "article/queryArticleList?android=1&menu_id=b3b7866c-3bf9-48a7-8caa-effa1fb86782";

	/***** 说说************************** split line ************************************************/
	/**
	 * 说说新建 返回一条说说id
	 */
	public static final String SUBMIT_SHUOSHUO = URL
			+ "topic/saveOrUpdate?android=1";
	/**
	 * 查询所有说说 加入参数可以查看分类 需要添加page rows参数 返回数组
	 */
	public static final String QUERY_SHUOSHUO = URL
			+ "topic/queryTopicList?android=1";
	/**
	 * 说说评论的查询 需要传入id参数 以及page rows参数
	 */
	public static final String QUERY_SHUOSHUO_EVA = URL
			+ "topicFeedback/queryTopicFeedbackList?android=1";
	/**
	 * 说说评论的修改提交 需要传入id 和评论者信息
	 */
	public static final String SUBMIT_SHUSHUO_EVA = URL
			+ "topicFeedback/saveOrUpdate?android=1";

	/* 、********************广告位******************************************8、 */

	/**
	 * 广告位 post 需要参数为 position =轮播广告类型 page=当前页码 rows=当前页码显示条数
	 */
	public static final String QUERY_ADD_BANNER = URL
			+ "ad/queryAdList?android=1";

	/*************************** split line *************************************/
	/**
	 * 字典查询
	 */
	public static final String DICT_SCHOOLNAME = URL
			+ "dict/queryDictItemsByDictId?android=1&type=163a0ad4-616c-45b9-bc6d-e516de9ebb8c";
	public static final String DICT_OPERATOR = URL
			+ "dict/queryDictItemsByDictId?android=1&type=25a261b6-85fa-48df-a29e-f27fb4d32a6d";
	public static final String DICT_NUMBER = URL
			+ "dict/queryDictItemsByDictId?android=1&type=213675c1-e12c-4efe-9fbe-a285e6f7a762";
	public static final String DICT_GOODS = URL
			+ "dict/queryDictItemsByDictId?android=1&type=3497086a-abd0-467e-bab2-47375e635b09";
	public static final String DICT_PAY = URL
			+ "dict/queryDictItemsByDictId?android=1&type=50ba2daa-2d35-4c8e-a032-edcc89ad45c1";
	public static final String DICT_SHUOSHUO = URL
			+ "dict/queryDictItemsByDictId?android=1&type=7c34cc58-e4ba-439d-b86f-4643699f4178";

	/**
	 * 
	 * @param url
	 *            发送请求的URL
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public static String getRequest(final String url) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						synchronized (httpClient) {

							String url2 = url;

							// 创建HttpGet对象。
							HttpGet get = new HttpGet(url2);

							// 发送GET请求
							HttpResponse httpResponse = httpClient.execute(get);
							// 如果服务器成功地返回响应
							if (httpResponse.getStatusLine().getStatusCode() == 200) {
								// 获取服务器响应字符串
								String result = EntityUtils
										.toString(httpResponse.getEntity());

								Log.d("url", url2);
								Log.d("result", result);
								return result;
							}
							return null;
						}
					}
				});
		new Thread(task).start();
		return task.get();
	}

	/**
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public static String postRequest(final String url,
			final Map<String, String> rawParams) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						synchronized (httpClient) {
							String url2 = url;

							// 创建HttpPost对象。
							HttpPost post = new HttpPost(url2);
							// 如果传递参数个数比较多的话可以对传递的参数进行封装
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							for (String key : rawParams.keySet()) {
								// 封装请求参数

								params.add(new BasicNameValuePair(key,
										rawParams.get(key)));

							}
							// 设置请求参数

							post.setEntity(new UrlEncodedFormEntity(params,
									"UTF-8"));
							// 发送POST请求
							HttpResponse httpResponse = httpClient
									.execute(post);
							// 如果服务器成功地返回响应
							if (httpResponse.getStatusLine().getStatusCode() == 200) {
								// 获取服务器响应字符串
								String result = EntityUtils
										.toString(httpResponse.getEntity());

								Log.d("url", url2);
								Log.d("param", rawParams.toString());
								Log.d("result", result);
								return result;
							}
							return null;
						}
					}
				});
		new Thread(task).start();
		return task.get();
	}

}
