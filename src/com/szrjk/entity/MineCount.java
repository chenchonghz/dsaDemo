package com.szrjk.entity;

public class MineCount {

	private int mineIllcase;
	private int mineNormal;
	private int mineReceivelike;
	private int minePuzzle;
	private int mineSendLike;
	public int getMineIllcase() {
		return mineIllcase;
	}
	public void setMineIllcase(int mineIllcase) {
		this.mineIllcase = mineIllcase;
	}
	public int getMineNormal() {
		return mineNormal;
	}
	public void setMineNormal(int mineNormal) {
		this.mineNormal = mineNormal;
	}
	public int getMineReceivelike() {
		return mineReceivelike;
	}
	public void setMineReceivelike(int mineReceivelike) {
		this.mineReceivelike = mineReceivelike;
	}
	public int getMinePuzzle() {
		return minePuzzle;
	}
	public void setMinePuzzle(int minePuzzle) {
		this.minePuzzle = minePuzzle;
	}
	public int getMineSendLike() {
		return mineSendLike;
	}
	public void setMineSendLike(int mineSendLike) {
		this.mineSendLike = mineSendLike;
	}
	@Override
	public String toString() {
		return "MineCount [mineIllcase=" + mineIllcase + ", mineNormal="
				+ mineNormal + ", mineReceivelike=" + mineReceivelike
				+ ", minePuzzle=" + minePuzzle + ", mineSendLike="
				+ mineSendLike + "]";
	}
	
	
}
