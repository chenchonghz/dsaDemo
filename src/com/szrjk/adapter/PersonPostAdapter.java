package com.szrjk.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.szrjk.adapter.IndexListViewAdapter.ViewHolder;
import com.szrjk.entity.OtherAbstractInfoEntity;
import com.szrjk.entity.OtherSrcPostAbstractCard;
import com.szrjk.entity.OtherSrcUserCard;
import com.szrjk.entity.OtherStatisInfo;
import com.szrjk.entity.OthersPostEntity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PersonPostAdapter extends BaseAdapter{
	private Context context;
	private List<OthersPostEntity> postlist;
	private ArrayList<OtherAbstractInfoEntity> abstractInfoEntities;
	private ArrayList<OtherStatisInfo> statisInfos;
	private ArrayList<OtherSrcPostAbstractCard> otherSrcPostAbstractCards;
	private ArrayList<OtherSrcUserCard> otherSrcUserCards;
	private BitmapUtils bitmapUtils;
	private Boolean isLike = false;
	private ViewHolder viewHolder;
	
	
	public PersonPostAdapter(Context contextj,List<OthersPostEntity> postlist,ArrayList<OtherAbstractInfoEntity> abstractInfoEntities,
			ArrayList<OtherStatisInfo> statisInfos,ArrayList<OtherSrcPostAbstractCard> otherSrcPostAbstractCards,
			ArrayList<OtherSrcUserCard> otherSrcUserCards) {
		this.context = contextj;
		if (postlist != null) {
			this.postlist = postlist;
		}else{
			this.postlist = new ArrayList<OthersPostEntity>();
		}
		if (abstractInfoEntities != null) {
			this.abstractInfoEntities = abstractInfoEntities;
		}else{
			this.abstractInfoEntities = new ArrayList<OtherAbstractInfoEntity>();
		}
		if (statisInfos != null) {
			this.statisInfos = statisInfos;
		}else{
			this.statisInfos = new ArrayList<OtherStatisInfo>();
		}
		if (otherSrcPostAbstractCards != null) {
			this.otherSrcPostAbstractCards = otherSrcPostAbstractCards;
		}else{
			this.otherSrcPostAbstractCards = new ArrayList<OtherSrcPostAbstractCard>();
		}
		if (otherSrcUserCards != null) {
			this.otherSrcUserCards = otherSrcUserCards;
		}else{
			this.otherSrcUserCards = new ArrayList<OtherSrcUserCard>();
		}
		bitmapUtils = new BitmapUtils(context);
	}
	@Override
	public int getCount() {
		return postlist.size();
	}
	@Override
	public Object getItem(int position) {
		return postlist.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int arg0, View convertView, ViewGroup parent) {
		return null;
	}

}
