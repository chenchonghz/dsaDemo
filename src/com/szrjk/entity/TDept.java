package com.szrjk.entity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.SharePerferenceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * denggm on 2015/10/21. DHome
 */
@Table(name = "TDEPT")
public class TDept extends AbstractTBEntity<TDept>
{

	private final static String TAG = "TDept";

	@Id(column = "subdeptid")
	private String subDeptId;

	@Column(column = "deptName")
	private String deptName;

	@Column(column = "deptId")
	private String deptId;

	@Column(column = "subDeptName")
	private String subDeptName;

	public String getSubDeptId()
	{
		return subDeptId;
	}

	public void setSubDeptId(String subDeptId)
	{
		this.subDeptId = subDeptId;
	}

	public String getDeptName()
	{
		return deptName;
	}

	public void setDeptName(String deptName)
	{
		this.deptName = deptName;
	}

	public String getDeptId()
	{
		return deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public String getSubDeptName()
	{
		return subDeptName;
	}

	public void setSubDeptName(String subDeptName)
	{
		this.subDeptName = subDeptName;
	}

	@Override
	public String getCURRENT_VERSION() {
		return "CURRENT_VERSION_TDEPT";
	}

	@Override
	public String getTAG() {
		return TAG;
	}

	@Override
	public String getTableId() {
		return "department";
	}

	@Override
	public Class gettheclass() {
		return TDept.class;
	}

	/**
	 * 初始表数据，即把接口数据 入到本地sqlite
	 * 
	 * @param db
	 * @param iversion
	 		*            版本号
			*/
			public  void initTable(final DbUtils db)
			{

				super.initTable(db);

//		int currentVersion = SharePerferenceUtil.getInstance(context,
//				Constant.APP_INFO).getIntValue(CURRENT_VERSION);
//		Log.e(TAG, currentVersion + ".." + iversion);
//		if (currentVersion >= iversion)
//		{
//			// 现在的版本高,不理
//			// ToastUtils.showMessage(c,"自己的版本高!!!");
//			Log.e(TAG, "自己的版本高!!!");
//			return;
//		}
//
//		HashMap<String, Object> busiParams = new HashMap<String, Object>();
//		busiParams.put("tableId", "department");
//
//		HashMap<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("ServiceName", "queryTableData");
//		paramMap.put("BusiParams", busiParams);
//		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack()
//		{
//			@Override
//			public void start()
//			{
//			}
//
//			@Override
//			public void loading(long total, long current, boolean isUploading)
//			{
//			}
//
//			@Override
//			public void failure(HttpException exception, JSONObject jsonObject)
//			{
//			}
//
//			@Override
//			public void success(final JSONObject jsonObject)
//			{
//
//				//开多线程处理
//				Runnable r = new Runnable() {
//					@Override
//					public void run() {
//						dosuccess(jsonObject);
//					}
//				};
//				new Thread(r).start();
//			}
//
//
//			public void dosuccess(JSONObject jsonObject)
//			{
//				try
//				{
//					JSONObject errorObj = jsonObject.getJSONObject("ErrorInfo");
//					JSONObject returnObj = jsonObject
//							.getJSONObject("ReturnInfo");
//					List resultlist = JSON.parseArray(
//							returnObj.getString("ListOut"), TDept.class);
//					if (resultlist != null && !returnObj.isEmpty())
//					{
//						db.dropTable(TDept.class);
//						Log.e(TAG, "dropTABLE TDept!!!");
//						SharePerferenceUtil.getInstance(context,
//								Constant.APP_INFO).setIntValue(CURRENT_VERSION,
//								iversion);
//					}
//					else
//					{
//						// 结果为空，则
//						return;
//					}
//
//					for (int i = 0; i < resultlist.size(); i++)
//					{
//						TDept tDept = (TDept) resultlist.get(i);
//						db.save(tDept);// 入sqlite
//					}
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		});
	}


	/**
	 * 名对应的 id
	 * 
	 * @param pactivity
	 * @param name
	 *            医院名
	 * @return
	 */
	public  String getKeyFromName(Activity pactivity, String name)
	{
		List<TDept> tlist = fetchAllList(pactivity);
		if(tlist!=null){
			for (TDept t : tlist)
			{
				if (name.equals(t.getSubDeptName())) { return t.getSubDeptId(); }
			}
		}

		return "";
	}

}
