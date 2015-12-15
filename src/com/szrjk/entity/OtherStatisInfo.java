package com.szrjk.entity;

public class OtherStatisInfo {

	private int COMMENT_NUM  ;
	private int FORWARD_NUM  ;
	private int LIKE_NUM  ;
	private String POST_ID  ;
	private int READ_NUM  ;
	public int getCOMMENT_NUM() {
		return COMMENT_NUM;
	}
	public void setCOMMENT_NUM(int cOMMENT_NUM) {
		COMMENT_NUM = cOMMENT_NUM;
	}
	public int getFORWARD_NUM() {
		return FORWARD_NUM;
	}
	public void setFORWARD_NUM(int fORWARD_NUM) {
		FORWARD_NUM = fORWARD_NUM;
	}
	public int getLIKE_NUM() {
		return LIKE_NUM;
	}
	public void setLIKE_NUM(int lIKE_NUM) {
		LIKE_NUM = lIKE_NUM;
	}
	public String getPOST_ID() {
		return POST_ID;
	}
	public void setPOST_ID(String pOST_ID) {
		POST_ID = pOST_ID;
	}
	public int getREAD_NUM() {
		return READ_NUM;
	}
	public void setREAD_NUM(int rEAD_NUM) {
		READ_NUM = rEAD_NUM;
	}
	@Override
	public String toString() {
		return "OtherStatisInfo [COMMENT_NUM=" + COMMENT_NUM + ", FORWARD_NUM="
				+ FORWARD_NUM + ", LIKE_NUM=" + LIKE_NUM + ", POST_ID="
				+ POST_ID + ", READ_NUM=" + READ_NUM + "]";
	}
	
}
