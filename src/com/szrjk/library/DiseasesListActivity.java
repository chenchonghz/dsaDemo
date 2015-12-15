package com.szrjk.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.LibraryEntity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.sortlistview.CharacterParser;
import com.szrjk.sortlistview.ClearEditText;
import com.szrjk.sortlistview.PinyinComparator;
import com.szrjk.sortlistview.SideBar;
import com.szrjk.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.szrjk.sortlistview.SortAdapter;

@ContentView(R.layout.activity_diseaseslist)
public class DiseasesListActivity extends BaseActivity {

	private DiseasesListActivity instance;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;

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
	private List<LibraryEntity> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private ListView sortListView;
	private Dialog alertdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		alertdialog = createDialog(this, "加载中...");
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		Intent intent = instance.getIntent();
		LibraryEntity linfo = (LibraryEntity) intent.getSerializableExtra(Constant.Library);
		findDiseasesInfo(linfo.getId());
		headerview_text_id.setText(linfo.getName());
		initViews();
		//获得疾病列表
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

		//item点击事件
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				//				LinearLayout item = (LinearLayout) adapter.getItem(position);
				//获得里面的子控件，因为点击字母的item，不需要跳转、
				TextView tv_c = (TextView) view.findViewById(R.id.catalog);
				TextView tv_title = (TextView) view.findViewById(R.id.title);
				
				//android:descendantFocusability="blocksDescendants"
				int visi = tv_c.getVisibility();
				//				@ExportedProperty(mapping={@IntToString(from=0, to="VISIBLE"), 
				//				@IntToString(from=4, to="INVISIBLE"), @IntToString(from=8, to="GONE")})
				if (visi == 0) {
					tv_c.setClickable(false);
							Intent intent = new Intent(instance,DiseasesDetailedActivity.class);
							intent.putExtra(Constant.Library, SourceDateList.get(position));
							startActivity(intent);
				}
				else {

					Intent intent = new Intent(instance,DiseasesDetailedActivity.class);
					//				(LibraryEntity)adapter.getItem(position)
					intent.putExtra(Constant.Library, SourceDateList.get(position));
					startActivity(intent);
				}

			}
		});

		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter); 


		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
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

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<LibraryEntity> filterDateList = new ArrayList<LibraryEntity>();

		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(LibraryEntity sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	private List<LibraryEntity> findDiseasesInfo(String categoryId) {

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> busiParams = new HashMap<String, Object>();

		paramMap.put("ServiceName", "selectDocument");
		paramMap.put("KB", "kb");
		busiParams.put("categoryId", categoryId);
		//由于这里分页处理（我的操作：进来的时候一次性获取所以），不知道里面有多少个病。所以把所以数据做成1页
		busiParams.put("numPerPage", "10000");
		busiParams.put("page", "1");

		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
				alertdialog.show();
//				alertdialog.setCancelable(false);
			}
			@Override
			public void loading(long total, long current, boolean isUploading) {			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				alertdialog.dismiss();
				showToast(instance, "加载失败，再试试呗", 0);
			}
			@Override
			public void success(JSONObject jsonObject) {
				alertdialog.dismiss();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
					SourceDateList =  JSON.parseArray(returnObj.getString("ListOut"), LibraryEntity.class);
					String[] zm = new String[SourceDateList.size()];
					for (int i = 0; i < zm.length; i++) {
						zm[i] = SourceDateList.get(i).getName();
					}
					filledData(zm);
					Collections.sort(SourceDateList, pinyinComparator);
					adapter = new SortAdapter(instance, SourceDateList);
					sortListView.setAdapter(adapter); 
					fl_content.setVisibility(View.VISIBLE);
				}else{
					fl_content.setVisibility(View.VISIBLE);
					Log.i("diseases", "异常");
				}


			}
		});
		return SourceDateList;
	}

	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private void filledData(String [] date){
		System.out.println(Arrays.toString(date));
//		List<LibraryEntity> mSortList = new ArrayList<LibraryEntity>();

		for(int i=0; i<SourceDateList.size(); i++){
			LibraryEntity sortModel = SourceDateList.get(i);
			sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
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
	@OnClick(R.id.btn_back)
	public void onbackOnclick(View v){
		finish();
	}
}
