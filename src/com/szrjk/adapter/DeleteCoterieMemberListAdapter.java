package com.szrjk.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.szrjk.dhome.R;
import com.szrjk.entity.UserCard;
import com.szrjk.widget.UserCardLayout;

public class DeleteCoterieMemberListAdapter extends BaseAdapter{

	private Context context;
	private List<UserCard> members;
	private LayoutInflater inflater;
	// 用来控制CheckBox的选中状况  
	private static HashMap<Integer, Boolean> isSelected; 

	public DeleteCoterieMemberListAdapter(Context context,List<UserCard> members) {
		this.context=context;
		this.members=members;
		this.inflater=LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();  
		// 初始化数据  
		initDate(); 
	}

	// 初始化isSelected的数据  
	private void initDate() {  
		for (int i = 0; i < members.size(); i++) {  
			getIsSelected().put(i, false);  
		}  
	}  

	@Override
	public int getCount() {
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		return members.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder=null;
		if (convertView==null) {
			convertView=inflater.inflate(R.layout.item_delete_coterie_member, null);
			holder=new ViewHolder();
			holder.userCardLayout=(UserCardLayout) convertView.findViewById(R.id.ucl_usercardlayout);
			holder.checkBox=(CheckBox) convertView.findViewById(R.id.cb_check);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		holder.userCardLayout.setContext(context);
		holder.userCardLayout.setUser(members.get(position));
		/*TextView professionalTitle = (TextView) holder.userCardLayout.findViewById(R.id.tv_user_type);
		professionalTitle.setTextColor(context.getResources().getColor(R.color.red));*/
		// 根据isSelected来设置checkbox的选中状况
		holder.checkBox.setChecked(isSelected.get(position));
		return convertView;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {  
		return isSelected;  
	}  

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
		DeleteCoterieMemberListAdapter.isSelected = isSelected;  
	}  


	public static class ViewHolder{
		public UserCardLayout userCardLayout;
		public CheckBox checkBox;
	}

}
