package com.szrjk.search;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;

@ContentView(R.layout.activity_search_main)
public class SearchMainActivity extends BaseActivity implements SearchView.SearchViewListener
{
	/** 
	 * 搜索view 
	 */  
	@ViewInject(R.id.sv_search)
	private SearchView searchView;

	@ViewInject(R.id.et_search)
	private EditText et_search;

	@ViewInject(R.id.tv_text)
	private TextView tv;

	/** 
	 * 热搜框列表adapter 
	 */  
	private ArrayAdapter<String> hintAdapter;  

	/** 
	 * 自动补全列表adapter 
	 */  
	private ArrayAdapter<String> autoCompleteAdapter;  

	/** 
	 * 搜索结果列表adapter 
	 */  
	private SearchAdapter resultAdapter;  

	/** 
	 * 数据库数据，总数据 
	 */  
	private List<Bean> dbData;  

	/** 
	 * 热搜版数据 
	 */  
	private List<String> hintData;  

	/** 
	 * 搜索过程中自动补全数据 
	 */  
	private List<String> autoCompleteData;  

	/** 
	 * 搜索结果的数据 
	 */  
	private List<Bean> resultData;  

	/** 
	 * 默认提示框显示项的个数 
	 */  
	private static int DEFAULT_HINT_SIZE = 5;  

	/** 
	 * 提示框显示项的个数 
	 */  
	private static int hintSize = DEFAULT_HINT_SIZE;  

	/** 
	 * 设置提示框显示项的个数 
	 * 
	 * @param hintSize 提示框显示个数 
	 */  
	public static void setHintSize(int hintSize) {  
		SearchMainActivity.hintSize = hintSize;  
	}  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		ViewUtils.inject(this);
		et_search.setFocusable(true);
		et_search.setFocusableInTouchMode(true);
		et_search.requestFocus();
		initData();  
		initViews(); 
	}

	/** 
	 * 初始化视图 
	 */  
	private void initViews() {  
		//设置监听  
		searchView.setSearchViewListener(this);  
		//设置adapter  
		searchView.setTipsHintAdapter(hintAdapter);  
		searchView.setAutoCompleteAdapter(autoCompleteAdapter);  

	}  

	/** 
	 * 初始化数据 
	 */  
	private void initData() {  
		//从数据库获取数据  
		getDbData();  
		//初始化热搜版数据  
		getHintData();  
		//初始化自动补全数据  
		getAutoCompleteData(null);  
		//初始化搜索结果数据  
		getResultData(null);  
	}  

	/** 
	 * 获取db 数据 
	 */  
	private void getDbData() {  
		int size = 100;  
		dbData = new ArrayList<Bean>(size);  
		/*dbData.add(new Bean(R.drawable.i_search, "叶楠", "教授", "叶楠是一个技术非常高超的医生！"));
		dbData.add(new Bean(R.drawable.i_search, "李欢乐", "学生", "李欢乐是一个高材生！"));
		dbData.add(new Bean(R.drawable.i_search, "李逸", "主任医生", "李逸是外科技术很好！"));
		dbData.add(new Bean(R.drawable.i_search, "郑斯铭", "主治医生", "他是一个有责任的医生！"));
		dbData.add(new Bean(R.drawable.i_search, "药", "药物", "山药"));
		dbData.add(new Bean(R.drawable.i_search, "论文", "这是一篇医药论文", "这篇论文的内容很值得推敲。"));
		dbData.add(new Bean(R.drawable.i_search, "圈子", "好友圈", "这仅仅是一个好友圈而已。"));*/
	}  

	/** 
	 * 获取热搜版data 和adapter 
	 */  
	private void getHintData() {  
		hintData = new ArrayList<String>(hintSize);  
		/*hintData.add("找人");
		hintData.add("圈子");
		hintData.add("疾病");
		hintData.add("药物");
		hintData.add("病例");
		hintData.add("论文");*/
		hintAdapter = new ArrayAdapter<String>(this, R.layout.item_auto_complete_textview,R.id.tv_text, hintData);  
	}  

	/** 
	 * 获取自动补全data 和adapter 
	 */  
	private void getAutoCompleteData(String text) {  
		if (autoCompleteData == null) {  
			//初始化  
			autoCompleteData = new ArrayList<String>(hintSize);  
		} else {  
			// 根据text 获取auto data  
			autoCompleteData.clear();  
			for (int i = 0, count = 0; i < dbData.size()  
					&& count < hintSize; i++) {  
				if (dbData.get(i).getTitle().contains(text.trim())) {  
					autoCompleteData.add(dbData.get(i).getTitle());  
					count++;  
				}  
			}  
		}  
		if (autoCompleteAdapter == null) {  
			autoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.item_auto_complete_textview,R.id.tv_text, autoCompleteData);  
		} else {  
			autoCompleteAdapter.notifyDataSetChanged();  
		}  
	}  

	/** 
	 * 获取搜索结果data和adapter 
	 */  
	private void getResultData(String text) {  
		if (resultData == null) {  
			// 初始化  
			resultData = new ArrayList<Bean>();  
		} else {  
			resultData.clear();  
			for (int i = 0; i < dbData.size(); i++) {  
				if (dbData.get(i).getTitle().contains(text.trim())) {  
					resultData.add(dbData.get(i));  
				}  
			}  
		}  
		if (resultAdapter == null) {  
			resultAdapter = new SearchAdapter(this, resultData, R.layout.item_bean_list);  
		} else {  
			resultAdapter.notifyDataSetChanged();  
		}  
	}  

	/** 
	 * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据 
	 * @param text 
	 */  
	@Override  
	public void onRefreshAutoComplete(String text) {  
		//更新数据  
		getAutoCompleteData(text);  
	}  

	/** 
	 * 点击搜索键时edit text触发的回调 
	 * 
	 * @param text 
	 */  
	@Override  
	public void onSearch(String text) {  
		//更新result数据  
		getResultData(text);  
	}  
}
