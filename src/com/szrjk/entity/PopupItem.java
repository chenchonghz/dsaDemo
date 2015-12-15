package com.szrjk.entity;

/**
 * denggm on 2015/11/24.
 * DHome 弹出框item
 */
public class PopupItem {

    private String itemname;

    private IPopupItemCallback iPopupItemCallback;
 
    private int color;
    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public IPopupItemCallback getiPopupItemCallback() {
        return iPopupItemCallback;
    }

    public void setiPopupItemCallback(IPopupItemCallback iPopupItemCallback) {
        this.iPopupItemCallback = iPopupItemCallback;
    }

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	
    
    
}
