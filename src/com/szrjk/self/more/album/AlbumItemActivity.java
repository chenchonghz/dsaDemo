package com.szrjk.self.more.album;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;

public class AlbumItemActivity extends BaseActivity implements OnClickListener{
    private GridView gridView;
    private PhotoUpImageBucket photoUpImageBucket;
    private AlbumGalleryAdapter adapter;
	private TextView tv_album;
	private TextView tv_name;
	private LinearLayout ll_backtoalbum;
	private TextView tv_comp;
	private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_gallery);
        init();
        setListener();
    }
    private void init(){
    	tv_comp=(TextView)findViewById(R.id.tv_comp);
        gridView = (GridView) findViewById(R.id.gv_list);
        ll_backtoalbum=(LinearLayout)findViewById(R.id.ll_backtoalbum);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_album = (TextView) findViewById(R.id.tv_album);

        Intent intent = getIntent();
        num = intent.getIntExtra("num", 0);
        photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra("imagelist");
        tv_name.setText(photoUpImageBucket.getBucketName());
//        adapter = new AlbumItemAdapter(photoUpImageBucket.getImageList(), AlbumItemActivity.this);
        adapter = new AlbumGalleryAdapter(this,photoUpImageBucket.getImageList(),tv_comp,num);
        tv_comp.setText("完成" + "("+AlbumGalleryAdapter.mSelectedImage.size() + "/" + num + ")");
		
        gridView.setAdapter(adapter);
        gridView.setEmptyView(tv_album);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
    private void setListener(){
    	ll_backtoalbum.setOnClickListener(this);
    	tv_comp.setOnClickListener(this);
    	//已经在适配器里面做了点击事件
//        gridView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                    int position, long id) {
//            	Intent intent=new Intent(AlbumItemActivity.this, CropPictureActivity.class);
//            	intent.putExtra("IMAGEPATH", photoUpImageBucket.getImageList().get(position).getImagePath());
//            	Log.i("imagepath", photoUpImageBucket.getImageList().get(position).getImagePath());
//				startActivity(intent);
//            }
//        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.ll_backtoalbum:
			Intent intent=new Intent(AlbumItemActivity.this, AlbumsActivity.class);
			intent.putExtra("num", num);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_comp:
//			finish();
			ArrayList<String> url = (ArrayList<String>) AlbumGalleryAdapter.mSelectedImage;
			Log.i("url", url.toString());
			if (url.size() == 0) {
				Toast.makeText(this, "没有选择图片", Toast.LENGTH_SHORT).show();
			} else {

				Intent intent2 = new Intent();
				String[] arr = new String[url.size()];
				for (int i = 0; i < url.size(); i++) {
					arr[i] = url.get(i);
				}
				intent2.putExtra("arr", arr);
				setResult(RESULT_OK, intent2);
				//清空图片
				AlbumGalleryAdapter.mSelectedImage.clear();
				finish();
			}
			
			
			break;
		}
    }
}
