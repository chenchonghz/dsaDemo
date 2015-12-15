package com.szrjk.adapter;

import java.util.ArrayList;

import com.szrjk.entity.NewType;
import com.szrjk.explore.NewsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class TabPageIndicatorAdapter extends FragmentStatePagerAdapter{
	private ArrayList<NewsFragment> fragments;
	private FragmentManager fm;

	public TabPageIndicatorAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
		// TODO Auto-generated constructor stub
	}
	
	public TabPageIndicatorAdapter(FragmentManager fm,
			ArrayList<NewsFragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fm = fm;
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.e("Adapter", "Fragment个数："+fragments.size());
		return fragments.size();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	public void setFragments(ArrayList<NewsFragment> fragments) {
		if (this.fragments != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.fragments) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.fragments = fragments;
		notifyDataSetChanged();
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		Object obj = super.instantiateItem(container, position);
		return obj;
	}
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	// TODO Auto-generated method stub
    	super.destroyItem(container, position, object);
    }

}
