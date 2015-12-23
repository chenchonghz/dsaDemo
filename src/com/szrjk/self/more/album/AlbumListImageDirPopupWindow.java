package com.szrjk.self.more.album;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.szrjk.dhome.R;
import com.szrjk.util.gallery.CommonAdapter;
import com.szrjk.util.gallery.ImageFloder;
import com.szrjk.util.gallery.ViewHolder;
/**
 * 新图库
 * @author Administrator
 *
 */
public class AlbumListImageDirPopupWindow  extends AlbumPopListView<ImageFloder>{
	private ListView mListDir;
	private Context context;
	private List<PhotoUpImageBucket> datas;
	private AlbumsAdapter adapter;
	public AlbumListImageDirPopupWindow(Context context ,int width, int height,
			List<PhotoUpImageBucket> datas, View convertView)
	{
		super(convertView, width, height, true, datas);
		this.context = context;
		this.datas = datas;
	}

	@Override
	public void initViews()
	{
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		adapter = new AlbumsAdapter(context);
		adapter.setArrayList(datas);
//		mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
//				R.layout.list_dir_item)
//		{
//			@Override
//			public void convert(ViewHolder helper, ImageFloder item)
//			{
//				helper.setText(R.id.id_dir_item_name, item.getName());
//				helper.setImageByUrl(R.id.id_dir_item_image,
//						item.getFirstImagePath());
//				helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
//			}
//		});
	}

	public interface OnImageDirSelected
	{
		void selected(PhotoUpImageBucket photoUpImageBucket);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
	{
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents()
	{
		mListDir.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				if (mImageDirSelected != null)
				{
					mImageDirSelected.selected(mDatas.get(position));
				}
			}
		});
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params)
	{
		// TODO Auto-generated method stub
	}

}
