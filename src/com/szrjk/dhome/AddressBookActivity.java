package com.szrjk.dhome;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.AddressBookAdapter;
import com.szrjk.config.Constant;
import com.szrjk.entity.AddressCard;
import com.szrjk.entity.AddressListEntity;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.explore.MyCircleActivity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.sortlistview.AddressBookPinyinComparator;
import com.szrjk.sortlistview.CharacterParser;
import com.szrjk.sortlistview.ClearEditText;
import com.szrjk.sortlistview.SideBar;
import com.szrjk.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.szrjk.util.ToastUtils;
@ContentView(R.layout.activity_address_book)
public class AddressBookActivity extends BaseActivity {
	private AddressBookActivity instance;
	@ViewInject(R.id.sidrbar)
	private SideBar sideBar;

	@ViewInject(R.id.dialog)
	private TextView dialog;

	@ViewInject(R.id.fl_content)
	private FrameLayout fl_content;

	@ViewInject(R.id.headerview_text_id)
	private TextView headerview_text_id;

	@ViewInject(R.id.btn_back)
	private ImageView btn_back;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	//	private List<LibraryEntity> SourceDateList;
	private List<AddressListEntity> sourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private AddressBookPinyinComparator pinyinComparator;
	private ListView sortListView;
	private Dialog alertdialog;
	private AddressBookAdapter adapter;
	private ClearEditText mClearEditText;
	private boolean visi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new AddressBookPinyinComparator();
		//获得是否圈子可见
		visi = this.getIntent().getBooleanExtra("visibility", true);
		findDiseasesInfo();
		initViews();
	}

	private void initViews() {
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		//通讯录接入显示头部圈子。我的主页点击进入：不显示圈子
		if (visi) {
			//加载头部
			View header = View.inflate(instance, R.layout.address_book_header, null);
			RelativeLayout rl = (RelativeLayout) header.findViewById(R.id.rl_addressbook_header);
			rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(instance,MyCircleActivity.class));
				}
			});
			sortListView.addHeaderView(header);
		}
		//item点击事件
//		sortListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				try {
//					AddressListEntity item = sourceDateList.get(position);
//					Log.i("item	",item.toString());
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//				//Intent intent = new Intent(instance,OtherPeopleActivity.class);
//				//intent.putExtra(Constant.USER_SEQ_ID, item.getUserCard().getUserSeqId());
//				//startActivity(intent);
//				//finish();
//			}
//		});

		adapter = new AddressBookAdapter(this, sourceDateList);
		sortListView.setAdapter(adapter);


		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				//filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

//	/**
//	 * 根据输入框中的值来过滤数据并更新ListView
//	 * @param filterStr
//	 */
	//	private void filterData(String filterStr){
	//		List<LibraryEntity> filterDateList = new ArrayList<LibraryEntity>();
	//
	//		if(TextUtils.isEmpty(filterStr)){
	//			filterDateList = SourceDateList;
	//		}else{
	//			filterDateList.clear();
	//			for(LibraryEntity sortModel : SourceDateList){
	//				String name = sortModel.getName();
	//				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
	//					filterDateList.add(sortModel);
	//				}
	//			}
	//		}
	//
	//		// 根据a-z进行排序
	//		Collections.sort(filterDateList, pinyinComparator);
	//		adapter.updateListView(filterDateList);
	//	}
	private void findDiseasesInfo() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getUserFriends");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("startNum",0);
		busiParams.put("endNum",500);
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					sourceDateList = JSON.parseArray(returnObj.getString("ListOut"), AddressListEntity.class);
					filledData(sourceDateList);
					//					Log.i("friendlist", sourceDateList.toString());
					Collections.sort(sourceDateList, pinyinComparator);
					adapter = new AddressBookAdapter(instance, sourceDateList);
					sortListView.setAdapter(adapter);
					fl_content.setVisibility(View.VISIBLE);
				}else if("0006".equals(errorObj.getReturnCode())){
					Log.i("好友列表", "异常");
				}
			}

			@Override
			public void start() {

			}
			@Override
			public void loading(long total, long current, boolean isUploading) {

			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				ToastUtils.showMessage(instance, "获取失败");
			}
		});

		//			@Override
		//			public void success(JSONObject jsonObject) {
		//				alertdialog.dismiss();
		//				ErrorInfo errorObj = JSON.parseObject(
		//						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
		//				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
		//					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
		//					SourceDateList =  JSON.parseArray(returnObj.getString("ListOut"), LibraryEntity.class);
		//					String[] zm = new String[SourceDateList.size()];
		//					for (int i = 0; i < zm.length; i++) {
		//						zm[i] = SourceDateList.get(i).getName();
		//					}
		//					filledData(zm);
		//					Collections.sort(SourceDateList, pinyinComparator);
		//					adapter = new AddressBookAdapter(instance, SourceDateList);
		//					sortListView.setAdapter(adapter); 
		//					fl_content.setVisibility(View.VISIBLE);
		//				}else{
		//					fl_content.setVisibility(View.VISIBLE);
		//					Log.i("diseases", "异常");
		//				}
		//
		//
		//			}
	}

	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private void filledData(List<AddressListEntity> friendlist){
		//		System.out.println(Arrays.toString(date));
		//		List<LibraryEntity> mSortList = new ArrayList<LibraryEntity>();

		for(int i=0; i<friendlist.size(); i++){
			AddressCard sortModel = friendlist.get(i).getUserCard();
			//			System.out.println();
			//			Log.i("列表里面的模型的名字", sortModel.getName());
			//			Log.i("String [] date", date[i]);
			//			System.out.println();
			//sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(friendlist.get(i).getUserCard().getUserName());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");

			}
			//			SourceDateList.add(sortModel);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode != RESULT_OK){
				Log.i("","data 异常");
			}
			switch (requestCode){
				case 3003:
					String isF = data.getStringExtra("isFriend");
					if ("no".equals(isF)){
						int index = data.getIntExtra("index",0);
						adapter.removeItem(index);
					}
					break;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
