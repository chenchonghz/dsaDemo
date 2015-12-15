package com.szrjk.entity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.IntArraySerializer;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.SharePerferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * denggm on 2015/10/23.
 * DHome
 */
@Table(name = "TPROFESSIONALTITLE")
public class TProfessionalTitle extends AbstractTBEntity<TProfessionalTitle>{

	private final static String TAG = "TCity";
	private String[] doctorList = {"101","102","103","104","105","106","107",
			"108","109","110","111","112"};
	private String[] pharmacistList = {
			"201","202","203","204","205","106","107","108","109","110"	
	};
	private String[] nurseList = {
			"301","302","303","304","305","106","107","108","109","110"	
	};

	@Id(column = "titleId")
	public String titleId;

	@Column(column = "titleName")
	public String titleName;


	@Override
	public String getCURRENT_VERSION() {
		return "CURRENT_VERSION_TPROFESSIONALTITLE";
	}

	@Override
	public String getTAG() {
		return TAG;
	}

	@Override
	public String getTableId() {
		return "professionalTitle";
	}

	@Override
	public Class gettheclass() {
		return TProfessionalTitle.class;
	}


	public  String getKeyFromName(Activity pactivity, String name)
	{
		List<TProfessionalTitle> tlist = fetchAllList(pactivity);
		for (TProfessionalTitle t : tlist)
		{
			if (name.equals(t.titleName)) { return t.titleId; }
		}
		return "";
	}

	public List<TProfessionalTitle> getListFromType(Activity pactivity,String Type)
	{
		List<TProfessionalTitle> tlist = fetchAllList(pactivity);
		List<TProfessionalTitle> list =new ArrayList<TProfessionalTitle>();
		switch (Integer.valueOf(Type)) {
		case 2:
			for (int i = 0; i < doctorList.length; i++) {
				for (TProfessionalTitle t : tlist)
				{
					if (doctorList[i].equals(t.titleId)) 
					{
						list.add(t);
					}
				}
			}
			return list;
		case 8:
			for (int i = 0; i < nurseList.length; i++) {
				for (TProfessionalTitle t : tlist)
				{
					if (nurseList[i].equals(t.titleId)) 
					{
						list.add(t);
					}
				}
			}
			return list;
		case 9:
			for (int i = 0; i < pharmacistList.length; i++) {
				for (TProfessionalTitle t : tlist)
				{
					if ( pharmacistList[i].equals(t.titleId)) 
					{
						list.add(t);
					}
				}
			}
			return list;


		}
		return list;


	}


}
