package com.szrjk.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * denggm on 2015/10/21.
 * DHome
 */
public class DCollectionUtils {


    /**
     * 给adapter使用 string[]
     * @param rList
     * @param pkey
     * @return
     */
    public static String[] convertFromList1(List<Map> rList,String pkey){
        List<String> targetList = new LinkedList<String>();
        for (Map t:rList){
            targetList.add((String) t.get(pkey));
        }
        return targetList.toArray(new String[targetList.size()]);
    }


    /**
     * 给adapter使用 string[]
     * @param rlist
     * @param pkey
     * @return
     */
    public static String[] converFromList2(List rlist,String pkey){
        List<Map> mlist = new LinkedList<Map>();
        for(Object pobj:rlist){
            mlist.add(DjsonUtils.bean2Map(pobj));
        }
        return convertFromList1(mlist,pkey);
    }
}
