package com.szrjk.dhome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.szrjk.db.DbHelper;
import com.szrjk.db.userDbHelper;
import com.szrjk.entity.*;
import com.umeng.message.PushAgent;

public class DHomeApplication extends Application
{

	public final static String constant_sex_val = "constant_sex_val";

	public final static String constant_hospital = "constant_hospital";

	public final static String constant_dept = "constant_dept";

	public final static String getConstant_professionalTitle = "getConstant_professionalTitle";

	/** 注册时的form **/
	private RegisterInfo registerInfo;

	public static List<Activity> activityList =new ArrayList<Activity>();
	
	public static SelfActivity selfActivity;

	public void finishRegisterActivity(){
		for (Activity activity : activityList) {
			if (activity!=null) {
				activity.finish();
			}
		}
	}


	/** 静态数据 **/
	private Map<String, Map> m = new HashMap<String, Map>();

	public RegisterInfo getRegisterInfo()
	{
		return registerInfo;
	}

	public void setRegisterInfo(RegisterInfo registerInfo)
	{
		this.registerInfo = registerInfo;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		//CrashHandler catchHandler = CrashHandler.getInstance();
		//catchHandler.init(getApplicationContext());
		setRegisterInfo(new RegisterInfo());
		PushAgent.getInstance(this);
		Thread.setDefaultUncaughtExceptionHandler(new UEHandler(this));

		initStaticData();

		Runnable r = new Runnable() {
			@Override
			public void run() {
				initDB();
			}
		};

		new Thread(r).start();


	}

	/**
	 * 初始化静态数据
	 */
	private void initStaticData()
	{
		try
		{
			// 初始化静态数据
			// 男女
			String[] sexstrArr = new String[]
					{
					"男", "女","取消"
					};
			String[] sexvalArr = new String[]
					{
					"1", "2","3"
					};
			setAdapterArr(constant_sex_val, sexstrArr, sexvalArr);

			// 医院
			// String[] hospitalList = new String[] { "北京人民医院", "广州人民医院",
			// "深圳人民医院", "上海人民医院","广西人民医院","广东人民医院","福建人民医院" };
			// String[] hospitalListval = new String[] { "1001", "1002", "1003",
			// "1004","1005","1006","1007" };
			// setAdapterArr(constant_hospital, hospitalList, hospitalListval);

			// 科室
			// String[] deptList = new String[] { "外科 骨科", "内科 肝胆科", "内科",
			// "妇科","儿科","其它","内科 胸科" };
			// String[] deptListval = new String[] { "2001", "2002", "2003",
			// "2004","2005","2006","2007"};
			// setAdapterArr(constant_dept, deptList, deptListval);
			//
			// //TODO 职称
			// String[] professionalTitleList = new String[] {
			// "无职称(待定","主任医师","副主任医师","主治医师","医师","住院医师"};
			// String[] professionalTitleListval = new String[] {
			// "0","1","2","3","4","5"};
			// setAdapterArr(getConstant_professionalTitle,
			// professionalTitleList, professionalTitleListval);

		}
		catch (BusiException e)
		{

		}
	}

	/**
	 * 初始化DB
	 */
	private void initDB()
	{
		// db
		DbHelper.initDb(this);
		userDbHelper.initDb(this);
		final DbUtils db = DbUtils.create(this, AbstractTBEntity.dir,AbstractTBEntity.dbname,AbstractTBEntity.dhdbVersion, new DbUtils.DbUpgradeListener() {

			@Override
			public void onUpgrade(DbUtils db,int oldVersion,int newVersion) 
			{
				if (newVersion>oldVersion) {
					Log.i("TAG", "sql更新成功");
					String DB_PATH = AbstractTBEntity.dir;
					String DB_NAME = AbstractTBEntity.dbname;

					// 检查 SQLite 数据库文件是否存在
					if ((new File(DB_PATH + DB_NAME)).exists() == false) {
						// 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
						File f = new File(DB_PATH);
						// 如 database 目录不存在，新建该目录
						if (!f.exists()) {
							f.mkdir();
						}
					}
					try {
						// 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
						InputStream is = DHomeApplication.this.getBaseContext().getAssets().open(DB_NAME);
						// 输出流
						OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

						// 文件写入
						byte[] buffer = new byte[1024];
						int length;
						while ((length = is.read(buffer)) > 0) {
							os.write(buffer, 0, length);
						}

						// 关闭文件流
						os.flush();
						os.close();
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		final DbUtils userdb = DbUtils.create(this, AbstractUserEntity.dir,AbstractUserEntity.dbname);

		//不需要初始化数据了，这里直接copy过去
		// 每个接口都要有个版本号，用于更新
		int iversion = 6;
		(new TCity()).initTable(db);
		(new THostipal()).initTable(db);
		(new TDept()).initTable(db);
		(new TProfessionalTitle()).initTable(db);
		(new TMessage()).initTable(userdb);
		//
		//		/**************************************** 省市数据 *************************************************/
		//		try
		//		{
		//			(new TCity()).initTable(db, iversion, DHomeApplication.this);
		//		}
		//		catch (Exception e)
		//		{
		//			// 地区的exception TODO,抛错后的做法
		//		}
		//
		//		try
		//		{
		//			(new THostipal()).initTable(db, iversion, DHomeApplication.this);
		//		}
		//		catch (Exception e)
		//		{
		//
		//		}
		//
		//		try
		//		{
		//			(new TDept()).initTable(db, iversion, DHomeApplication.this);
		//		}
		//		catch (Exception e)
		//		{
		//
		//		}
		//
		//		try
		//		{
		//			(new TProfessionalTitle()).initTable(db, iversion, DHomeApplication.this);
		//		}
		//		catch (Exception e)
		//		{
		//
		//		}
	}

	/**
	 * 设置key 对应的 strs 和vals
	 * 
	 * @param key
	 *            自定义
	 */
	public void setAdapterArr(String key, String[] strs, Object[] vals)
			throws BusiException
			{
		if (vals.length != strs.length)
		{
			// /两个长度不一致，这里要抛个错
			throw new BusiException("两个数组的长度不一样");
		}
		Map str_val = new LinkedHashMap();// 这里要有排序
		for (int i = 0; i < vals.length; i++)
		{
			str_val.put(strs[i], vals[i]);
		}
		m.put(key, str_val);
			}

	/**
	 * 得到 arr
	 * 
	 * @param key
	 * @return
	 */
	public String[] getAdapterArr(String key)
	{
		Set<String> keyset = m.get(key).keySet();
		return keyset.toArray(new String[keyset.size()]);
	}

	/**
	 * 判断是否有这个key
	 * 
	 * @param key
	 * @return
	 */
	public boolean haskey(String key)
	{
		return m.get(key) != null;
	}

	/**
	 * 
	 * @param key
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public Object getVal(String key, String str) throws BusiException
	{
		if (m.get(key) == null)
		{
			// 这里要抛个错
			throw new BusiException("没有这个key");
		}
		Map str_val = m.get(key);
		return str_val.get(str);
	}

}
