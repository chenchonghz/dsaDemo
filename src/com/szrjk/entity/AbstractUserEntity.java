package com.szrjk.entity;

import com.lidroid.xutils.DbUtils;

/**
 * denggm on 2015/10/26.
 * DHome
 */
public abstract class AbstractUserEntity<T extends  Object> {

    public final static String dbname = "Userdb.db";

    public final static  String dir = "data/data/com.szrjk.dhome/databases/";

    public static DbUtils userdb;

//    public abstract String getCURRENT_VERSION();
//
//    public abstract String getTAG();
//
//    public abstract  String getTableId();
//
//    public abstract  Class gettheclass();

    public void initTable(final DbUtils userdb){

        AbstractUserEntity.userdb = userdb;
    }

    /**
     * 查询所有
     *
     * @param dactivity
     * @return
     * @throws DbException
     */
//    public  List<T> fetchAllList(Activity dactivity)
//    {
//        try
//        {
////            final DbUtils db = DbUtils.create(dactivity, dbname);
//            List<T> ulist = userdb.findAll(Selector.from(gettheclass()));
//            return ulist;
//        }
//        catch (Exception e)
//        {
//            Log.e(getTAG(), "", e);
//        }
//        return null;
//    }
}
