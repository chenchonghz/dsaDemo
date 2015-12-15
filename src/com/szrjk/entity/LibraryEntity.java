package com.szrjk.entity;

import java.io.Serializable;

public class LibraryEntity implements Serializable{

    private String id;
    private String hasLeaf;
    private String name;
    private String picCount;
    private String sortLetters;  //显示数据拼音的首字母
    private String kbFace;
	private String firstLetter;

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public String getKbFace() {
		return kbFace;
	}
	public void setKbFace(String kbFace) {
		this.kbFace = kbFace;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHasLeaf() {
		return hasLeaf;
	}
	public void setHasLeaf(String hasLeaf) {
		this.hasLeaf = hasLeaf;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicCount() {
		return picCount;
	}
	public void setPicCount(String picCount) {
		this.picCount = picCount;
	}
	@Override
	public String toString() {
		return "LibraryEntity [id=" + id + ", hasLeaf=" + hasLeaf + ", name="
				+ name + ", picCount=" + picCount + ", sortLetters="
				+ sortLetters + ", kbFace=" + kbFace + "]";
	}
    
}
