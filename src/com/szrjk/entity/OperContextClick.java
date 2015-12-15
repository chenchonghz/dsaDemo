package com.szrjk.entity;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;

import com.szrjk.config.Constant;
import com.szrjk.dhome.IndexGalleryActivity;
import com.szrjk.index.CaseDetailActivity;
import com.szrjk.index.PostDetailActivity;
import com.szrjk.index.PostDetailFowardActivity;

public class OperContextClick implements IContextClickOper,Serializable{
	
	private Context context;
	
	
	public OperContextClick() {
		super();
	}



	public OperContextClick(Context context) {
		super();
		this.context = context;
	}



	@Override
	public void clickoper(int position, String[] piclist,Context context) {
		String postId = ((IndexGalleryActivity)context).getPostId();
		int postType = ((IndexGalleryActivity)context).getPostType();
		Intent intent=null;
		switch (postType) {
		case 101:
			intent = new Intent(context, PostDetailActivity.class);
			intent.putExtra(Constant.POST_ID, postId);
			break;
		case 102:
			intent = new Intent(context, CaseDetailActivity.class);
			intent.putExtra(Constant.POST_ID, postId);
			break;
		case 103:
			intent = new Intent(context, CaseDetailActivity.class);
			intent.putExtra(Constant.POST_ID, postId);
			break;
		case 104:
			intent = new Intent(context, PostDetailActivity.class);
			intent.putExtra(Constant.POST_ID, postId);
			break;
		case 202:
			intent = new Intent(context, PostDetailFowardActivity.class);
			intent.putExtra(Constant.POST_ID, postId);
			break;
		case 204:
			intent = new Intent(context, PostDetailFowardActivity.class);
			intent.putExtra(Constant.POST_ID, postId);
			break;
		}
		context.startActivity(intent);
	}
}
