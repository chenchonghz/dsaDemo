package com.szrjk.self.more.album;

import java.io.Serializable;

public class PhotoUpImageItem implements Serializable ,Comparable<PhotoUpImageItem>{
    //图片ID
    private String imageId;
    //原图路径
    private String imagePath;
    
    public String thumbnailPath;

    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	@Override
	public String toString() {
		return "PhotoUpImageItem [imageId=" + imageId + ", imagePath="
				+ imagePath + ", thumbnailPath=" + thumbnailPath + "]";
	}
	@Override
	public int compareTo(PhotoUpImageItem another) {
		return new Integer(Integer.parseInt(this.getImageId())).compareTo(new Integer(Integer.parseInt(another.getImageId())));
	}
	
}
