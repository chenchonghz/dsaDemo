package com.szrjk.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.szrjk.dhome.R;
import com.szrjk.util.ToastUtils;

public class SearchView extends LinearLayout implements OnClickListener {

	/**
	 * 输入框
	 */
	private EditText etInput;

	/**
	 * 删除键
	 */
	private ImageView ivDelete;

	/**
	 * 搜索键
	 */
	private ImageView iv_search;
	private LinearLayout lly_search;

	/**
	 * 返回
	 */
	private ImageView ivBack;
	private LinearLayout lly_back;

	/**
	 * 上下文对象
	 */
	private Context mContext;

	/**
	 * 弹出列表
	 */
	private ListView lvTips;

	/**
	 * 提示adapter （推荐adapter）
	 */
	private ArrayAdapter<String> mHintAdapter;

	/**
	 * 自动补全adapter 只显示名字
	 */
	private ArrayAdapter<String> mAutoCompleteAdapter;

	/**
	 * 搜索回调接口
	 */
	private SearchViewListener mListener;

	/**
	 * 设置搜索回调接口
	 * 
	 * @param listener
	 *            监听者
	 */
	public void setSearchViewListener(SearchViewListener listener) {
		mListener = listener;
	}

	public SearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.search_layout, this);
		initViews();
	}

	private void initViews() {
		etInput = (EditText) findViewById(R.id.et_search);
		ivDelete = (ImageView) findViewById(R.id.iv_search_delete);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		lvTips = (ListView) findViewById(R.id.lv_search_tips);
		lly_back = (LinearLayout) findViewById(R.id.lly_back);
		lly_search = (LinearLayout) findViewById(R.id.lly_search);

		lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				// set edit text
				String text = lvTips.getAdapter().getItem(i).toString();
				etInput.setText(text);
				etInput.setSelection(text.length());
				// hint list view gone and result list view show
				lvTips.setVisibility(View.GONE);
				notifyStartSearching(text);
			}
		});

		ivDelete.setOnClickListener(this);
		lly_back.setOnClickListener(this);
		lly_search.setOnClickListener(this);

		etInput.addTextChangedListener(new EditChangedListener());
		etInput.setOnClickListener(this);
		etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent keyEvent) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					lvTips.setVisibility(GONE);
					notifyStartSearching(etInput.getText().toString());
				}
				return false;
			}
		});
		// 输入完后按键盘上的搜索键【回车键改为了搜索键】
		etInput.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
				Log.i("keyCode1", keyCode+"");
				Log.i("keyCode2", KeyEvent.KEYCODE_ENTER+"");
				// 修改回车键功能
				if (keyCode==KeyEvent.KEYCODE_ENTER) {
					// 先隐藏键盘
					((InputMethodManager) mContext
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(((Activity) mContext)
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

					// 跳转到搜索结果界面
					String search = etInput.getText().toString();
					if (search.equals("")) {
						ToastUtils.showMessage(mContext, "请输入搜索内容!");
						return false;
					}
					if ((Activity) mContext instanceof SearchMoreActivity) {
						SearchMoreActivity activity = (SearchMoreActivity) mContext;
						activity.afreshSearch();
						return false;
					}
					Intent intent = new Intent(mContext,
							SearchMoreActivity.class);
					intent.putExtra("search", search);
					mContext.startActivity(intent);
				}
				return false;
			}
		});
	}

	/**
	 * 通知监听者 进行搜索操作
	 * 
	 * @param text
	 */
	private void notifyStartSearching(String text) {
		if (mListener != null) {
			mListener.onSearch(etInput.getText().toString());
		}
		// 隐藏软键盘
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 设置热搜版提示 adapter
	 */
	public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
		this.mHintAdapter = adapter;
		if (lvTips.getAdapter() == null) {
			lvTips.setAdapter(mHintAdapter);
		}
	}

	/**
	 * 设置自动补全adapter
	 */
	public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
		this.mAutoCompleteAdapter = adapter;
	}

	private class EditChangedListener implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i2,
				int i3) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i2,
				int i3) {
			if (!"".equals(charSequence.toString())) {
				ivDelete.setVisibility(VISIBLE);
				lvTips.setVisibility(VISIBLE);
				if (mAutoCompleteAdapter != null
						&& lvTips.getAdapter() != mAutoCompleteAdapter) {
					lvTips.setAdapter(mAutoCompleteAdapter);
				}
				// 更新autoComplete数据
				if (mListener != null) {
					mListener.onRefreshAutoComplete(charSequence + "");
				}
			} else {
				ivDelete.setVisibility(GONE);
				if (mHintAdapter != null) {
					lvTips.setAdapter(mHintAdapter);
				}
				lvTips.setVisibility(GONE);
			}

		}

		@Override
		public void afterTextChanged(Editable editable) {
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.et_search:

			lvTips.setVisibility(VISIBLE);
			break;
		case R.id.iv_search_delete:
			etInput.setText("");
			ivDelete.setVisibility(GONE);
			break;
		case R.id.lly_back:
			((Activity) mContext).finish();
			break;
		case R.id.lly_search:
			String search = etInput.getText().toString();
			if (search.equals("")) {
				ToastUtils.showMessage(mContext, "请输入搜索内容!");
				return;
			}
			if ((Activity) mContext instanceof SearchMoreActivity) {
				SearchMoreActivity activity = (SearchMoreActivity) mContext;
				activity.afreshSearch();
				return;
			}
			Intent intent = new Intent(mContext, SearchMoreActivity.class);
			intent.putExtra("search", search);
			mContext.startActivity(intent);

			break;
		}
	}

	/**
	 * search view回调方法
	 */
	public interface SearchViewListener {

		/**
		 * 更新自动补全内容
		 * 
		 * @param text
		 *            传入补全后的文本
		 */
		void onRefreshAutoComplete(String text);

		/**
		 * 开始搜索
		 * 
		 * @param text
		 *            传入输入框的文本
		 */
		void onSearch(String text);

		// /**
		// * 提示列表项点击时回调方法 (提示/自动补全)
		// */
		// void onTipsItemClick(String text);
	}
}
