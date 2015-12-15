package com.szrjk.entity;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * denggm on 2015/11/3.
 * DHome
 */
public class DealPhoto extends Thread
{
    private String pathName;

    public DealPhoto(String pathName)
    {
        this.pathName = pathName;
    }

    @Override
    public void run()
    {
        dealPhoto(pathName);
    }

    // 处理图片
    private void dealPhoto(String pathName)
    {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ServiceName", "dealPic");
        Map<String, Object> busiParams = new HashMap<String, Object>();
        List<PhotoBucket> buckets = new ArrayList<PhotoBucket>();
        PhotoBucket bucket = new PhotoBucket();
        bucket.setBucket(Constant.PHOTO_BUCKET_FEED);
        bucket.setKey(pathName);
        bucket.setSize(Constant.FEED_DEAL_SIZE);
        buckets.add(bucket);
        busiParams.put("pics", buckets);
        paramMap.put("BusiParams", busiParams);
        DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

            @Override
            public void start() {

            }

            @Override
            public void loading(long total, long current, boolean isUploading) {

            }

            @Override
            public void failure(HttpException exception, JSONObject jobj) {

            }

            @Override
            public void success(JSONObject jsonObject) {

            }
        });
    }
}
