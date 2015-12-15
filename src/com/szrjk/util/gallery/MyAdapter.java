package com.szrjk.util.gallery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szrjk.dhome.R;


@SuppressLint("ShowToast")
public class MyAdapter extends CommonAdapter<String>
{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 * Linklist<>
	 */
	public static List<String> mSelectedImage = new ArrayList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;

	//显示可选个数
	private TextView tv_number;
	private int num;
	private Handler handler;
	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath,TextView tv_number,int num ,Handler handler)
	{

		super(context, mDatas, itemLayoutId);
		Collections.reverse(mDatas);//排序；从最近的开始显示
		this.mDirPath = dirPath;
		this.tv_number = tv_number;
		this.num = num;
		this.handler = handler;
		Log.i("mDirPath", mDirPath.toString());
	}

	@Override
	public void convert(final com.szrjk.util.gallery.ViewHolder helper, final String item)
	{
		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		//设置no_selected
		helper.setImageResource(R.id.id_item_select,
				R.drawable.icon_addpic_01_36);
		//设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);

		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v)
			{

				if (mSelectedImage.size() >= num) {
//					handler.sendEmptyMessage(9);
					// 已经选择过该图片,如果再点击，就取消勾选
					if (mSelectedImage.contains(mDirPath + "/" + item))
					{
//						
						mSelectedImage.remove(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.icon_addpic_01_36);
						mImageView.setColorFilter(null);

					}else
						// 未选择该图片
					{
//						mSelectedImage.add(mDirPath + "/" + item);
//						mSelect.setImageResource(R.drawable.icon_addpic_02_36);
//						mImageView.setColorFilter(Color.parseColor("#77000000"));
						Toast.makeText(mContext, "你最多只能选择" + num+ "张图片", 0).show();
					}
				}else{

					// 已经选择过该图片
					if (mSelectedImage.contains(mDirPath + "/" + item))
					{
						mSelectedImage.remove(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.icon_addpic_01_36);
						mImageView.setColorFilter(null);

					} else
						// 未选择该图片
					{
						mSelectedImage.add(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.icon_addpic_02_36);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
					}
				}
				tv_number.setText(mSelectedImage.size() + "/" + num);
			}
		});

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item))
		{
			//			mSelect.setImageResource(R.drawable.pictures_selected);
			mSelect.setImageResource(R.drawable.icon_addpic_02_36);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}
}
