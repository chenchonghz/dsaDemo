package com.szrjk.util;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ImageItem implements Serializable,Parcelable,Comparable<ImageItem>
{
	@Override
	public int describeContents() {
		return 0;
	}

//	@Override
//	public void writeToParcel(Parcel parcel, int i) {
//		parcel.writeInt(id);
//		parcel.writeString(name);
//	}


	public ImageItem() {
		super();
	}

	public ImageItem(Parcel source) {
		imageId = source.readInt();
		thumbnailPath = source.readString();
		imagePath = source.readString();
		bitmap = source.readParcelable(Bitmap.class.getClassLoader());
		isSelected = source.readInt() != 0;
	}

	public static final Parcelable.Creator<ImageItem> CREATOR
				= new Parcelable.Creator<ImageItem>() {

			@Override
		public ImageItem createFromParcel(Parcel parcel) {
			return new ImageItem(parcel);
		}

		@Override
		public ImageItem[] newArray(int i) {
			return new ImageItem[i];
		}
	};


	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(imageId);
		parcel.writeString(thumbnailPath);
		parcel.writeString(imagePath);
		parcel.writeParcelable(bitmap, i);
		parcel.writeInt(isSelected ? 1 : 0);
	}

	private static final long serialVersionUID = 1L;
	public Integer imageId;
	public String thumbnailPath;
	public String imagePath;
	private Bitmap bitmap;
	public boolean isSelected = false;
	
	/**
	 * 由于在图库选择图片之后，返回的bitmap保存到imageitem里面，但是在循环上传取出来的
	 * 时候，bitmap是空的？未知异常。去截图，和拍照都没问题
	 * 现在增加一个absPath属性，保存图片的绝对路径；
	 * 如果出现imageitem 的getBitmap是空的时候，用绝对路径再读取一次图片，压缩之后上传即可
	 */
	private String absPaht;
	
	public String getAbsPaht() {
		return absPaht;
	}

	public void setAbsPaht(String absPaht) {
		this.absPaht = absPaht;
	}

	public Integer getImageId()
	{
		return imageId;
	}

	public void setImageId(Integer imageId)
	{
		this.imageId = imageId;
	}

	public String getThumbnailPath()
	{
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath)
	{
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath()
	{
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}

	public boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}

	public Bitmap getBitmap()
	{
//		if (bitmap == null)
//		{
//			try
//			{
//				bitmap = Bimp.revitionImageSize(imagePath);
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
		try {
			if (bitmap == null) {
				Log.i("imagePath对象里面的", absPaht);
				
//				bitmap = BitmapFactory.decodeFile(absPaht);
				bitmap = BitmapCompressImage.getimage(absPaht);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}

	@Override
	public int compareTo(ImageItem another) {
		return this.getImageId().compareTo(another.getImageId());
	}

}
