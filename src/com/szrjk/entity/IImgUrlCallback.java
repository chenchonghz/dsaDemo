package com.szrjk.entity;

import android.graphics.Bitmap;

/**
 * denggm on 2015/11/3.
 * DHome
 */
public interface IImgUrlCallback {

    /**
     * 上传完url之后处理
     * @param imgurl
     */
    public void operImgUrl(String imgurl);

    /**
     * 拍照/选择照片后的处理
     * @param bitmap
     */
    public void operImgPic(Bitmap bitmap);


}
