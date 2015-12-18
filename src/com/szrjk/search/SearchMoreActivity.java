package com.szrjk.search;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.R;

@ContentView(R.layout.activity_search)
public class SearchMoreActivity extends FragmentActivity implements OnClickListener{
	
	@ViewInject(R.id.rly_search_user)
	private RelativeLayout rly_search_user;
	
	@ViewInject(R.id.tv_search_user)
	private TextView tv_search_user;
	
	@ViewInject(R.id.v_search_user)
	private View v_search_user;

	@ViewInject(R.id.rly_search_circle)
	private RelativeLayout rly_search_circle;
	
	@ViewInject(R.id.tv_search_circle)
	private TextView tv_search_circle;
	
	@ViewInject(R.id.v_search_circle)
	private View v_search_circle;

	@ViewInject(R.id.rly_search_sick)
	private RelativeLayout rly_search_sick;
	
	@ViewInject(R.id.tv_search_sick)
	private TextView tv_search_sick;
	
	@ViewInject(R.id.v_search_sick)
	private View v_search_sick;

	@ViewInject(R.id.rly_search_case)
	private RelativeLayout rly_search_case;
	
	@ViewInject(R.id.tv_search_case)
	private TextView tv_search_case;
	
	@ViewInject(R.id.v_search_case)
	private View v_search_case;

	@ViewInject(R.id.rly_search_drug)
	private RelativeLayout rly_search_drug;
	
	@ViewInject(R.id.tv_search_drug)
	private TextView tv_search_drug;
	
	@ViewInject(R.id.v_search_drug)
	private View v_search_drug;

	@ViewInject(R.id.rly_search_paper)
	private RelativeLayout rly_search_paper;
	
	@ViewInject(R.id.tv_search_paper)
	private TextView tv_search_paper;
	
	@ViewInject(R.id.v_search_paper)
	private View v_search_paper;
	
	@ViewInject(R.id.et_search)
	private EditText et_search;

	@ViewInject(R.id.vp_search)
	private ViewPager vp_search;
	
	private SearchMoreActivity instance;
	private List<TextView> tv_list = new ArrayList<TextView>();
	private List<View> v_list = new ArrayList<View>();
	private int[]id = {
			R.id.rly_search_user,R.id.rly_search_circle,R.id.rly_search_sick,
			R.id.rly_search_drug,R.id.rly_search_case,R.id.rly_search_paper
	};
	private LookForSomeoneFragment fg_search_user = new LookForSomeoneFragment();
	private CoterieFragment fg_search_circle = new CoterieFragment();
	private SearchFragment fg_search_sick  = new SearchFragment("01");
	private SearchFragment fg_search_drug = new SearchFragment("02");
	private SearchFragment fg_search_case  = new SearchFragment("03");
	private SearchPagerFragment fg_search_paper ;
//	private SearchFragment fg_search_paper = new SearchFragment();
	private List<Fragment> list = new ArrayList<Fragment>();
	public String searchword;

	private SearchVpAdapter adapter;

	private static final int AFRESH_SEARCH=0;
	private Handler handler=null;
	@Override   
	protected void onCreate(Bundle savedInstanceState) {   
		super.onCreate(savedInstanceState);   
		ViewUtils.inject(this);
		instance =this;
		vp_search.setOffscreenPageLimit(6);  
		Intent intent =getIntent();
		searchword = intent.getStringExtra("search");
		et_search.setText(searchword);
		fg_search_paper  = new SearchPagerFragment();
		initLayout();
	}
	public String getkeyword(){
		return et_search.getText().toString();
	}
	
	public void afreshSearch(){
		fg_search_paper.refreshSearch();
		fg_search_user.refreshSearch();
		fg_search_circle.refreshSearch();
		fg_search_sick.refreshSearch();
		fg_search_drug.refreshSearch();
		fg_search_case.refreshSearch();
	}
	
	public Handler getHandler(){
		return handler;
	}
	public FragmentPagerAdapter getAdapter(){
		return adapter;
		
	}
	private void initLayout() {
		rly_search_user.setOnClickListener(instance);
		rly_search_case.setOnClickListener(instance);
		rly_search_sick.setOnClickListener(instance);
		rly_search_paper.setOnClickListener(instance);
		rly_search_drug.setOnClickListener(instance);
		rly_search_circle.setOnClickListener(instance);
		tv_list.add(tv_search_user);tv_list.add(tv_search_circle);tv_list.add(tv_search_sick);
		tv_list.add(tv_search_drug);tv_list.add(tv_search_case);tv_list.add(tv_search_paper);
		v_list.add(v_search_user);v_list.add(v_search_circle);v_list.add(v_search_sick);
		v_list.add(v_search_drug);v_list.add(v_search_case);v_list.add(v_search_paper);
		list.add(fg_search_user);list.add(fg_search_circle);list.add(fg_search_sick);
		list.add(fg_search_drug);list.add(fg_search_case);list.add(fg_search_paper);
		adapter = new SearchVpAdapter(getSupportFragmentManager());
		vp_search.setAdapter(adapter);
		vp_search.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:changeLayout(id[0]);			
				break;
				case 1:changeLayout(id[1]);			
				break;
				case 2:changeLayout(id[2]);			
				break;
				case 3:changeLayout(id[3]);			
				break;
				case 4:changeLayout(id[4]);			
				break;
				case 5:changeLayout(id[5]);			
				break;

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rly_search_user:
			changeLayout(view.getId());
			vp_search.setCurrentItem(0);
			break;
		case R.id.rly_search_circle:
			changeLayout(view.getId());
			vp_search.setCurrentItem(1);
			break;
		case R.id.rly_search_sick:
			changeLayout(view.getId());
			vp_search.setCurrentItem(2);
			break;
		case R.id.rly_search_drug:
			changeLayout(view.getId());
			vp_search.setCurrentItem(3);
			break;
		case R.id.rly_search_case:
			changeLayout(view.getId());
			vp_search.setCurrentItem(4);
			break;
		case R.id.rly_search_paper:
			changeLayout(view.getId());
			vp_search.setCurrentItem(5);
			break;

		}
	}

	private void changeLayout(int viewId) {
		for (int i = 0; i < id.length; i++) {
			if (id[i]==viewId) {
				tv_list.get(i).setTextColor(getResources().getColor(R.color.global_main));
				v_list.get(i).setVisibility(View.VISIBLE);
			}else{
				tv_list.get(i).setTextColor(getResources().getColor(R.color.font_cell));
				v_list.get(i).setVisibility(View.GONE);

			}
		}

	}

	class SearchVpAdapter extends FragmentPagerAdapter{

		public SearchVpAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}
		@Override
		public int getCount() {
			return list.size();
		}
	}

}
