package com.szrjk.dhome;

import com.lidroid.xutils.ViewUtils;
import com.szrjk.config.Constant;
import com.szrjk.library.LibraryGuideActivity;
import com.szrjk.library.LibraryPaperActivity;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LibraryFragment extends Fragment implements OnClickListener
{
	private RelativeLayout rl_sick;
	private RelativeLayout rl_case;
	private RelativeLayout rl_drug;
	private RelativeLayout rl_paper;
	private MainActivity instance;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.library_fragment, null);
		ViewUtils.inject(this, view);
		instance = (MainActivity) getActivity();
		initLayout(view);
		initListener();
		return view;
	}

	
	private void initLayout(View view) {
		rl_sick = (RelativeLayout) view.findViewById(R.id.rl_sick);
		rl_case = (RelativeLayout) view.findViewById(R.id.rl_case);
		rl_drug = (RelativeLayout) view.findViewById(R.id.rl_drug);
		rl_paper = (RelativeLayout) view.findViewById(R.id.rl_paper);
	}
	//设置布局监听器
	private void initListener() {
		rl_sick.setOnClickListener(this);
		rl_case.setOnClickListener(this);
		rl_drug.setOnClickListener(this);
		rl_paper.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_sick:
			Intent intent_sick = new Intent(instance, LibraryGuideActivity.class);
			intent_sick.putExtra(Constant.Library, "1");
			startActivity(intent_sick);
			break;
		case R.id.rl_case:
			if(BusiUtils.isguest(getContext())){
				//如果是游客,则弹框
				DialogUtil.showGuestDialog(getContext(),null);
				break;
			}
			Intent intent_case = new Intent(instance, LibraryGuideActivity.class);
			intent_case.putExtra(Constant.Library, "3");
			startActivity(intent_case);
			break;
		case R.id.rl_drug:
			if(BusiUtils.isguest(getContext())){
				//如果是游客,则弹框
				DialogUtil.showGuestDialog(getContext(),null);
				break;
			}
			Intent intent_drug = new Intent(instance, LibraryGuideActivity.class);
			intent_drug.putExtra(Constant.Library, "2");
			startActivity(intent_drug);
			break;
		case R.id.rl_paper:
			if(BusiUtils.isguest(getContext())){
				//如果是游客,则弹框
				DialogUtil.showGuestDialog(getContext(),null);
				break;
			}
			Intent intent_paper = new Intent(instance, LibraryPaperActivity.class);
			startActivity(intent_paper);
			break;

		}
		
	}

}
