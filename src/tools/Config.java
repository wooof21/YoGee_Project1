package tools;

public class Config{

	//public static String BASE_URL = "http://192.168.1.120:8080/app/";
	public static String BASE_URL = "http://123.57.15.173:8089/app/";
	 //public static String BASE_URL = "http://192.168.1.118:8080/app/";//zhanghao

	public static String test = "version";

	/**
	 * 主页广告url
	 */
	public static String MAIN_AD_URL = "guanggao";

	public static int notificationNum = 0;

	/**
	 * 6.2. 我要抢单主页面
	 * 
	 * @param 客户端请求数据
	 * @param 如果
	 *            type 等于1 时 完成方式，订单类型，超时赔付，在线支付，多选
	 * @param 如果
	 *            type 等于2 时 1,距离，2出价最高，3最新发布，4正在拼 单选
	 * @param 如果筛选类型选择距离lat
	 *            lnt 为坐标 不然为0，
	 */
	public static String MAIN_GRAB_URL = "orderList";

	/**
	 * 6.1.4. 登陆接口
	 */
	public static String LOGIN_URL = "userLogin";

	/**
	 * 6.1.1. 用户注册接口
	 */
	public static String SAVE_PERSONAL_INFO_URL = "userRegistration";

	/**
	 * 6.2 第三方登陆
	 */
	public static String OTHER_LOGIN = "otherLogin";

	/**
	 * 6.5. 我要发单填写订单
	 */
	public static String POST_ORDER_URL = "releaseOrder";

	/**
	 * 6.4. 抢单详情
	 */
	public static String ORDER_DETAIL_GRAB_URL = "GrabOrder";

	/**
	 * 6.5. 报价详情
	 */
	public static String ORDER_DETAIL_BID_URL = "quoteDetails";

	/**
	 * 6.7. 用户抢单
	 */
	public static String USER_GRAB_URL = "userQute";

	/**
	 * 6.8. 用户报价
	 */
	public static String USER_TO_BID_URL = "userQuote";

	/**
	 * 6.9. 用户取消报价
	 */
	public static String USER_TO_BID_CANCEL_URL = "cancelQuote";

	/**
	 * 6.10. 订单-我的抢单列表
	 */
	public static String MY_ORDER_LIST_GRAB_URL = "grabOrderList";

	/**
	 * 6.11. 订单-进行中
	 */
	public static String MY_ORDER_LIST_INPROCESS_URL = "inOrderList";

	/**
	 * 6.12. 订单-我的发单
	 */
	public static String MY_ORDER_LIST_POST_URL = "releaseOrderList";

	/**
	 * 6.14. 得到用户个人信息
	 */
	public static String GET_USER_INFO_URL = "getUserInfo";

	/**
	 * 6.21. 查看用户地址信息
	 */
	public static String FIND_USER_ADDRESS = "findUserAddress";

	/**
	 * 6.15. 修改个人头像
	 */
	public static String UPDATE_USER_HEAD_URL = "updateUserPortrait";

	/**
	 * 6.14. 修改个人信息
	 */
	public static String UPDATE_USER_INFO_URL = "updateUser";

	/**
	 * 6.22. 查看简历
	 */
	public static String FIND_USER_RESUME_URL = "findUserResume";

	/**
	 * 6.16. 修改用户地址信息
	 */
	public static String CHANGE_USER_ADDRESS_URL = "updateUserAddress";

	/**
	 * 6.19. 选择默认地址
	 */
	public static String SELECT_USER_ADDRESS_URL = "seleteUserAddress";

	/**
	 * 6.18. 删除用户地址信息
	 */
	public static String DELETE_USER_ADDRESS_URL = "deleteUserAddress";
	/**
	 * 6.22. 客户端请求数据
	 */
	public static String UPDATE_USER_RESUME_URL = "updateUserResume";
	/**
	 * 6.24. 版本更新
	 */
	public static String UPDATE_VERSION = "version";

	/**
	 * 6.28. 用户修改密码
	 */
	public static String UPDATE_PASSWORD = "updatePassword";

	/**
	 * 6.13. 发单人选中报价人
	 */
	public static String SELECT_BIDER_URL = "selectedQuotation";

	/**
	 * 6.23. 意见反馈
	 */
	public static String FEED_BACK = "feedback";

	/**
	 * 6.14. 发单人取消报价人
	 */
	public static String DELETE_BIDER_URL = "cancelQuotation";

	/**
	 * 6.16. 进行中的状态提交
	 */
	public static String STATE_UPDATE_URL = "userOrderState";

	/**
	 * 6.34. 评价
	 */
	public static String COMMENT_URL = "comment";

	/**
	 * 6.25.查看别人的个人信息
	 */
	public static String OTHERS_USER_INFO = "othersUserInfo";

	/**
	 * 6.36. 消息
	 */
	public static String MESSAGE_URL = "news";

	/**
	 * 6.33. 绑定别名
	 */
	public static String BIND_ALIAS = "bindAlias";
	/**
	 * 6.34. 解除绑定别名
	 */
	public static String UN_BIND_ALIAS = "unBindAlias";

	/**
	 * 6.38. 催单
	 */
	public static String REMINDER_URL = "reminder";

	/**
	 * 6.39. 退单
	 */
	public static String REFUND_URL = "refund";

	/**
	 * 6.39. 退单
	 */
	public static String PAY_URL = "pay";

	/**
	 * 6.40. 坐标提交
	 */
	public static String UPDATE_LATLON_URL = "coordinate";

	/**
	 * 6.39. 删除消息
	 */
	public static String NOTIFICATION_DELETE_URL = "detleteNews";
	/**
	 * 6.32. 积分商城商品详情
	 */
	public static String SHANGPIN_DETAILS = "shangpinDetails";
	/**
	 * 6.44. 个人认证列表
	 */
	public static String AUTH_ENTICATION_LIST = "authenticationList";
	/**
	 * 6.45. 个人认证
	 */
	public static String AUTH_ENTICATION = "authentication";
	/**
	 * 6.46. 更新个人认证
	 */
	public static String UPDATE_AUTH_ENTICATION = "newUpdateAuthentication";
	/**
	 * 6.47. 绑定
	 */
	public static String BINDING_LIST = "bindingList";
	/**
	 * 6.48. 绑定
	 */
	public static String BINDING = "binding";
	/**
	 * 6.49. 解除绑定
	 */
	public static String REMOVE_BINDING = "removebinding";
	/**
	 * 6.50. 交易记录
	 */
	public static String ORDER_RECORDS = "orderRecords";
	/**
	 * 6.51.提现
	 */
	public static String WITH_DRAWALS = "withdrawals";

	/**
	 * 6.47. 发现
	 */
	public static String DISCOVER_MAIN = "faxian";

	/**
	 * 6.45. 积分商城主页面
	 */
	public static String CREDIT_MALL_MAIN = "zhuyemian";

	/**
	 * 6.43. 启动引导页
	 */
	public static String GUIDE_PAGE = "guidepage";
	/**
	 * 6.1. 登陆注册认证码接口
	 */
	public static String USER_CHECK = "userCheck";

	/**
	 * 6.56. 订单
	 */
	public static String ORDER_LIST_URL = "order";

	/**
	 * 6.51. 类型商品列表
	 */
	public static String CREDIT_MALL_CATEGORY_URL = "typemall";
	/**
	 * 6.59. 订单举报
	 */
	public static String REPORT = "report";

	/**
	 * 6.31. 积分商城商品详情
	 */
	public static String CREDIT_MALL_GOODS_DETAIL_URL = "shangpinDetails";

	/**
	 * 6.55. 积分商城商品兑换记录
	 */
	public static String CREDIT_MALL_EXCHANGE_RECORD_LIST_URL = "conversion";

	/**
	 * 6.54. 积分商城兑换
	 */
	public static String CREDIT_MALL_EXCHANGE_URL = "exchange";

	/**
	 * 6.56. 积分商城商品兑换记录详情
	 */
	public static String CREDIT_MALL_EXCHANGE_LIST_DETAIL_URL = "duihuandetails";

	/**
	 * 6.57. 积分商城商品兑换详情
	 */
	public static String CREDIT_MALL_EXCHANGE_POPUP_DETAIL_URL = "exchangedetail";
	
	/**
	 *6.60.	三公里类型
	 */
	public static String CONVENIENCE_FACILITY_FILTER_URL = "peripheryType";
	
	/**
	 *6.61.	三公里数据
	 */
	public static String CONVENIENCE_FACILITY_FILTER_LIST_URL = "periphery";
	
	/**
	 *6.62.	活动
	 */
	public static String ORDER_ACTIVITY_LIST = "orderActivityList";

	
	/**
	 *6.63.	抢单人抢单，雇主取消
	 */
	public static String CANCEL_ORDER_GRABER_URL = "deleteQute";
	
	/**
	 *6.64.	提交坐标
	 */
	public static String SUBMIT_GPS_COORDINATE_URL = "SubmitCoordinate";

	/**
	 *6.64.	找事头条
	 */
	public static String TOP_LINE = "topLine";


}
