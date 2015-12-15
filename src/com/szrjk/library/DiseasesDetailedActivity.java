package com.szrjk.library;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.LibraryEntity;
import com.szrjk.entity.PaperListInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.ChangeWebViewFontSize;

@ContentView(R.layout.activity_diseasesdetailed)
public class DiseasesDetailedActivity extends BaseActivity {
	
	private DiseasesDetailedActivity instance;
	private LibraryEntity detaileInfo;
	private PaperListInfo paper;
	@ViewInject(R.id.headerview_text_id)
	private TextView headerview_text_id;
	private String id;
	@ViewInject(R.id.tv_con)
	private TextView tv_con;
	
	@ViewInject(R.id.web1)
	private WebView web;
	
	private Dialog dl;
	private Dialog alertdialog;
	private WebSettings settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		Intent intent = this.getIntent();
		dl = createDialog(this, "加载中...");
		paper = (PaperListInfo) intent.getSerializableExtra("paper");
		detaileInfo = (LibraryEntity) intent.getSerializableExtra(Constant.Library);
		id = intent.getStringExtra("searchID");
		if (detaileInfo==null&&id==null) {
			String st = paper.getPaperTitle();
			int slen = st.length();
			if (slen > 5) {
				headerview_text_id.setText(st.substring(0, 5) + "...");
			}else{
				headerview_text_id.setText(detaileInfo.getName());
			}
			id = paper.getKnowledgeId();
		}else if(id==null){
			String st = detaileInfo.getName();
			int slen = st.length();
			if (slen > 5) {
				headerview_text_id.setText(st.substring(0, 5) + "...");
			}else{
				headerview_text_id.setText(detaileInfo.getName());
			}
			id = detaileInfo.getId();
		}else{
			String st = intent.getStringExtra("searchTitle");
			st = st.replace("<font color=red>", "");
			st = st.replace("</font>", "");
			Log.i("TAG", st);
			int slen = st.length();
			if (slen > 5) {
				headerview_text_id.setText(st.substring(0, 5) + "...");
			}else{
				headerview_text_id.setText(st);
			}
		}
//		showToast(instance, detaileInfo.toString(), 0);
		//加载网页
		alertdialog = createDialog(instance, "加载中...");
		loadingWeb();
//		getInitialFontSize();
//		getFontSize();
		ChangeWebViewFontSize cvfs = new ChangeWebViewFontSize();
		cvfs.setFontSize(instance, settings);
	}


	private void loadingWeb() {
		String webUrl = "http://dochome.digi123.cn/dochome/view/library/matter.html?queryId=" + id;
		//findDetailedInfo();
		web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
		// 开启 DOM storage API 功能
		web.getSettings().setDomStorageEnabled(true);
		// 开启 database storage API 功能
		web.getSettings().setDatabaseEnabled(true);
//		web.setWebContentsDebuggingEnabled(true);
//		web.loadDataWithBaseURL(bd, null, "text/html", "utf-8", null);   

		settings = web.getSettings();
		settings.setJavaScriptEnabled(true);
		//改字体用
//		settings.setSupportZoom(true);  
		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		web.setWebChromeClient(new WebChromeClient());
		web.setWebViewClient(new WebViewClient(){
	           @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	             view.loadUrl(url);
	            return true;
	        }
	       });
		web.loadUrl(webUrl);
		web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                	alertdialog.dismiss();
                } else {
                    // 加载中
//                	alertdialog.setCancelable(false);
                	alertdialog.show();
                }

            }
        });
	}

	private void findDetailedInfo() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> busiParams = new HashMap<String, Object>();

		paramMap.put("ServiceName", "selectKnowledge");
		paramMap.put("KB", "kb");
		busiParams.put("knowledgeId", detaileInfo.getId());
		//由于这里分页处理（我的操作：进来的时候一次性获取所以），不知道里面有多少个病。所以把所以数据做成1页
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
				dl.show();
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				dl.dismiss();
				showToast(instance, "加载失败", 0);
			}

			@Override
			public void success(JSONObject jsonObject) {
				dl.dismiss();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
//					List<DiseasesDetailedEntity> dateList = JSON.parseArray(returnObj.getString("ListOut"), DiseasesDetailedEntity.class);
//					Log.i("dateList", dateList.toString());
					JSONObject obj = returnObj.getJSONObject("ListOut");
					String oc = obj.getString("context");
//					System.out.println(oc);
					tv_con.setText(Html.fromHtml(oc));
//					web.loadData(oc, "text/html", "gbk");
//					web.loadDataWithBaseURL(null, oc, "text/html", "utf-8", null);     
				}
			}
		});
	}
	
	@OnClick(R.id.btn_back)
	public void onBack(View v ){
		finish();
	}
}
