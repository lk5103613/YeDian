package com.like.network;

public class APIS {
	
	public static final String BASE_URL = "http://121.42.195.28";
	
	public static final String GET_RECRUIT_LIST = BASE_URL + "/index.php/appRecruit/findPageList?currentPage=%1&recruit_type=%2&catname=%3&key=%4//获取已发布信息列表";
	public static final String GET_RECRUIT_DETAIL = BASE_URL + "/index.php/appRecruit/loadRecruit?recruit_id=%1//获取发布信息详细信息";
	public static final String GET_AREA = BASE_URL + "/index.php/Apparea/listArea?cityId=%1//获取区域列表";

}
