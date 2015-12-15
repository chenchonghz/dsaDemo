package com.szrjk.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.szrjk.util.DCollectionUtils;

/**
 * denggm on 2015/10/20. DHome
 */
@Table(name = "TCITY")
public class TCity extends AbstractTBEntity<TCity>
{

	private final static String TAG = "TCity";

	@Id(column = "cityCode")
	private String cityCode;

	@Column(column = "cityName")
	private String cityName;

	@Column(column = "provinceCode")
	private String provinceCode;

	@Column(column = "provinceName")
	private String provinceName;


	public String getCityCode()
	{
		return cityCode;
	}

	public void setCityCode(String cityCode)
	{
		this.cityCode = cityCode;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getProvinceCode()
	{
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode)
	{
		this.provinceCode = provinceCode;
	}

	public String getProvinceName()
	{
		return provinceName;
	}

	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}

	@Override
	public String getCURRENT_VERSION() {
		return "CURRENT_VERSION_TCITY";
	}

	@Override
	public String getTAG() {
		return TAG;
	}

	@Override
	public String getTableId() {
		return "region";
	}

	@Override
	public Class gettheclass() {
		return TCity.class;
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
	}

	/**
	 * 得省的 string[]
	 * 
	 * @param dactivity
	 * @return
	 * @throws Exception
	 */
	public String[] fetchAllProvinceNames(Activity dactivity)
			throws Exception
	{
		List<DbModel> ulist = db
				.findDbModelAll(Selector.from(TCity.class)
						.groupBy("provinceCode")
						.select("provinceCode", "provinceName"));
		// Log.i(TAG, DjsonUtils.bean2Json(ulist));
		List<String> provincename = new LinkedList<String>();
		List<String> provincecode = new LinkedList<String>();

		for (DbModel d : ulist)
		{
			Map tmpMap = d.getDataMap();
			provincename.add(String.valueOf(tmpMap.get("provinceName")));
		}
		String[] provincenameArr = provincename.toArray(new String[provincecode
				.size()]);
		return provincenameArr;
	}

	/**
	 * 得省的 codes
	 * 
	 * @param dactivity
	 * @return
	 * @throws Exception
	 */
	public String[] fetchProvinceCode(Activity dactivity)
			throws Exception
	{
//		final DbUtils db = DbUtils.create(dactivity, "dhdb");
		List<DbModel> ulist = db
				.findDbModelAll(Selector.from(TCity.class)
						.groupBy("provinceCode")
						.select("provinceCode", "provinceName"));
		List<String> provincecode = new LinkedList<String>();

		for (DbModel d : ulist)
		{
			Map tmpMap = d.getDataMap();
			provincecode.add(String.valueOf(tmpMap.get("provinceCode")));
		}
		String[] provincecodeArr = provincecode.toArray(new String[provincecode
				.size()]);
		return provincecodeArr;
	}

	public  String[] fetchAllCityNames(Activity dactivity, String pid)
			throws Exception
	{
		return getCitys(dactivity, pid, 2);
	}

	/**
	 * 
	 * @param dactivity
	 * @param pid
	 * @param flag
	 *            1取 citycode,2取 cityname
	 * @return
	 */
	private  String[] getCitys(Activity dactivity, String pid, int flag)
			throws DbException
	{
//		final DbUtils db = DbUtils.create(dactivity, "dhdb");
		List<TCity> ulist = db.findAll(Selector.from(TCity.class).where(
				"provinceCode", "=", pid));
		List<String> targetList = new LinkedList<String>();

		if (flag == 1)
		{
			return DCollectionUtils.converFromList2(ulist, "cityCode");
		}
		else
		{
			return DCollectionUtils.converFromList2(ulist, "cityName");
		}
	}

	public  String[] fetchAllCityCodes(Activity dactivity, String pid)
			throws Exception
	{
		return getCitys(dactivity, pid, 1);
	}

	/**
	 * 通过provicecode 得 省名字
	 * @param activity
	 * @param code 省的code
	 * @return
	 * @throws DbException
	 */
	public  String getProvince(Activity activity,String code) throws DbException {
		List<TCity> tCities = super.fetchAllList(activity);
//		final DbUtils db = DbUtils.create(activity, "dhdb");
		List<TCity> ulist = db.findAll(Selector.from(TCity.class).where("provinceCode", "=", code));
		return ulist.get(0).getProvinceName();
	}
	public  String getCity(Activity activity,String code) throws DbException {
		List<TCity> tCities = super.fetchAllList(activity);
//		final DbUtils db = DbUtils.create(activity, "dhdb");
		List<TCity> ulist = db.findAll(Selector.from(TCity.class).where("cityCode", "=", code));
		if(!ulist.isEmpty()){	
			return ulist.get(0).getCityName();
		}else{
			return null;
		}
	}

}
