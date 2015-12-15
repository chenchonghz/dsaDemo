package com.szrjk.entity;

import java.util.Arrays;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PostComment {

	private byte[] ivPortrait;
	private String tvName;
	private String tvTime;
	private int ivComment;
	private String etComment;
	public PostComment() {
		super();
	}
	public PostComment(byte[] ivPortrait, String tvName, String tvTime,
			int ivComment, String etComment) {
		super();
		this.ivPortrait = ivPortrait;
		this.tvName = tvName;
		this.tvTime = tvTime;
		this.ivComment = ivComment;
		this.etComment = etComment;
	}
	
	
	public byte[] getIvPortrait() {
		return ivPortrait;
	}
	public void setIvPortrait(byte[] ivPortrait) {
		this.ivPortrait = ivPortrait;
	}
	public String getTvName() {
		return tvName;
	}
	public void setTvName(String tvName) {
		this.tvName = tvName;
	}
	public String getTvTime() {
		return tvTime;
	}
	public void setTvTime(String tvTime) {
		this.tvTime = tvTime;
	}
	public int getIvComment() {
		return ivComment;
	}
	public void setIvComment(int ivComment) {
		this.ivComment = ivComment;
	}
	public String getEtComment() {
		return etComment;
	}
	public void setEtComment(String etComment) {
		this.etComment = etComment;
	}
	@Override
	public String toString() {
		return "Comment [ivPortrait="
				+ Arrays.toString(ivPortrait) + ", tvName=" + tvName
				+ ", tvTime=" + tvTime + ", ivComment=" + ivComment
				+ ", etComment=" + etComment + "]";
	}
	
	
}
