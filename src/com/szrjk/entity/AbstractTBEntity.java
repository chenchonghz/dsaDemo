package com.szrjk.entity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * denggm on 2015/10/26.
 * DHome
 */
public abstract class AbstractTBEntity<T extends  Object> {

    public final static String dbname = "dhdb.db";

    public final static  String dir = "data/data/com.szrjk.dhome/databases/";
    
    //基础数据库版本号
    //数据库版本2：更改“海南岛”为“海南省”，增加“文昌”市
    public final static  int dhdbVersion = 2;

    public static DbUtils db;

    public abstract String getCURRENT_VERSION();

    public abstract String getTAG();

    public abstract  String getTableId();

    public abstract  Class gettheclass();

    public void initTable(final DbUtils db){

        this.db = db;
    }

    /**
     * 查询所有
     *
     * @param dactivity
     * @return
     * @throws DbException
     */
    public  List<T> fetchAllList(Activity dactivity)
    {
        try
        {
//            final DbUtils db = DbUtils.create(dactivity, dbname);
            List<T> ulist = db.findAll(Selector.from(gettheclass()));
            return ulist;
        }
        catch (Exception e)
        {
            Log.e(getTAG(), "", e);
        }
        return null;
    }
}
