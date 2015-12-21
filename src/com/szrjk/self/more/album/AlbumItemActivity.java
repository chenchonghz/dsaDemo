package com.szrjk.self.more.album;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;

public class AlbumItemActivity extends BaseActivity implements OnClickListener{
    private GridView gridView;
    private PhotoUpImageBucket photoUpImageBucket;
    private AlbumItemAdapter adapter;
	private TextView tv_album;
	private TextView tv_name;
	private LinearLayout ll_backtoalbum;
	private TextView tv_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_gallery);
        init();
        setListener();
    }
    private void init(){
    	tv_cancel=(TextView)findViewById(R.id.tv_cancel);
        gridView = (GridView) findViewById(R.id.gv_list);
        ll_backtoalbum=(LinearLayout)findViewById(R.id.ll_backtoalbum);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_album = (TextView) findViewById(R.id.tv_album);

        Intent intent = getIntent();
        photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra("imagelist");
        tv_name.setText(photoUpImageBucket.getBucketName());
        adapter = new AlbumItemAdapter(photoUpImageBucket.getImageList(), AlbumItemActivity.this);
        gridView.setAdapter(adapter);
        gridView.setEmptyView(tv_album);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
    private void setListener(){
    	ll_backtoalbum.setOnClickListener(this);
    	tv_cancel.setOnClickListener(this);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	Intent intent=new Intent(AlbumItemActivity.this, CropPictureActivity.class);
            	intent.putExtra("IMAGEPATH", photoUpImageBucket.getImageList().get(position).getImagePath());
            	Log.i("imagepath", photoUpImageBucket.getImageList().get(position).getImagePath());
				startActivity(intent);
            }
        });
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
			startActivity(intent);
			finish();
			break;
		case R.id.tv_cancel:
			finish();
			break;
		}
    }
}
