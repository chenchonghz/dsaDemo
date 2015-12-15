package com.szrjk.util;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class CheckImageDegree {
	private static int degree;
	private static Bitmap cbitmap;
	/**
	 * 
	 * @param f  文件
	 * @param filePath  文件路径
	 * @return bitm
	 */
	public static Bitmap getNewBitmap(File f,String filePath)	{
		/** 
	     * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转 
	     */ 
	if (f == null) {
		degree = readPictureDegree(filePath);  
	}
	if (filePath ==  null) {
		
		degree = readPictureDegree(f.getAbsolutePath());  
	}	
	
     
    BitmapFactory.Options opts=new BitmapFactory.Options();//获取缩略图显示到屏幕上
    opts.inSampleSize=2;
    if (f == null) {
    	cbitmap = BitmapFactory.decodeFile(filePath,opts);
	}
    if (filePath == null) {
    	cbitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),opts);
	}
//    cbitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),opts);
     
    /** 
     * 把图片旋转为正的方向 
     */ 
    Bitmap newbitmap = rotaingImageView(degree, cbitmap);  
//    iv.setImageBitmap(newbitmap);
    return newbitmap;
    
	}
    
	/**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
   /*
    * 旋转图片 
    * @param angle 
    * @param bitmap 
    * @return Bitmap 
    */ 
   public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
       //旋转图片 动作   
       Matrix matrix = new Matrix();;  
       matrix.postRotate(angle);  
       System.out.println("angle2=" + angle);  
       // 创建新的图片   
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
       return resizedBitmap;  
   }
}
