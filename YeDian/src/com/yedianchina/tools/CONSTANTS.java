package com.yedianchina.tools;

public class CONSTANTS {
	
	private CONSTANTS() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
	
	public static final String  YEDIANCHINA_USER_INFO="yedianchina_user_info";
	
	
	public static final String HOST="http://121.42.195.28/";   //服务器 IP
	//public static final String HOST="http://192.168.0.107/";   //本地IP
	
	public static final String SAVE_SUGGEST=HOST+"index.php/appSuggest/saveNewSuggest";
	
	public static final String SAVE_JOB=HOST+"index.php/AppJobs/saveJob";//保存发布的招聘
	
	
	public static final String UPDATE_USER=HOST+"index.php/AppUser/updateUserFn";//修改名片
	
	
	public static final String LOAD_LOGINUSER=HOST+"index.php/AppLoginUser/loadUserInfo?uid=";
	
	
	
	public static final String ATTENTION_MERCHANT_PAGE=HOST+"index.php/appAttention/findAttentionMerchantList?currentPage=";//
	
	
	public static final String saveComment=HOST+"index.php/AppComment/saveComment";//保存评论
	
	
	public static final String FANS_PAGE=HOST+"index.php/AppFans/findFansList?currentPage=";//我的粉丝分页
	
	//保存二手设备 等
	public static final String SAVE_ERSHOUSHEBEI=HOST+"index.php/AppErshoushebei/saveErshoushebei";//保存发布的二手设备
	
	
	public static final String SAVE_RECRUIT=HOST+"index.php/AppRecruit/saveRecruit";//保存发布的招聘
	
	public static final String UPDATE_MINGPIAN=HOST+"index.php/AppLoginUser/updateMingPian";//修改名片
	
	public static final String ERSHOUSHEBEI_PAGE=HOST+"index.php/AppErshoushebei/findPageList?currentPage=";//二手设备
	
	public static final String  CHAT_PAGE=HOST+"index.php/AppChat/findPageList?currentPage=";//聊天主页
	
	
	public static final String GROUP_DETAIL=HOST+"index.php/AppGroup/loadGroup?group_id=";//加载群组详情
	
	
	public static final String ERSHOUSHEBEI_DETAIL_URL=HOST+"index.php/AppErshoushebei/loadErshoushebeiDetail?id=";//加载求职详情
	
	public static final String NEARBY_PAIDUI_PAGE=HOST+"index.php/AppPaidui/findPageList?currentPage=";//派对分页
	
	
	public static final String PAIDUI_DETAIL=HOST+"index.php/AppPaidui/loadDetail?id=";//派对详情08-04
	
	
	public static final String SAVE_BAOMING=HOST+"index.php/AppBaoming/saveBaoming";//保存报名信息
	public static final String SAVE_ACTIVITY=HOST+"index.php/AppActivity/saveActivity";//保存发布的活动
	
	
	public static final String SAVE_PAIDUI=HOST+"index.php/AppPaidui/savePaidui";//保存发布的活动
	
	
	
	public static final String NEARBY_ACTIVITY_PAGE=HOST+"index.php/AppActivity/findListPage?currentPage=";//附近活动
	public static final String ACTIVITY_DETAIL=HOST+"index.php/AppActivity/loadDetail?act_id=";//附近活动详情
	
	
	public static final String GROUP_LIST=HOST+"index.php/AppGroup/findPageList?currentPage=";//群组分页
	
	public static final String FAV_RECRUIT_DETAIL_URL=HOST+"index.php/AppFav/saveFavRecruit?recruit_id=";//商家收藏用户求职帖子
	
	//更新用户登陆的时间
	public static final String UPDATE_LAST_LOGIN=HOST+"index.php/AppLoginUser/updateLastLogin";
	 
	public static final String UPDATE_PWD=HOST+"index.php/AppLoginUser/updatePwd";
	
	
	public static final String UPDATE_BAIDUUID=HOST+"index.php/AppLoginUser/updateBaiDuUID";
	
	public static final String LIST_AREA=HOST+"index.php/Apparea/listArea?cityId=";//加载区域
	public static final String RECRUIT_PAGE=HOST+"index.php/appRecruit/findPageList?currentPage=";//招聘
	public static final String RECRUIT_DETAIL=HOST+"index.php/appRecruit/loadRecruit?recruit_id=";//招聘
	
	public static final String MERCHANT_DETAIL=HOST+"index.php/appMerchant/loadMerchantInfo?merchantId=";//商家
	
	public static final String NEARBY_MERCHANT_PAGE=HOST+"index.php/AppNearby/getNearByMerchantList?currentPage=";//
	public static final String MERCHANT_RANK_PAGE=HOST+"index.php/AppMerchantRank/findPageList?currentPage=";//招聘
	
	public static final String JOBS_PAGE=HOST+"index.php/AppJobs/findPageList?currentPage=";//求职
	public static final String JOBS_DETAIL_URL=HOST+"index.php/AppJobs/loadJobDetail?id=";//求职详情
	
	public static final String FAV_JOBS_DETAIL_URL=HOST+"index.php/AppFav/saveFavJob?";//商家收藏用户求职帖子
	
	public static final String USERNAME="u";
	
	public static final String SELECT_CITY="select_city";
	public static final String UID="uid";
	public static final String USERTYPE="userType";
	public static final String PWD="pwd";
	public static final String NICKNAME="nickname";//昵称
	
	
	public static final String YD_APPID="1u7M20h7e";//夜店中国appid
	

	public static final String token="";
	public static final String appid="";
	
	//附近模块
	public static final String NEARBY_USER_DYNAMIC=HOST+"index.php/AppUser/loadUserInfo?uid=";
	
	
	public static final String NEARBY_USER_DYNAMIC_PAGE=HOST+"index.php/AppUser/findUserDynamicPage?uid=";
	public static final String NEARBY_USER_DYNAMIC_DETAIL=HOST+"index.php/AppUser/loadUserDynamicDetail?dynamic_id=";
	
	//动态评论
	public static final String NEARBY_USER_DYNAMIC_COMMENT=HOST+"index.php/appComment/findDynamicCommentPage?dynamic_id=";
	
	
	public static final String NEARBY_USER_PAGE=HOST+"index.php/AppUser/findNearByUserList?currentPage=";
	
	public static final String NEARBY_USER_Map=HOST+"index.php/AppUser/findNearByUserMap";
	
	public static final String NEARBY_USER_BOTTOM_GZ=HOST+"index.php/AppNearby/nearbyBottomGz";
	
	
	 
	public static final String IMG_HOST=HOST;
	public static final String TEL400=HOST+"index.php/AppConfig/get400Tel";
	
	
	
	public static final String MY_ORDER_LIST=HOST+"index.php/appOrder/findMyOrderList?mp=";
	
	
	public static final String LOAD_ORDER_DETAIL=HOST+"index.php/appOrder/loadMyOrder?id=";
	//用户注册登录
	public static final String REG_MP=HOST+"index.php/AppLoginUser/regMp";
	public static final String REG_EMAIL=HOST+"index.php/AppLoginUser/regEmail";
	
	
	public static final String LOGIN=HOST+"index.php/AppLoginUser/login";
	
	//附近的司机 列表
	public static final String NEARBY_DRIVER_URL=HOST+"index.php/appDriver/getNearByDriverList2";
	//
	public static final String MERCHANT_DETAIL_URL=HOST+"index.php/appMerchant/loadMerchantInfo?merchantId=";
	
	//发送验证码
	public static final String SMS_SEND_CODE=IMG_HOST+"send_mp_code.php";
	//保存订单
	public static final String SAVE_ORDER=IMG_HOST+"index.php/appOrder/saveOrder";
	public static final String SAVE_CALL_MP_ORDER=IMG_HOST+"index.php/appOrder/saveCallMpOrder";
	//城市列表
	public static final String CITY_LIST=HOST+"index.php/appCity/findCityList";
	//城市费率
	public static final String FEE_LIST=HOST+"index.php/appFee/findNewFeeList?cityId=";
	
	
	public static final String SERVER_VERSION_URL=HOST+"index.php/appVersion/getLatestVersion";
	//司机评论
	public static final String MERCHANT_COMMENT_URL=HOST+"index.php/appComment/findMerchantCommentPage?merchantId=";
	
	
	
	public static final String MY_RECHARGE_LIST=HOST+"index.php/appRecharge/findMyLogList?mp=";
	
	public static final String SAVE_FEEDBACK=HOST+"index.php/appSuggest/saveFeedBack";
	
	public static final String LOAD_USER_INFO=HOST+"index.php/appUser/loadUserInfo";
	
	
	public static final String UPDATE_MP_STATUS=HOST+"index.php/appUser/updateMpStatusFn";
	//评价司机
	public static final String SAVE_COMMENT=HOST+"index.php/appComment/saveComment";
	//客户消费 11-09 14:24
	public static final String MY_TRANSACTION_LIST=HOST+"index.php/AppTransaction/findMyLogList?mp=";
	

}
