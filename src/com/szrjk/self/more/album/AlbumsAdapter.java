package com.szrjk.self.more.album;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.szrjk.dhome.R;

public class AlbumsAdapter extends BaseAdapter {

	private List<PhotoUpImageBucket> arrayList;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();// 初始化获取实例
	private DisplayImageOptions options;
	private String TAG = AlbumsAdapter.class.getSimpleName();// 开发测试标记位

	public AlbumsAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);
		arrayList = new ArrayList<PhotoUpImageBucket>();// 初始化集合
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCacheExtraOptions(96, 120).build();// 初始化图片加载器的配置
		// Initialize ImageLoader with configuration.
		imageLoader.init(config);

		// 使用DisplayImageOption.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.pic_downloadfailed_bg)
				// 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.pic_downloadfailed_bg)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.pic_downloadfailed_bg)
				// 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)
				// 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); // 创建配置过的DisplayImageOption对象
		// 上面的默认图片或者无法加载情况下的图片开发者可以自己设置，当然，可以设置不同的显示图片。我这里为了方便，采用同一幅图片作为默认图片
	};

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = layoutInflater.inflate(R.layout.ablum_item, parent,
					false);
			holder.image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.count = (TextView) convertView.findViewById(R.id.tv_count);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.count.setText("(" + arrayList.get(position).getCount() + ")");
		holder.name.setText(arrayList.get(position).getBucketName());

		// 图片加载器的使用代码，就这一句代码即可实现图片的加载。请注意这里的uri地址，因为我们现在实现的是获取本地图片，所以使用"file://"+图片的存储地址。如果要获取网络图片，这里的uri就是图片的网络地址。
		imageLoader.displayImage("file://"
				+ arrayList.get(position).getImageList().get(0).getImagePath(),
				holder.image, options);
		return convertView;
	}

	class Holder {
		ImageView image;
		TextView name;
		TextView count;
	}

	// 设置相册列表数据，主要用于把相册数据传递到Adapter中
	public void setArrayList(List<PhotoUpImageBucket> arrayList) {
		this.arrayList = arrayList;
	}
}
