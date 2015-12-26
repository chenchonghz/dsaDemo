package com.szrjk.entity;

/**
 * 
 * @author 郑斯铭 on  2015/12/10
 * CustonListDialog的item
 *
 */
public class DialogItem {
	private String itemText;
	private DialogItemCallback dialogItemCallback;
	private int color;
	
	public DialogItem() {
		super();
	}
	public DialogItem(String itemText,
			int color, DialogItemCallback dialogItemCallback) {
		super();
		this.itemText = itemText;
		this.dialogItemCallback = dialogItemCallback;
		this.color = color;
	}
	public String getItemText() {
		return itemText;
	}
	public void setItemText(String itemText) {
		this.itemText = itemText;
	}
	public DialogItemCallback getDialogItemCallback() {
		return dialogItemCallback;
	}
	public void setDialogItemCallback(DialogItemCallback dialogItemCallback) {
		this.dialogItemCallback = dialogItemCallback;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
}
