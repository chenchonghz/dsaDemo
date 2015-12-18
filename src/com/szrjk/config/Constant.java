package com.szrjk.config;

import com.szrjk.entity.UpdataInfo;
import com.szrjk.entity.UserInfo;

public class Constant
{
	// App淇℃伅
	public static String APP_INFO = "AppInfo";
	// 鏄惁瀵艰埅椤甸潰
	public static final String GUIDE_STATE = "GuideState";
	// 鐢ㄦ埛淇℃伅
	public static String USER_INFO = "UserInfo";
	// 鐢ㄦ埛濮撳悕
	public static String USER_NAME = "UserName";
	//闈炰笟鍔′娇鐢�,
	public static String ERRORMSG = "ERRORMSG";
	// 鐢ㄦ埛ID
	public static String USER_SEQ_ID = "UserSeqId";
	// 鐢ㄦ埛瀵嗙爜
	public static String USER_PWD = "UserPwd";
	// 璁颁綇淇濆瓨瀵嗙爜鐘舵��
	public static String USER_REMEMBER = "UserRemember";
	// 鐢ㄦ埛鐧诲綍鐘舵��
	public static final String LOGIN_STATE = "LoginState";
	// 绉佸瘑
	public static final String PRIVATEKEY = "szrjk2015";
	// 寮�鍙戠幆澧�

//	public static String RQEUEST_URL = "http://192.168.1.71:8888/qry";
	//澶栫綉鐜
//	public static  String RQEUEST_URL = "http://120.236.165.150:8888/qry";
//	 娴嬭瘯鐜
	 public static String RQEUEST_URL = "http://192.168.1.82:8888/qry";
	//UAT鐜
//	public static String RQEUEST_URL = "http://112.74.109.173:8888/qry";
//	public static String RQEUEST_URL = "http://120.25.134.160:1127/qry";



	
//	public static final String RQEUEST_URL = "http://192.168.2.217:8888/qry";

	// public static final String RQEUEST_URL = "http://112.74.126.158:8888/qry";
	public static final String CHANNElKEY = "awfjoi(&%*werfkk52364frwjp4";
	public static final String REQUESTCODE = "0000";
	public static UserInfo userInfo;
	// 鐣岄潰鏋氫妇鍊�
	public static final String ACTIVITYENUM = "ActivityEnum";


	public static final String IMGNUM = "IMGNUM";
	public static final String IMGLIST = "IMGLIST";

	public static final int LOGIN_TYPE_PHONE = 1; // 鎵嬫満
	public static final int LOGIN_TYPE_QQ = 2; // QQ
	public static final int LOGIN_TYPE_WB = 3; // 寰崥
	public static final int LOGIN_TYPE_WX = 4; // 寰俊

	// 鍒濆鑾峰彇甯栧瓙璇锋眰鍙傛暟
	public static final String POST_BEGIN_NUM = "0";
	public static final String POST_END_NUM = "9";
	public static final String USER_BEGIN_NUM = "0";
	public static final String USER_END_NUM = "3";
	
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
	public static final String FORWARD_NUM = "forward_num";
	
	public static final String PCOMMENT_ID="pcomment_id";

	public static final String COMMENT_LEVEL="comment_level";
	
	public static UpdataInfo updataInfo;

	public static int maxCount = 9;

	public static final String PHOTO_BUCKET_FACE = "dd-face";
	public static final String PHOTO_BUCKET_FEED = "dd-feed";
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
	public static final int INDEX_FLAG = 1;
	public static final int SELF_FLAG = 2;
	public static final int OTHER_FLAG = 3;
	public static final int SYSTEM_FLAG = 4;
	public static final int CIRCLE_FLAG = 5;
	public static final int UNKNOW_FLAG = 6;
	
}
