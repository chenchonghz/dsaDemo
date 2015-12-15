package com.szrjk.http;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.entity.PhotoBucket;
import com.szrjk.entity.PhotoType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * denggm on 2015/11/3.
 * DHome
 */
public class InterfaceComm {

    // 处理图片
    public static void dealPhoto(String pathName,PhotoType photoType)
    {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ServiceName", "dealPic");
        Map<String, Object> busiParams = new HashMap<String, Object>();
        List<PhotoBucket> buckets = new ArrayList<PhotoBucket>();
        PhotoBucket bucket = new PhotoBucket();
        if (PhotoType.Face == photoType){
            bucket.setBucket(Constant.PHOTO_BUCKET_FACE);
            bucket.setSize(Constant.FACE_DEAL_SIZE);
            pathName = pathName.substring(pathName.lastIndexOf("/") + 1);
            pathName="face/"+pathName;
            bucket.setKey(pathName);
        }else{
            bucket.setBucket(Constant.PHOTO_BUCKET_FEED);
            bucket.setSize(Constant.FEED_DEAL_SIZE);
            pathName = pathName.substring(pathName.lastIndexOf("/") + 1);
            bucket.setKey(pathName);
        }

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
