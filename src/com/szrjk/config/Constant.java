package com.szrjk.config;

import com.szrjk.entity.UpdataInfo;
import com.szrjk.entity.UserInfo;

public class Constant
{

	// App信息
	public static String APP_INFO = "AppInfo";
	// 是否导航页面
	public static final String GUIDE_STATE = "GuideState";
	// 用户信息
	public static String USER_INFO = "UserInfo";
	// 用户姓名
	public static String USER_NAME = "UserName";
	//非业务使用,
	public static String ERRORMSG = "ERRORMSG";
	// 用户ID
	public static String USER_SEQ_ID = "UserSeqId";
	// 用户密码
	public static String USER_PWD = "UserPwd";
	// 记住保存密码状态
	public static String USER_REMEMBER = "UserRemember";
	// 用户登录状态
	public static final String LOGIN_STATE = "LoginState";
	// 私密
	public static final String PRIVATEKEY = "szrjk2015";
	// 开发环境

//	public static String RQEUEST_URL = "http://192.168.1.71:8888/qry";
	//外网环境
//	public static  String RQEUEST_URL = "http://120.236.165.150:8888/qry";
//	 测试环境
	 public static String RQEUEST_URL = "http://192.168.1.82:8888/qry";
	//UAT环境
//	public static String RQEUEST_URL = "http://112.74.109.173:8888/qry";
//	public static String RQEUEST_URL = "http://120.25.134.160:1127/qry";



	
//	public static final String RQEUEST_URL = "http://192.168.2.217:8888/qry";

	// public static final String RQEUEST_URL = "http://112.74.126.158:8888/qry";
	public static final String CHANNElKEY = "awfjoi(&%*werfkk52364frwjp4";
	public static final String REQUESTCODE = "0000";
	public static UserInfo userInfo;
<<<<<<< HEAD
	// 鐣岄潰鏋氫妇鍊�
=======
	// 界面枚举值
>>>>>>> 86d3878c8658967c04f04d948751cc57b33620ca
	public static final String ACTIVITYENUM = "ActivityEnum";


	public static final String IMGNUM = "IMGNUM";
	public static final String IMGLIST = "IMGLIST";

<<<<<<< HEAD
	public static final int LOGIN_TYPE_PHONE = 1; // 鎵嬫満
	public static final int LOGIN_TYPE_QQ = 2; // QQ
	public static final int LOGIN_TYPE_WB = 3; // 寰崥
	public static final int LOGIN_TYPE_WX = 4; // 寰俊

	// 鍒濆鑾峰彇甯栧瓙璇锋眰鍙傛暟
=======
	public static final int LOGIN_TYPE_PHONE = 1; // 手机
	public static final int LOGIN_TYPE_QQ = 2; // QQ
	public static final int LOGIN_TYPE_WB = 3; // 微博
	public static final int LOGIN_TYPE_WX = 4; // 微信

	// 初始获取帖子请求参数
>>>>>>> 86d3878c8658967c04f04d948751cc57b33620ca
	public static final String POST_BEGIN_NUM = "0";
	public static final String POST_END_NUM = "9";
	public static final String USER_BEGIN_NUM = "0";
	public static final String USER_END_NUM = "3";
	
<<<<<<< HEAD
	//甯栧瓙鏇存柊
	public static final int NOTIFY_DATA_SET_CHANGE=5;

	// 甯栧瓙鍒嗙被
	public static final String NORMAL_POST = "101"; // 鏅�氬笘瀛�
	public static final String CASE_SHARE = "102"; // 鐥呬緥鍒嗕韩
	public static final String PROBLEM_HELP = "103"; // 鐤戦毦姹傚姪
	public static final String CIRCLE_POST = "104"; // 鍦堝唴甯栧瓙
	public static final String DATE_POST = "105"; // 鏃ョ▼甯栧瓙
	public static final String TRANSMIT_POST = "202";//杞彂甯栧瓙
	public static final String RECOMMEND_USER = "106";//鎺ㄨ崘鐢ㄦ埛
	public static final String TRANSMIT_POST2 = "204";//杞彂甯栧瓙
	
	//璧勮鍒嗙被
	public static final String NEW_HOT = "RD";  //鐑偣銆佹渶鏂拌祫璁�

	// 鍒锋柊甯栧瓙鏍囪
	public static final int HAVE_NEW_POST = 0; // 鏈夋渶鏂板笘瀛�
	public static final int HAVE_NEW_POST_BY_REFRESH = 2; //涓嬫媺鍒锋柊
	public static final int NOT_NEW_POST = 1; // 娌℃湁鏈�鏂板笘瀛�
	public static final int HAVE_NEW_GG = 4;  //鏈夋渶鏂板叕鍛�
	
	public static final int HAVE_NEW_TYPE = 1;   //鏈夋柊璧勮绫诲瀷

	// 浼犻�掑笘瀛恑d鏍囪
	public static final String POST_ID = "post_id";
	//浼犻�掔敤鎴峰ご鍍弖rl鏍囪
	public static final String PIC_URL = "pic_url";
	//浼犻�掑笘瀛愭鏂囨爣璁�
	public static final String POST_TEXT = "post_text";
	//浼犻�掑笘瀛愮被鍨嬫爣璁�
	public static final String POST_TYPE = "post_type";
	//浼犻�掑笘瀛恖evel鏍囪
	public static final String POST_LEVEL = "post_level";
	//浼犻�抯rcPostId鏍囪
	public static final String SRC_POST_ID = "src_post_id";
	//浼犻�抪PostId鏍囪
	public static final String P_POST_ID = "p_post_id";
	//浼犻�掍綅缃殑鏍囪
	public static final String POSITION = "position";
	//浼犻�掕浆鍙戞暟閲忔爣璁�
=======
	//帖子更新
	public static final int NOTIFY_DATA_SET_CHANGE=5;

	// 帖子分类
	public static final String NORMAL_POST = "101"; // 普通帖子
	public static final String CASE_SHARE = "102"; // 病例分享
	public static final String PROBLEM_HELP = "103"; // 疑难求助
	public static final String CIRCLE_POST = "104"; // 圈内帖子
	public static final String DATE_POST = "105"; // 日程帖子
	public static final String TRANSMIT_POST = "202";//转发帖子
	public static final String RECOMMEND_USER = "106";//推荐用户
	public static final String TRANSMIT_POST2 = "204";//转发帖子
	
	//资讯分类
	public static final String NEW_HOT = "RD";  //热点、最新资讯

	// 刷新帖子标记
	public static final int HAVE_NEW_POST = 0; // 有最新帖子
	public static final int HAVE_NEW_POST_BY_REFRESH = 2; //下拉刷新
	public static final int NOT_NEW_POST = 1; // 没有最新帖子
	public static final int HAVE_NEW_GG = 4;  //有最新公告
	
	public static final int HAVE_NEW_TYPE = 1;   //有新资讯类型

	// 传递帖子id标记
	public static final String POST_ID = "post_id";
	//传递用户头像url标记
	public static final String PIC_URL = "pic_url";
	//传递帖子正文标记
	public static final String POST_TEXT = "post_text";
	//传递帖子类型标记
	public static final String POST_TYPE = "post_type";
	//传递帖子level标记
	public static final String POST_LEVEL = "post_level";
	//传递srcPostId标记
	public static final String SRC_POST_ID = "src_post_id";
	//传递pPostId标记
	public static final String P_POST_ID = "p_post_id";
	//传递位置的标记
	public static final String POSITION = "position";
	//传递转发数量标记
>>>>>>> 86d3878c8658967c04f04d948751cc57b33620ca
	public static final String FORWARD_NUM = "forward_num";
	
	public static final String PCOMMENT_ID="pcomment_id";

	public static final String COMMENT_LEVEL="comment_level";
	
	public static UpdataInfo updataInfo;

	public static int maxCount = 9;

	public static final String PHOTO_BUCKET_FACE = "dd-face";
	public static final String PHOTO_BUCKET_FEED = "dd-feed";
<<<<<<< HEAD
	// 甯栧瓙鍥剧墖灏哄
	public static final String FEED_DEAL_SIZE = "230x230,350x350,750x1050,360x160";
	// 澶村儚灏哄
	public static final String FACE_DEAL_SIZE = "80x80,144x144,210x210,750x750";
	// 鑳屾櫙灏哄
	public static final String BG_DEAL_SIZE = "750x618,750x750,170x170";
	// 骞垮憡灏哄
	public static final String AD_DEAL_SIZE = "750x490,710x439,170x170";
	// 灏侀潰灏哄
	public static final String COVER_DEAL_SIZE = "710x439";
	//浼犲叆鍥剧墖鏍囪
	//涓汉澶村儚鍥剧墖
	public static final String PICTURE_FACE_CODE = "200";
	//鍏朵粬灞曠ず鍥剧墖
	public static final String PICTURE_OTHER_CODE = "201";
	
	public static final String COMMENT_SUCCESS = "result_ok";
	//璇勮绫诲瀷
	public static final String COMMENT_TYPE = "203";
	// 璇勮缁撴灉
	public static final int COMMENT_RESULTCODE = 204;
	//杞彂缁撴灉
	public static final int FORWARD_RESULTCODE = 205;
	//甯栧瓙璇︽儏杞彂缁撴灉
	public static final int POSTDETAIL_RESULTCODE = 206;
	//淇敼绠�浠嬩俊鎭�
	public static final String EDITINFO = "editinfo";
	//浼犻�掑湀瀛�
	public static final String CIRCLE ="circle";
	//浼犻�掔煡璇嗗簱id
	public static final String Library ="categoryId";
	//鐤戦毦淇濆瓨鑽夌key
	public static final String SAVEPUZZ ="savepuzz";
	//鏄惁淇濆瓨鑽夌puzz
	public static final String ISAVEPUZZ ="isavepuzz";
	
	//鑾峰彇褰撳墠绯荤粺鐗堟湰鍙�
	public static final int CURRENTAPIVERSION=android.os.Build.VERSION.SDK_INT;
	
	//灞忓箷瀹�
	public static int screenWidth ;
	
	//鏄惁淇濆瓨鑽夌puzz
	public static final String ISAVECASE ="isavecase";
	
	//鍒嗕韩淇濆瓨鑽夌key
	public static final String SAVECASE ="savecase";
	
	//甯栧瓙鏍囪
=======
	// 帖子图片尺寸
	public static final String FEED_DEAL_SIZE = "230x230,350x350,750x1050,360x160";
	// 头像尺寸
	public static final String FACE_DEAL_SIZE = "80x80,144x144,210x210,750x750";
	// 背景尺寸
	public static final String BG_DEAL_SIZE = "750x618,750x750,170x170";
	// 广告尺寸
	public static final String AD_DEAL_SIZE = "750x490,710x439,170x170";
	// 封面尺寸
	public static final String COVER_DEAL_SIZE = "710x439";
	//传入图片标记
	//个人头像图片
	public static final String PICTURE_FACE_CODE = "200";
	//其他展示图片
	public static final String PICTURE_OTHER_CODE = "201";
	
	public static final String COMMENT_SUCCESS = "result_ok";
	//评论类型
	public static final String COMMENT_TYPE = "203";
	// 评论结果
	public static final int COMMENT_RESULTCODE = 204;
	//转发结果
	public static final int FORWARD_RESULTCODE = 205;
	//帖子详情转发结果
	public static final int POSTDETAIL_RESULTCODE = 206;
	//修改简介信息
	public static final String EDITINFO = "editinfo";
	//传递圈子
	public static final String CIRCLE ="circle";
	//传递知识库id
	public static final String Library ="categoryId";
	//疑难保存草稿key
	public static final String SAVEPUZZ ="savepuzz";
	//是否保存草稿puzz
	public static final String ISAVEPUZZ ="isavepuzz";
	
	//获取当前系统版本号
	public static final int CURRENTAPIVERSION=android.os.Build.VERSION.SDK_INT;
	
	//屏幕宽
	public static int screenWidth ;
	
	//是否保存草稿puzz
	public static final String ISAVECASE ="isavecase";
	
	//分享保存草稿key
	public static final String SAVECASE ="savecase";
	
	//帖子标记
>>>>>>> 86d3878c8658967c04f04d948751cc57b33620ca
	public static final int INDEX_FLAG = 1;
	public static final int SELF_FLAG = 2;
	public static final int OTHER_FLAG = 3;
	public static final int SYSTEM_FLAG = 4;
	public static final int CIRCLE_FLAG = 5;
	public static final int UNKNOW_FLAG = 6;
	
}
