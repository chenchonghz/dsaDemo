package com.szrjk.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

@Table(name = "TMESSAGE")
public class TMessage extends AbstractUserEntity<TMessage>{

	private static final  String TAG = "TMESSAGE";
	@Id(column ="messageID")@Transient
	private int id;

	@Column(column = "selfID")
	private String selfUserId;

	@Column(column = "ObjUserID")
	private String objUserid;
	@Column(column = "ObjUsername")
	private String objUsername;
	@Column(column = "ObjUserPtitle")
	private String objUserPtitle;
	@Column(column = "ObjUserHospital")
	private String objUserHospital;
	@Column(column = "ObjUserdept")
	private String objUserdept;
	@Column(column = "objUserfaceurl")
	private String objUserfaceurl;
	@Column(column = "objUserfacelevle")
	private String objUserfacelevel;
	@Column(column = "lasttime")
	private String lasttime;
	@Column(column = "messageBackground")
	private String messageBackground;


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

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
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

	public String getMessageBackground() {
		return messageBackground;
	}

	public void setMessageBackground(String messageBackground) {
		this.messageBackground = messageBackground;
	}


	@Override
	public String toString() {
		return "TMessage [id=" + id + ", selfUserId=" + selfUserId
				+ ", objUserid=" + objUserid + ", objUsername=" + objUsername
				+ ", objUserPtitle=" + objUserPtitle + ", objUserHospital="
				+ objUserHospital + ", objUserdept=" + objUserdept
				+ ", objUserfaceurl=" + objUserfaceurl + ", objUserfacelevel="
				+ objUserfacelevel + ", lasttime=" + lasttime
				+ ", messageBackground=" + messageBackground + "]";
	}

	//	@Override
	//	public String getCURRENT_VERSION() {
	//		// TODO Auto-generated method stub
	//		return  "CURRENT_VERSION_TMESSAGE";
	//	}
	//
	//	@Override
	//	public String getTAG() {
	//		// TODO Auto-generated method stub
	//		return TAG;
	//	}
	//
	//	@Override
	//	public String getTableId() {
	//		// TODO Auto-generated method stub
	//		return "region";
	//	}
	//
	//	@Override
	//	public Class gettheclass() {
	//		// TODO Auto-generated method stub
	//		return TMessage.class;
	//	}
	/**
	 * 初始表数据，即把接口数据 入到本地sqlite
	 * 
	 * @param db
	 * @param iversion
	 *            版本号
	 */
	@Override
	public  void initTable(final DbUtils db)
	{
		super.initTable(db);
	}
	/**
	 * 增添数据
	 * @throws DbException 
	 */

	public void addmessage(List<MessageListEntity> list) throws DbException{
		for(MessageListEntity mle :list){
			Log.i("TAG", mle.toString());
			if (mle.getReceiveUserCard().getUserSeqId().equals(Constant.userInfo.getUserSeqId())) 
			{
				List<TMessage> ulist = userdb.findAll(Selector.from(TMessage.class)
						.where("ObjUserID", "=",mle.getSendUserCard().getUserSeqId()));
				if (ulist==null||ulist.isEmpty()) {
					TMessage tm= new TMessage();
					tm.setSelfUserId(mle.getReceiveUserCard().getUserSeqId());
					tm.setObjUserid(mle.getSendUserCard().getUserSeqId());
					tm.setObjUsername(mle.getSendUserCard().getUserName());
					tm.setObjUserPtitle(mle.getSendUserCard().getProfessionalTitle());
					tm.setObjUserHospital(mle.getSendUserCard().getCompanyName());
					tm.setObjUserdept(mle.getSendUserCard().getDeptName());
					tm.setObjUserfaceurl(mle.getSendUserCard().getUserFaceUrl());
					tm.setObjUserfacelevel(mle.getSendUserCard().getUserLevel());
					tm.setLasttime(mle.getCreateDate());
					userdb.save(tm);
					Log.i("TAG", tm.toString());

				}else{
					TMessage tm = ulist.get(0);
					tm.setLasttime(mle.getCreateDate());
					userdb.update(tm, "lasttime");
					Log.i("TAG", "update complete");
				}
			}
		}
		Log.i("TAG", "ok");
	}
	//发送消息时的数据库更新
	public void addmessage(UserCard objUserCard,String messagetime) throws DbException{
		List<TMessage> ulist = userdb.findAll(Selector.from(TMessage.class)
				.where("ObjUserID", "=",objUserCard.getUserSeqId()));
		if (ulist==null||ulist.isEmpty()) {
			TMessage tm= new TMessage();
			tm.setSelfUserId(Constant.userInfo.getUserSeqId());
			tm.setObjUserid(objUserCard.getUserSeqId());
			tm.setObjUsername(objUserCard.getUserName());
			tm.setObjUserPtitle(objUserCard.getProfessionalTitle());
			tm.setObjUserHospital(objUserCard.getCompanyName());
			tm.setObjUserdept(objUserCard.getDeptName());
			tm.setObjUserfaceurl(objUserCard.getUserFaceUrl());
			tm.setObjUserfacelevel(objUserCard.getUserLevel());
			tm.setLasttime(messagetime);
			userdb.save(tm);
			Log.i("TAG", tm.toString());
		}else{
			TMessage tm = ulist.get(0);
			tm.setLasttime(messagetime);
			userdb.update(tm, "lasttime");
			Log.i("TAG", "update complete");
		}
	}
	/*
	 * 获得usercard
	 */

	public List<TMessage> getlist(Activity activity) throws DbException{
		List<TMessage> list =  userdb.findAll(Selector.from(TMessage.class));
		return list;
	}
	public List<TMessage> getlist(Activity activity,String userid) throws DbException{
		List<TMessage> list =  userdb.findAll(Selector.from(TMessage.class));
		List<TMessage> templist = new ArrayList<TMessage>();
		if (list!=null&&list.size()!=0) {
			for (TMessage tm :list) {
				if(tm.getSelfUserId().equals(userid)){
					templist.add(tm);
				}
			}
		}
		return templist;

	}
	//写入背景url
	public void addBackground(UserCard objUserCard,String bgURL) throws DbException{
		List<TMessage> ulist = userdb.findAll(Selector.from(TMessage.class)
				.where("ObjUserID", "=",objUserCard.getUserSeqId()));
		if (ulist!=null&&ulist.size()!=0) {
			for (TMessage tm:ulist) {
				if (tm.getSelfUserId().equals(Constant.userInfo.getUserSeqId())) {
					tm.setMessageBackground(bgURL);
					userdb.update(tm, "messageBackground");
					Log.i("TAG", "update complete");
					break;
				}
			}
		}else{
			TMessage tm= new TMessage();
			tm.setSelfUserId(Constant.userInfo.getUserSeqId());
			tm.setObjUserid(objUserCard.getUserSeqId());
			tm.setObjUsername(objUserCard.getUserName());
			tm.setObjUserPtitle(objUserCard.getProfessionalTitle());
			tm.setObjUserHospital(objUserCard.getCompanyName());
			tm.setObjUserdept(objUserCard.getDeptName());
			tm.setObjUserfaceurl(objUserCard.getUserFaceUrl());
			tm.setObjUserfacelevel(objUserCard.getUserLevel());
			tm.setMessageBackground(bgURL);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date =df.format(new Date());
			tm.setLasttime(date);
			userdb.save(tm);
			
			Log.i("TAG", tm.toString());
		}
	}
	//返回背景url
	public String getBackground(UserCard objUserCard) throws DbException{
		List<TMessage> ulist = userdb.findAll(Selector.from(TMessage.class)
				.where("ObjUserID", "=",objUserCard.getUserSeqId()));
		if (ulist!=null&&ulist.size()!=0) {
			for (TMessage tm:ulist) {
				if (tm.getSelfUserId().equals(Constant.userInfo.getUserSeqId())) {
					String url = tm.getMessageBackground();
					Log.i("TAG", "search complete");
					return url;
				}
			}
			
		}
		return null;
	}

}
