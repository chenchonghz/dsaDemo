package com.szrjk.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.szrjk.config.Constant;
import com.szrjk.message.MessageListEntity;
/**
 * 用户好友请求-数据库存储实体类
 * @author 郑斯铭
 *
 */
@Table(name = "TFriendRequest")
public class TFriendRequest extends AbstractUserEntity<TFriendRequest>{
	private static final  String TAG = "TFriendRequest";
	@Id(column ="RequestID")@Transient
	private int id;
	//用户自己的userid
	@Column(column = "selfID")
	private String selfUserId;

	//好友请求里的usercard
	//id
	@Column(column = "ObjUserID")
	private String objUserid;
	//姓名
	@Column(column = "ObjUsername")
	private String objUsername;
	//职称
	@Column(column = "ObjUserPtitle")
	private String objUserPtitle;
	//医院
	@Column(column = "ObjUserHospital")
	private String objUserHospital;
	//科室
	@Column(column = "ObjUserdept")
	private String objUserdept;
	//头像
	@Column(column = "objUserfaceurl")
	private String objUserfaceurl;
	//是否加v
	@Column(column = "objUserfacelevel")
	private String objUserfacelevel;
	//用户类型
	@Column(column = "objUserfacetype")
	private String objUserfacetype;
	//发送时间
	@Column(column = "requestDate")
	private String requestDate;
	//请求状态
	@Column(column = "requestState")
	private int requestState;
	//请求desc
	@Column(column = "requestDesc")
	private String requestDesc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSelfUserId() {
		return selfUserId;
	}
	public void setSelfUserId(String selfUserId) {
		this.selfUserId = selfUserId;
	}
	public String getObjUserid() {
		return objUserid;
	}
	public void setObjUserid(String objUserid) {
		this.objUserid = objUserid;
	}
	public String getObjUsername() {
		return objUsername;
	}
	public void setObjUsername(String objUsername) {
		this.objUsername = objUsername;
	}
	public String getObjUserPtitle() {
		return objUserPtitle;
	}
	public void setObjUserPtitle(String objUserPtitle) {
		this.objUserPtitle = objUserPtitle;
	}
	public String getObjUserHospital() {
		return objUserHospital;
	}
	public void setObjUserHospital(String objUserHospital) {
		this.objUserHospital = objUserHospital;
	}
	public String getObjUserdept() {
		return objUserdept;
	}
	public void setObjUserdept(String objUserdept) {
		this.objUserdept = objUserdept;
	}
	public String getObjUserfaceurl() {
		return objUserfaceurl;
	}
	public void setObjUserfaceurl(String objUserfaceurl) {
		this.objUserfaceurl = objUserfaceurl;
	}
	public String getObjUserfacelevel() {
		return objUserfacelevel;
	}
	public void setObjUserfacelevel(String objUserfacelevel) {
		this.objUserfacelevel = objUserfacelevel;
	}

	public String getObjUserfacetype() {
		return objUserfacetype;
	}
	public void setObjUserfacetype(String objUserfacetype) {
		this.objUserfacetype = objUserfacetype;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getRequestDesc() {
		return requestDesc;
	}
	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}

	public int getRequestState() {
		return requestState;
	}
	public void setRequestState(int requestState) {
		this.requestState = requestState;
	}

	@Override
	public String toString() {
		return "TFriendRequest [id=" + id + ", selfUserId=" + selfUserId
				+ ", objUserid=" + objUserid + ", objUsername=" + objUsername
				+ ", objUserPtitle=" + objUserPtitle + ", objUserHospital="
				+ objUserHospital + ", objUserdept=" + objUserdept
				+ ", objUserfaceurl=" + objUserfaceurl + ", objUserfacelevel="
				+ objUserfacelevel + ", objUserfacetype=" + objUserfacetype
				+ ", requestDate=" + requestDate + ", requestState="
				+ requestState + ", requestDesc=" + requestDesc + "]";
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
	public void addrequest(List<RequestList> list,HashMap<String, Integer> state) throws DbException{
		for(RequestList mle :list){
			Log.i("TAG", mle.toString());
			List<TFriendRequest> ulist = userdb.findAll(Selector.from(TFriendRequest.class)
					.where("ObjUserID", "=",mle.getUserCard().getUserSeqId()));
			//如查不到，则新增；如有记录，更新时间
			if (ulist==null||ulist.isEmpty()) {
				TFriendRequest tm= new TFriendRequest();
				tm.setSelfUserId(Constant.userInfo.getUserSeqId());
				tm.setObjUserid(mle.getUserCard().getUserSeqId());
				tm.setObjUsername(mle.getUserCard().getUserName());
				tm.setObjUserPtitle(mle.getUserCard().getProfessionalTitle());
				tm.setObjUserHospital(mle.getUserCard().getCompanyName());
				tm.setObjUserdept(mle.getUserCard().getDeptName());
				tm.setObjUserfaceurl(mle.getUserCard().getUserFaceUrl());
				tm.setObjUserfacelevel(mle.getUserCard().getUserLevel());
				tm.setObjUserfacetype(mle.getUserCard().getUserType());
				tm.setRequestDate(mle.getRequestDate());
				tm.setRequestDesc(mle.getRequestDesc());
				tm.setRequestState(state.get(mle.getUserCard().getUserSeqId()));
				userdb.save(tm);
				Log.i("TAG", tm.toString());
			}else{
				TFriendRequest tm = ulist.get(0);
				tm.setRequestDate(mle.getRequestDate());
				userdb.update(tm, "requestDate");
				tm.setRequestState(state.get(mle.getUserCard().getUserSeqId()));
				userdb.update(tm, "requestState");
				Log.i("TAG", "update complete");
			}
		}
		Log.i("TAG", "add ok");
	}
	/**
	 * 查询表内已有数据,返回List<RequestList> templist
	 */
	public List<RequestList> getlist(String userid) throws DbException{
		List<TFriendRequest> list =  userdb.findAll(Selector.from(TFriendRequest.class));
		List<RequestList> templist = new ArrayList<RequestList>();
		if (list!=null&&list.size()!=0) {
			for (TFriendRequest tm :list) {
				if(tm.getSelfUserId().equals(userid)){
					RequestList item = new RequestList();
					UserCard tempUC= new UserCard();
					tempUC.setUserSeqId(tm.getObjUserid());
					tempUC.setUserName(tm.getObjUsername());
					tempUC.setCompanyName(tm.getObjUserHospital());
					tempUC.setProfessionalTitle(tm.getObjUserPtitle());
					tempUC.setDeptName(tm.getObjUserdept());
					tempUC.setUserFaceUrl(tm.getObjUserfaceurl());
					tempUC.setUserLevel(tm.getObjUserfacelevel());
					tempUC.setUserType(tm.getObjUserfacetype());
					item.setUserCard(tempUC);
					item.setRequestDesc(tm.getRequestDesc());
					item.setRequestDate(tm.getRequestDate());
					templist.add(item);
				}
			}
		}
		return templist;
	}
	/**
	 * 查询表内已有数据,HashMap<String, Integer> state
	 */
	public HashMap<String, Integer> getStatelist(String userid) throws DbException{
		List<TFriendRequest> list =  userdb.findAll(Selector.from(TFriendRequest.class));
		HashMap<String, Integer> statelist = new HashMap<String, Integer>();
		if (list!=null&&list.size()!=0) {
			for (TFriendRequest tm :list) {
				if(tm.getSelfUserId().equals(userid)){
					statelist.put(tm.getObjUserid(), tm.getRequestState());
				}
			}
		}
		return statelist;
	}
	//更改请求状态（同意→已同意）
	public void agreeRequest(String Objuserid) throws DbException{
		List<TFriendRequest> ulist = userdb.findAll(Selector.from(TFriendRequest.class)
				.where("ObjUserID", "=",Objuserid));
		if (ulist==null||ulist.isEmpty()) {

		}else{
			TFriendRequest tm = ulist.get(0);
			tm.setRequestState(2);
			userdb.update(tm, "requestState");
			Log.i("TAG", "agree complete");
		}
	}
	//删除单条请求记录（用户手动删除）
	public void deleteRequest(RequestList item) throws DbException{
		List<TFriendRequest> ulist = userdb.findAll(Selector.from(TFriendRequest.class)
				.where("ObjUserID", "=",item.getUserCard().getUserSeqId()));
		if (ulist==null||ulist.isEmpty()) {

		}else{
			TFriendRequest tm = ulist.get(0);
			userdb.delete(tm);
			Log.i("TAG", "delete single complete");
		}
	}
	//删除所有请求记录
	public void deleteAllRequest() throws DbException{
		List<TFriendRequest> ulist = userdb.findAll(Selector.from(TFriendRequest.class)
				.where("ObjUserID", "=",Constant.userInfo.getUserSeqId()));
		if (ulist==null||ulist.isEmpty()) {

		}else{
			userdb.deleteAll(ulist);
			Log.i("TAG", "delete all complete");
		}
	}
}
