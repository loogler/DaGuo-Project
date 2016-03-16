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
	 * 
	 * 
	 * 115.29.224.248 大果服务器
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
	 * push 接口 这里默认只推送一条
	 */
	public static String PUSH = URL
			+ "msgPush/queryMsgPushList?android=1&page=1&rows=1";
	/**
	 * push 用户点击push信息后 向服务器发送统计数据 &id=193ab84d-19c8-4e61-aa82-d6622ec4af9a
	 */
	public static final String PUSH_SUB = URL + "msgPush/addPushNum?android=1";

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
			+ "personInfo/queryPersonInfoList?android=1";

	/**
	 * 查询身边的人（通过批量查询id获取身边的人信息）
	 * 范例：&page=1&rows=13&ids=07a98832-e061-4a5f-b7a1-86d
	 * f0e9fc7ec,0b2f1add-c071-47f3-99c5-bfeaaad4f7ac
	 */
	public static final String QUERY_USERINFO_NEARBY = URL
			+ "personInfo/queryPersonInfoByIds?android=1";

	/**
	 * 商城购物==》抽奖 信息与个人信息关联 -------------------- 范例 &p_id=d510e0bb
	 * -ff40-4def-8919-ee1e1573f0cd&gift_id=3dd7b84d-c1aa-4ae5-a988-f1297e3df6f6
	 */
	public static final String SUBMIT_LOTTERY = URL
			+ "order/saveGift?android=1";

	/**
	 * 用户关注操作 follow_id` varchar(36) DEFAULT NULL COMMENT '关注人ID followed_id`
	 * varchar(36) DEFAULT NULL COMMENT '被关注人ID'
	 */
	public static final String SUBMIT_ATTENTION = URL
			+ "follow/saveOrUpdate?android=1";

	/*------------- 订单 **-----------------**/
	/**
	 * 商品主表订单，所有订单共有接口 pay_type= 50ba2daa-2d35-4c8e-a032-edcc89ad45c1 支付宝id
	 */
	public static final String SUBMIT_ORDER_PUB = URL
			+ "order/saveOrUpdate?android=1";
	/**
	 * 商品订单详细
	 */
	public static final String SUBMIT_GOODS_DETAIL = URL
			+ "goodsDetail/saveOrUpdate?android=1";

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
	 * 查询单个宽带详情 &id=00eb4eae-71ce-47c4-8390-b53aff12948c&page=1&rows=1
	 */
	public static final String QUERY_BROADBAND_DETAIL = URL
			+ "broadband/getBroadbandById?android=1";
	/**
	 * 电话号码查询 所有地区
	 */
	public static final String QUERY_NUMBER = URL
			+ "phoneNumber/queryPhoneNumberListByMobile?android=1";

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
	 * 查询商品分类表，这里的分类是中部有图标的分类，可以后台变动的 ，所有的分类不是这个接口
	 */
	public static final String QUERY_GOOD_TYPE = URL
			+ "goodType/queryGoodTypeList?android=1";

	/**
	 * 查询商品列表 需要传入参数&page= 页码 &rows=每页显示数量 / &type_id=用于区分大类
	 * type_id=a6ad60a7-a587-4216-b83d-54094b05af5b 推荐商品
	 */

	public static final String QUERY_GOODSLIST = URL
			+ "goods/queryGoodsList?android=1";
	/**
	 * 查询该学校下 单个分类所有商品信息 queryGoodsList 参数 type_id school_id
	 */
	public static final String QUERY_GOODSLIST_SCHOOL = URL
			+ "goods/queryBySchoolGoodsList?android=1";

	/**
	 * 查询商品具体信息 &id=372dce67-6f3f-46de-8108-231442f24de6&page=1&rows=1
	 */
	public static final String QUERY_GOODSDETAIL = URL
			+ "goods/getGoodsDetaileById?android=1";

	/**
	 * 查询校园商品分类信息
	 */
	public static final String QUERY_SCHOOL_GOODSTYPE = URL
			+ "schoolGoodType/querySchoolGoodTypeList?android=1";
	/**
	 * 查询校园商品推荐商品信息 school_id 参数
	 */
	public static final String QUERY_SCHOOL_GOODSITEM = URL
			+ "schoolGoods/querySchoolGoodsList?android=1";
	/**
	 * 加入购物车操作 ==>canshu
	 * p_id=d510e0bb-ff40-4def-8919-ee1e1573f0cd&good_id=4378dd43
	 * -c575-4ce1-9af2-45489413cc65
	 * 
	 */
	public static final String SUBMIT_CART_JION = URL
			+ "shoppingCart/saveOrUpdate?android=1";
	/**
	 * 查询购物车列表， 参数&page=1&rows=1&person_id=d510e0bb-ff40-4def-8919-ee1e1573f0cd
	 */
	public static final String QUERY_CART = URL
			+ "shoppingCart/queryShoppingCartList?android=1";

	/**
	 * 查询积分信息商品列表，
	 */
	public static final String QUERY_CENTGOODS = URL
			+ "scoreGood/queryScoreGoodList?android=1";
	/**
	 * 兑换积分操作
	 * 
	 */
	public static final String SUBMIT_CENTGOODS = URL
			+ "scoreChange/saveOrUpdate?android=1";

	/**
	 * 查询优惠卷列表 &page=1&rows=13&p_id=d510e0bb-ff40-4def-8919-ee1e1573f0cd
	 */
	public static final String QUERY_COUPON_LIST = URL
			+ "acceptCoupon/queryCouponList?android=1";

	/**
	 * 提交一个领取优惠卷的请求
	 * &page=1&rows=13&p_id=d510e0bb-ff40-4def-8919-ee1e1573f0cd&a_id=e1ced02d
	 * -42ab-4fc2-8856-61a39b0c3599
	 */
	public static final String SUBMIT_COUPON_APPLY = URL
			+ "acceptCoupon/saveOrUpdate?android=1";

	/**
	 * 查看我的领取过的优惠卷列表 &page=1&rows=13&p_id=d510e0bb-ff40-4def-8919 -ee1e1573f0cd
	 */
	public static final String QUERY_COUPON_MYLIST = URL
			+ "acceptCoupon/queryAcceptCouponList?android=1";

	/****** 领奖 活动************************split line *****************************************************/
	/**
	 * 领奖
	 */
	public static final String QUERY_ACCEPTPRIZE = URL
			+ "acceptPrize/queryAcceptPrizeList?android=1&page=1&rows=10";
	public static final String SUBMIT_AWARDSTATUS = URL
			+ "acceptPrize/saveOrUpdate?android=1";
	// /**
	// * 新闻查询
	// */
	// public static final String QUERY_NEWS = URL
	// +
	// "article/queryArticleList?android=1&menu_id=db94a88d-5c78-448b-a3a7-4af1c3850571";

	/**
	 * 新闻 、社团、活动、专题 都是一个>==查询==<接口方法 参数menu_id可以从tbl_menu表查询，
	 * 参数page为当前页码，rows为当前显示数量
	 */
	public static final String QUERY_EVENT = URL
			+ "article/queryArticleList?android=1";
	/**
	 * 新闻 、社团、活动、专题 的>==查询评论信息==<都是一个接口方法 参数menu_id可以从tbl_menu表查询，
	 * 参数page为当前页码，rows为当前显示数量 a_id为该条信息的评论列表
	 */
	public static final String QUERY_EVENT_DETAIL = URL
			+ "articleFeedback/queryArticleFeedbackList?android=1";

	/**
	 * 新闻社团活动的||评论|| 统一接口该接口不需要menuid &content=内容 &a_id=内容id &p_id=报名者id
	 */
	public static final String SUBMIT_EVENT_FEEDBACK = URL
			+ "articleFeedback/saveOrUpdate?android=1";

	/**
	 * 新闻社团活动的||报名||接口 &table_name业务表（tbl_topic:说说，0：活动
	 * ）&source_id业务ID&type类型（0：报名 1：点赞）&p_id报名，点赞人l
	 */
	public static final String SUBMIT_EVENT_APPLY = URL
			+ "sign/saveOrUpdate?android=1";
	/**
	 * 取消点赞 操作 报名不可取消 &table_name业务表（tbl_topic:说说，0：活动
	 * ）&source_id业务ID&type类型（0：报名 1：点赞）&p_id报名，点赞人l
	 */
	public static final String SUBMIT_EVENT_CANCEL = URL
			+ "sign/delete?android=1";

	/**
	 * ||查询||新闻活动社团是否已在报名列表中 &table_name业务表（tbl_topic:说说，0：活动 ）&source_id业务ID
	 * &type类型（0：报名 1：点赞）&p_id报名，点赞人l
	 */
	public static final String QUERY_EVENT_APPLY = URL
			+ "sign/querySignList?android=1";

	/**
	 * 活动查询
	 */
	public static final String QUERY_HUODONG = URL
			+ "article/queryArticleList?android=1&menu_id=b3b7866c-3bf9-48a7-8caa-effa1fb86782";

	/*------------------------- 说说-----------------*/
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
	 * 熱門說說。。。。。。
	 */
	public static final String QUERY_SHUOSHUO_REMEN = URL
			+ "topic/queryHotTopicList?android=1";

	/**
	 * 说说评论的查询 需要传入t_id参数 以及page rows参数
	 */
	public static final String QUERY_SHUOSHUO_EVA = URL
			+ "topicFeedback/queryTopicFeedbackList?android=1";
	/**
	 * 说说评论的修改提交 需要传入id 和评论者信息
	 */
	public static final String SUBMIT_SHUSHUO_EVA = URL
			+ "topicFeedback/saveOrUpdate?android=1";

	/**
	 * 聊天接口==>查询与对方聊天 的历史内容 此处调用接口必须传对方的和自己的id s_p_id r_p_id 座位参数去查询
	 */
	public static final String QUERY_CHAT_DETAIL = URL
			+ "chatInfo/queryChatInfoList?android=1";
	/**
	 * 查询当前聊天人新发送的消息 范例
	 * &page=1&rows=13&r_p_id=07a98832-e061-4a5f-b7a1-86df0e9fc7ec
	 * &content=44434343&s_p_id=003996d0-33da-4ae6-8ebc-55483a8e8417
	 */
	public static final String QUERY_CHATNOW = URL
			+ "chatInfo/queryUnReadChatInfo?android=1";

	/**
	 * 聊天==》发送一条信息给对方
	 */
	public static final String SUBMIT_CHAT_SEND = URL
			+ "chatInfo/saveOrUpdate?android=1";

	/* ------------------*广告位*--------------- */

	/**
	 * 广告位 post 需要参数为 position =轮播广告类型 page=当前页码 rows=当前页码显示条数
	 */
	public static final String QUERY_ADD_BANNER = URL
			+ "ad/queryAdList?android=1";

	/* -----------------消息中心--------------------------------- */
	/**
	 * 消息==》通知 列表查看
	 */
	public static final String QUERY_MESSAGE_INFORM = URL
			+ "msgCenter/queryMsgCenterList?android=1";
	/**
	 * 消息==》聊天 所有未读聊天记录查看
	 */
	public static final String QUERY_MESSAGE_CHAT = URL
			+ "chatInfo/queryUnReadChatInfoList?android=1";
	/**
	 * 查看完消息后修改消息状态 &id=00797e22-f6d8-493f-864e-284c45da42eb (id是本条消息id)
	 */
	public static final String SUBMIT_MESSAGE_MOD = URL
			+ "msgCenter/updateMsgStatus?android=1";

	/*----------------个人中心------------------------------*/
	/**
	 * 个人中心主页信息查询 我的说说数 关注数
	 */
	public static final String QUERY_MYINFO = URL
			+ "personCenter/queryCountInfo?android=1";

	/**
	 * 个人中心 == 》我的活动 我报名的活动 1）p_id：当前人ID
	 */
	public static final String QUERY_MYHUODONG = URL
			+ "personCenter/queryMyAct?android=1";

	/**
	 * 个人中心== 》 我的活动， 我评论过的活动
	 */
	public static final String QUERY_MYHUODONG_EVA = URL
			+ "personCenter/queryLikeAct?android=1";
	/**
	 * 个人中心 ==》 我的点赞 我点赞过的说说
	 */
	public static final String QUERY_MYDIANZAN = URL
			+ "personCenter/queryTopicGoods?android=1";

	/**
	 * 个人中心 ==》我的报名 我报名过的社团
	 */
	public static final String QUERY_MYBAOMING = URL
			+ "personCenter/querySignParty?android=1";
	/**
	 * 个人中心===》查询我的关注 （关注/被关注） 范例测试
	 * &page=1&rows=1&follow_id=07a98832-e061-4a5f-b7a1-86df0e9fc7ec
	 */
	public static final String QUERY_ATTENTION = URL
			+ "follow/queryFollowList?android=1";

	/**
	 * 个人中心===》 查询我的订单 范例测试 ：
	 * &page=1&rows=13&p_id=07a98832-e061-4a5f-b7a1-86df0e9fc7ec
	 * ---------------未付款 pay_status=0 已付款 ：pay_status=1 ；；；status=1查询处理情况
	 */
	public static final String QUERY_MYORDER = URL
			+ "personCenter/queryMyOrder?android=1";

	/**
	 * 个人中心==》 查询我的新手任务 。 canshu
	 * &page=1&rows=1&p_id=8ce10832-e592-4148-9e52-8ac3df4c421b
	 */
	public static final String QUERY_MYCENT = URL
			+ "noviceTask/queryNoviceTaskList?android=1";
	/**
	 * 个人中心===》 完成新手任务，提交至服务器加分 &p_id
	 * =d510e0bb-ff40-4def-8919-ee1e1573f0cd&type_id=
	 * a3306d73-6087-4c64-acdd-2977f33f3b7d
	 */
	public static final String SUBMIT_MYCENT = URL
			+ "noviceTask/saveOrUpdate?android=1";

	/** -----------------app下载--------------------------------* */
	/**
	 * app列表
	 */
	public static final String QUERY_APP_DOWNLOAD = URL
			+ "softInfo/querySoftInfoListByMobile?android=1";

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
