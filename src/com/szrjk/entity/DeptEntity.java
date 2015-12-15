package com.szrjk.entity;

public class DeptEntity {

	private String deptValue;
	private String deptId;
	public String getDeptValue() {
		return deptValue;
	}
	public void setDeptValue(String deptValue) {
		this.deptValue = deptValue;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	@Override
	public String toString() {
		return "DeptEntity [deptValue=" + deptValue + ", deptId=" + deptId
				+ "]";
	}
	
}
