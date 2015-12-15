package com.szrjk.entity;

/**
 * denggm on 2015/10/19.
 * DHome
 */
public class BusiException extends Exception{

    public BusiException() {
    }

    public BusiException(String detailMessage) {
        super(detailMessage);
    }

    public BusiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BusiException(Throwable throwable) {
        super(throwable);
    }
}
