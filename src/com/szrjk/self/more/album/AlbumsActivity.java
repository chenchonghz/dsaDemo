package com.szrjk.self.more.album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.self.more.album.PhotoUpAlbumHelper.GetAlbumList;

public class AlbumsActivity extends BaseActivity {

    private ListView listView;
    private AlbumsAdapter adapter;//适配器
    private PhotoUpAlbumHelper photoUpAlbumHelper;//加载相册和图片的异步线程类
    private List<PhotoUpImageBucket> list;//存放相册列表数据
	private TextView tv_cancel;
	public static String recentphoto;
	public static int count=0;
	private PhotoUpImageBucket photoUpImageBucket=new PhotoUpImageBucket();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        init();//初始化
        loadData();//加载数据
        onItemClick();//设置相册点击事件
    }
    private int num ;
    private void init(){
    	num =  this.getIntent().getIntExtra("num", 0);
    	photoUpImageBucket.setBucketName("最近照片");
    	photoUpImageBucket.setCount(count);
    	ArrayList<PhotoUpImageItem> imageList = new ArrayList<PhotoUpImageItem>();
    	PhotoUpImageItem photoUpImageItem = new PhotoUpImageItem();
    	photoUpImageItem.setImagePath(recentphoto);
    	imageList.add(0, photoUpImageItem);
    	photoUpImageBucket.setImageList(imageList);
    	
        listView = (ListView) findViewById(R.id.lv_albums);
        tv_cancel=(TextView)findViewById(R.id.tv_cancel);
        adapter = new AlbumsAdapter(AlbumsActivity.this);
        listView.setAdapter(adapter);
    }

    private void loadData(){
        photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();//创建异步线程实例
        photoUpAlbumHelper.init(AlbumsActivity.this);//初始化实例
        //回调接口，创建匿名内部对象，实现接口中的方法，获取到PhotoUpAlbumHelper的接口GetAlbumList所传递的数据
        photoUpAlbumHelper.setGetAlbumList(new GetAlbumList() {
            @Override
            public void getAlbumList(List<PhotoUpImageBucket> list) {
            	Collections.sort(list);
            	AlbumsActivity.this.list = list;
//            	if (photoUpImageBucket!=null) {
//            		list.add(0, photoUpImageBucket);
//				}
                adapter.setArrayList(list);
                adapter.notifyDataSetChanged();//更新视图
            }
        });
        photoUpAlbumHelper.execute(false);//异步线程执行
    }
    //通过点击每个相册，进入相册内部查看该相册内的图片
    private void onItemClick(){
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	if (position==0) {
					Intent intent=new Intent(AlbumsActivity.this, AlbumGalleryActivity.class);
					intent.putExtra("num", num);
					startActivity(intent);
					finish();
				}else {
					Intent intent = new Intent(AlbumsActivity.this,AlbumItemActivity.class);
					intent.putExtra("imagelist", list.get(position));
					intent.putExtra("num", num);
					startActivity(intent);
					finish();
				}
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				AlbumGalleryAdapter.mSelectedImage.clear();
			}
		});
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
