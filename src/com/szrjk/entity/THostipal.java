package com.szrjk.entity;

import android.app.Activity;
import android.util.Log;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import java.util.List;

/**
 * denggm on 2015/10/21. DHome
 */
@Table(name = "THOSTIPAL")
public class THostipal extends AbstractTBEntity<THostipal>
{

	private final static String TAG = "THostipal";

	@Column(column = "cityCode")
	private String cityCode;

	@Column(column = "hospitalName")
	private String hospitalName;

	@Id(column = "hospitalId")
	private String hospitalId;


	public String getCityCode()
	{
		return cityCode;
	}

	public void setCityCode(String cityCode)
	{
		this.cityCode = cityCode;
	}

	public String getHospitalName()
	{
		return hospitalName;
	}

	public void setHospitalName(String hospitalName)
	{
		this.hospitalName = hospitalName;
	}

	public String getHospitalId()
	{
		return hospitalId;
	}

	public void setHospitalId(String hospitalId)
	{
		this.hospitalId = hospitalId;
	}

	@Override
	public String getCURRENT_VERSION() {
		return "TCITY_VERSION";
	}

	@Override
	public String getTAG() {
		return TAG;
	}

	@Override
	public String getTableId() {
		return "hospital";
	}

	@Override
	public Class gettheclass() {
		return THostipal.class;
	}

//	public  void initTable(final DbUtils db, final int iversion,
//			final Context context)
//	{
//
//		super.initTable(db,iversion,context);
////		int currentVersion = SharePerferenceUtil.getInstance(context,
////				Constant.APP_INFO).getIntValue(CURRENT_VERSION);
////
////		Log.e(TAG, currentVersion + ".." + iversion);
////		if (currentVersion >= iversion)
////		{
////			// 现在的版本高,不理
////			Log.e(TAG, "自己的版本>=接口版本!!!");
////			return;
////		}
////
////		// 调接口
////		HashMap<String, Object> busiParams = new HashMap<String, Object>();
////		busiParams.put("tableId", "hospital");
////
////		HashMap<String, Object> paramMap = new HashMap<String, Object>();
////		paramMap.put("ServiceName", "queryTableData");
////		paramMap.put("BusiParams", busiParams);
////		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack()
////		{
////			@Override
////			public void start()
////			{
////			}
////
////			@Override
////			public void loading(long total, long current, boolean isUploading)
////			{
////			}
////
////			@Override
////			public void failure(HttpException exception, JSONObject jsonObject)
////			{
////			}
////
////			public void dosuccess(JSONObject jsonObject){
////
////				try
////				{
////					ErrorInfo errorObj = JSON.parseObject(
////							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
////					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
////					List<THostipal> resultlist = JSON.parseArray(returnObj.getString("ListOut"), THostipal.class);
////
////					// 结果list
////					if (resultlist != null && !resultlist.isEmpty())
////					{
////						db.dropTable(THostipal.class);
////						SharePerferenceUtil.getInstance(context,
////								Constant.APP_INFO).setIntValue(CURRENT_VERSION,
////								iversion);
////					}
////					else
////					{
////						// 结果为空，则
////						return;
////					}
////
////					for (int i = 0; i < resultlist.size(); i++)
////					{
////						THostipal tHostipal = (THostipal) resultlist.get(i);
////						db.save(tHostipal);// 入sqlite
////					}
////				}
////				catch (DbException e)
////				{
////					e.printStackTrace();
////				}
////			}
////
////
////			@Override
////			public void success(final JSONObject jsonObject)
////			{
////
////				Runnable r = new Runnable() {
////					@Override
////					public void run() {
////						dosuccess(jsonObject);
////					}
////				};
////				new Thread(r).start();
////
////			}
////		});
//	}

	/**
	 * 查询所有 医院
	 * 
	 * @param dactivity
	 * @return
	 * @throws DbException
	 */
	public  List<THostipal> fetchHostipalList(Activity dactivity)
	{
		try
		{
//			final DbUtils db = DbUtils.create(dactivity, "dhdb");
			List<THostipal> ulist = db.findAll(Selector.from(THostipal.class));
			return ulist;
		}
		catch (Exception e)
		{
			Log.e(TAG, "", e);
		}
		return null;
	}

	/**
	 * 医院名对应的 hospitalid
	 * 
	 * @param pactivity
	 * @param name
	 *            医院名
	 * @return
	 */
	public  String getKeyFromName(Activity pactivity, String name)
	{
		List<THostipal> tlist = fetchHostipalList(pactivity);
		if(tlist!=null){
			for (THostipal t : tlist)
			{
				if (name.equals(t.getHospitalName())) { return t.getHospitalId(); }
			}
		}
		return "";
	}

}
