package com.szrjk.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import u.aly.ci;

import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.szrjk.config.Constant;

/**
 * 用户圈子通知-数据库存储实体类
 * @author 郑斯铭
 *
 */
@Table(name = "TFriendRequest")
public class TCircleRequest extends AbstractUserEntity<TCircleRequest>{
	private static final  String TAG = "TCircleRequest";
	@Id(column ="RequestID")@Transient
	private int id;
	//通知序号
	@Column(column = "pkID")
	private String pkID;

	//用户自己的id
	@Column(column = "userSeqId")
	private String userSeqId;
	//用户自己的usercard jsonString
	@Column(column = "userCard")
	private String userCard;
	//邀请对象/请求对象 objUserSeqId
	@Column(column = "objUserSeqId")
	private String objUserSeqId;
	//邀请对象/请求对象 usercard jsonString
	@Column(column = "objUserCard")
	private String objUserCard;
	//通知类型
	@Column(column = "notifyType")
	private String notifyType; 
	//圈子名称
	@Column(column = "coterieName")
	private String coterieName;
	//圈子头像
	@Column(column = "coterieFaceUrl")
	private String coterieFaceUrl;
	//圈子id
	@Column(column = "coterieId")
	private String coterieId;
	//请求时间
	@Column(column = "opTime")
	private String opTime;
	//成员数量
	@Column(column = "memberCount")
	private String memberCount;
	//按钮状态
	@Column(column = "state")
	private int state;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPkID() {
		return pkID;
	}
	public void setPkID(String pkID) {
		this.pkID = pkID;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public String getUserCard() {
		return userCard;
	}
	public void setUserCard(String userCard) {
		this.userCard = userCard;
	}
	public String getObjUserSeqId() {
		return objUserSeqId;
	}
	public void setObjUserSeqId(String objUserSeqId) {
		this.objUserSeqId = objUserSeqId;
	}
	public String getObjUserCard() {
		return objUserCard;
	}
	public void setObjUserCard(String objUserCard) {
		this.objUserCard = objUserCard;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public String getCoterieName() {
		return coterieName;
	}
	public void setCoterieName(String coterieName) {
		this.coterieName = coterieName;
	}
	public String getCoterieFaceUrl() {
		return coterieFaceUrl;
	}
	public void setCoterieFaceUrl(String coterieFaceUrl) {
		this.coterieFaceUrl = coterieFaceUrl;
	}
	public String getCoterieId() {
		return coterieId;
	}
	public void setCoterieId(String coterieId) {
		this.coterieId = coterieId;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public String getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "TCircleRequest [id=" + id + ", pkID=" + pkID + ", userSeqId="
				+ userSeqId + ", userCard=" + userCard + ", objUserSeqId="
				+ objUserSeqId + ", objUserCard=" + objUserCard
				+ ", notifyType=" + notifyType + ", coterieName=" + coterieName
				+ ", coterieFaceUrl=" + coterieFaceUrl + ", coterieId="
				+ coterieId + ", opTime=" + opTime + ", memberCount="
				+ memberCount + ", state=" + state + "]";
	}
	/**
	 * 初始表数据，即把接口数据 入到本地sqlite
	 * 
	 * @param db 所属数据库名（userdb）
	 * 
	 */
	@Override
	public  void initTable(final DbUtils db)
	{
		super.initTable(db);
	}
	/**
	 * 增添数据
	 * @throws DbException 
	 * 传入好友请求列表和请求状态hash表
	 */
	public void addrequest(List<CircleRequest> list,HashMap<String, Integer> state) throws DbException{
		for(CircleRequest CRitem :list){
			Log.i("TAG",  CRitem.toString());
			List<TCircleRequest> ulist = userdb.findAll(Selector.from(TCircleRequest.class)
					.where("objUserSeqId", "=", CRitem.getObjUserSeqId())
					.and("coterieId", "=", CRitem.getCoterieId())
					.and("notifyType", "=", CRitem.getNotifyType()));

			//如查不到，则新增；如有记录，更新时间
			if (ulist==null||ulist.isEmpty()) {
				TCircleRequest TCR = new TCircleRequest();
				TCR.setPkID(CRitem.getPkID());
				TCR.setUserSeqId(CRitem.getUserSeqId());
				TCR.setUserCard(UserCard.Usercard2Json(CRitem.getUserCard()));
				TCR.setObjUserSeqId(CRitem.getObjUserSeqId());
				TCR.setObjUserCard(UserCard.Usercard2Json(CRitem.getObjUserCard()));
				TCR.setCoterieId(CRitem.getCoterieId());
				TCR.setCoterieFaceUrl(CRitem.getCoterieFaceUrl());
				TCR.setCoterieName(CRitem.getCoterieName());
				TCR.setNotifyType(CRitem.getNotifyType());
				TCR.setOpTime(CRitem.getOpTime());
				TCR.setMemberCount(CRitem.getMemberCount());
				if (CRitem.getNotifyType().equals(11)||CRitem.getNotifyType().equals(12)) {
					TCR.setState(state.get(getKey(CRitem.getObjUserSeqId(), CRitem.getCoterieId())));
				}else{
					TCR.setState(0);
				}
				userdb.save(TCR);
				Log.i("TAG", TCR.toString());
			}else{
				TCircleRequest tcr = ulist.get(0);
				tcr.setOpTime(CRitem.getOpTime());
				tcr.setPkID(CRitem.getPkID());
				tcr.setState(state.get(getKey(CRitem.getObjUserSeqId(), CRitem.getCoterieId())));
				userdb.update(tcr, new String[]{"opTime","pkID","state"});
				Log.i("TAG", "update complete");
			}
		}
		Log.i("TAG", "add ok");
	}
	/**
	 * 查询表内已有数据,返回List<CircleRequest> templist
	 */
	public List<CircleRequest> getlist(String userid) throws DbException{
		List<TCircleRequest> list =  userdb.findAll(Selector.from(TCircleRequest.class)
				.where("userSeqId", "=", userid));
		List<CircleRequest> templist = new ArrayList<CircleRequest>();
		if (list!=null&&list.size()!=0) {
			for (TCircleRequest TCRitem :list) {
				CircleRequest item = new CircleRequest();
				item.setUserSeqId(TCRitem.getUserSeqId());
				item.setUserCard(UserCard.json2UserCard(TCRitem.getUserCard()));
				item.setPkID(TCRitem.getPkID());
				item.setObjUserSeqId(TCRitem.getObjUserSeqId());
				item.setObjUserCard(UserCard.json2UserCard(TCRitem.getObjUserCard()));
				item.setCoterieName(TCRitem.getCoterieName());
				item.setCoterieId(TCRitem.getCoterieId());
				item.setCoterieFaceUrl(TCRitem.getCoterieFaceUrl());
				item.setMemberCount(TCRitem.getMemberCount());
				item.setNotifyType(TCRitem.getNotifyType());
				item.setOpTime(TCRitem.getOpTime());
				templist.add(item);
			}
		}
		return templist;
	}
	/**
	 * 查询表内已有数据,HashMap<String, Integer> state
	 */
	public HashMap<String, Integer> getStatelist(String userid) throws DbException{
		List<TCircleRequest> list =  userdb.findAll(Selector.from(TCircleRequest.class)
				.where("userSeqId", "=", userid));
		HashMap<String, Integer> statelist = new HashMap<String, Integer>();
		if (list!=null&&list.size()!=0) {
			for (TCircleRequest TCRitem :list) {
				if (TCRitem.getNotifyType().equals("11")||TCRitem.getNotifyType().endsWith("12")) {
					StringBuffer sb = new StringBuffer(TCRitem.getObjUserSeqId());
					sb.append(TCRitem.getCoterieId());
					statelist.put(sb.toString(),TCRitem.getState());
				}
			}
		}
		return statelist;
	}
	//更改请求状态（同意→已同意） 传参：通知发起人id，圈子id，通知类型
	public void agreeRequest(String Objuserid,String circleID,String NotifyType) throws DbException{
		List<TCircleRequest> ulist = userdb.findAll(Selector.from(TCircleRequest.class)
				.where("objUserSeqId", "=",Objuserid)
				.and("coterieId", "=", circleID)
				.and("notifyType", "=", NotifyType));
		if (ulist==null||ulist.isEmpty()) {

		}else{
			TCircleRequest TCRitem = ulist.get(0);
			TCRitem.setState(1);
			userdb.update(TCRitem, "State");
			Log.i("TAG", "agree complete");
		}
	}
	//删除单条请求记录（用户手动删除）
	public void deleteRequest(CircleRequest item) throws DbException{
		List<TCircleRequest> ulist = userdb.findAll(Selector.from(TCircleRequest.class)
				.where("objUserSeqId", "=",item.getObjUserSeqId())
				.and("coterieId", "=", item.getCoterieId())
				.and("notifyType", "=",item.getNotifyType()));
		if (ulist==null||ulist.isEmpty()) {

		}else{
			TCircleRequest tm = ulist.get(0);
			userdb.delete(tm);
			Log.i("TAG", "delete single complete");
		}
	}
	//删除所有请求记录
	public void deleteAllRequest() throws DbException{
		List<TCircleRequest> ulist = userdb.findAll(Selector.from(TCircleRequest.class)
				.where("userSeqId", "=",Constant.userInfo.getUserSeqId()));
		if (ulist==null||ulist.isEmpty()) {

		}else{
			userdb.deleteAll(ulist);
			Log.i("TAG", "delete all complete");
		}
	}
	//获得hash表的key值
	public String getKey(String userID,String circleID){
		StringBuffer sb = new StringBuffer(userID);
		sb.append(circleID);
		String key = sb.toString();
		return key;
	}
}
