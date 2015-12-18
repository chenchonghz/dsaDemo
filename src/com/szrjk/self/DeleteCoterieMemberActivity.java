package com.szrjk.self;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.adapter.DeleteCoterieMemberListAdapter;
import com.szrjk.adapter.DeleteCoterieMemberListAdapter.ViewHolder;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.Coterie;
import com.szrjk.entity.UserCard;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.SetListViewHeightUtils;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.DeleteCoterieMemberPopup;
import com.szrjk.widget.UserCardLayout;

@ContentView(R.layout.activity_delete_coterie_member)
public class DeleteCoterieMemberActivity extends BaseActivity{
	
	@ViewInject(R.id.ll_coterie_member)
	private LinearLayout ll_coterie_member;

	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	
	@ViewInject(R.id.tv_delete)
	private TextView tv_delete;
	
	@ViewInject(R.id.ucl_usercardlayout)
	private UserCardLayout ucl_usercardlayout;
	
	@ViewInject(R.id.tv_selectAll)
	private TextView tv_selectAll;
	
	@ViewInject(R.id.lv_coteriemember)
	private ListView lv_coteriemember;
	
	private DeleteCoterieMemberActivity instance;

	private Coterie coterie;

	private Resources resources;

	private UserInfo userInfo;
	
	private ArrayList<String> objUserSeqIds=new ArrayList<String>();

	private DeleteCoterieMemberListAdapter adapter;

	private List<UserCard> memberCardList;
	
	private int checkNum; // 记录选中的条目数量
	
	private ViewHolder holder;

	private DeleteCoterieMemberPopup deleteCoterieMemberPopup;
	
	private static final int DELETE_COTERIE_MEMBER_SUCCESS=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		coterie=(Coterie) getIntent().getSerializableExtra("COTERIE");
		initLayout();
	}
	private void initLayout() {
		resources=getResources();
		userInfo=Constant.userInfo;
		UserCard creator = coterie.getCreator();
		ucl_usercardlayout.setContext(instance);
		/*TextView professionalTitle = (TextView) findViewById(R.id.tv_user_type);
		professionalTitle.setTextColor(resources.getColor(R.color.red));*/
		ucl_usercardlayout.setUser(creator);
		memberCardList = coterie.getMemberCardList();
		adapter=new DeleteCoterieMemberListAdapter(instance, memberCardList);
		lv_coteriemember.setAdapter(adapter);
		SetListViewHeightUtils.setListViewHeight(lv_coteriemember);
		lv_coteriemember.setOnItemClickListener(new OnItemClickListener() {
			

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				UserCard userCard=memberCardList.get(i);
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤  
                holder = (ViewHolder) view.getTag();  
                // 改变CheckBox的状态  
                holder.checkBox.toggle();
                // 将CheckBox的选中状况记录下来  
                DeleteCoterieMemberListAdapter.getIsSelected().put(i, holder.checkBox.isChecked());  
                // 调整选定条目  
                if (holder.checkBox.isChecked() == true) {
                	if (!objUserSeqIds.contains(userCard.getUserSeqId())) {
                		objUserSeqIds.add(userCard.getUserSeqId());
					}
                	//Log.i("objUserSeqIds1", objUserSeqIds.toString());
                    checkNum++;  
                } else {  
                	if (objUserSeqIds.contains(userCard.getUserSeqId())) {
						objUserSeqIds.remove(userCard.getUserSeqId());
					}
                    checkNum--;  
                    //Log.i("objUserSeqIds1", objUserSeqIds.toString());
                }  
			}
		});
	}
	
	/**点击删除*/
	@OnClick(R.id.tv_delete)
	public void clickDelete(View view){
		deleteCoterieMemberPopup=new 
				DeleteCoterieMemberPopup(instance, itemsOnClick);
		deleteCoterieMemberPopup.showAtLocation(ll_coterie_member, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
private OnClickListener itemsOnClick=new OnClickListener() {
		

		@Override
		public void onClick(View v) {
				deleteCoterieMemberPopup.dismiss();
			switch (v.getId())
			{
			case R.id.tv_delete_coterie_member:
				deleteCoterieMember();
				break;
			}
		}
	};
	int selectNum=-1;
	
	/**点击全选*/
	@OnClick(R.id.tv_selectAll)
	public void clickSelectAll(View view){
		if (selectNum!=checkNum) {
			// 遍历list的长度，将MyAdapter中的map值全部设为true  
			for (int i = 0; i < memberCardList.size(); i++) {  
				DeleteCoterieMemberListAdapter.getIsSelected().put(i, true);
				if (!objUserSeqIds.contains(memberCardList.get(i).getUserSeqId())) {
					objUserSeqIds.add(memberCardList.get(i).getUserSeqId());
				}
			}  
			// 数量设为list的长度  
			checkNum = memberCardList.size();  
			selectNum=checkNum;
		}else {
			for (int i = 0; i < memberCardList.size(); i++) {  
				DeleteCoterieMemberListAdapter.getIsSelected().put(i, false);  
				objUserSeqIds.remove(memberCardList.get(i).getUserSeqId());
			}  
			// 数量设为list的长度  
			checkNum = 0; 
			selectNum=-1;
		}
        // 刷新listview和
        adapter.notifyDataSetChanged();
        //Log.i("objUserSeqIds2", objUserSeqIds.toString());
	}
	
	private void deleteCoterieMember() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "removeUserFromCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("coterieId", coterie.getCoterieId());
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		busiParams.put("objUserSeqIds", objUserSeqIds);
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ToastUtils.showMessage(instance, "删除成功！");
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("CHECKNUM", checkNum);
				intent.putExtras(bundle);
				setResult(DELETE_COTERIE_MEMBER_SUCCESS,intent);
				instance.finish();
			}
			@Override
			public void start() {
			}
			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
			}
		});
	}
	
	

}
