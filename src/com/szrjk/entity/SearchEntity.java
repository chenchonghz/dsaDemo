package com.szrjk.entity;

/**searchType搜索类型：00不指定则搜索全局, 01药物, 02疾病, 03病例, 04论文, 
 * 05知识点目录, 06医学计算器, 07医学图谱, 08用户帖子, 09用户评论内容, 10用户昵称, 11好友聊天记录,…
(后端根据渠道匹配相关搜索类型推送到前端)*/
public class SearchEntity {
	
	private String searchType;//搜索类型
	private String recordId;//记录ID
	private String recordType;//记录类型
	private String title;//标题
	private String author;//所有者
	private String summary;//摘要
	private String pictureURL;//图片URL
	private String updateTime;//更新时间
	
	// dcust用户
		private String user_name;//名字
		private String sex;
		private String user_type;
		private String company_name;//单位或者学校
		private String dept_name;//部门或者科室
		private String professional_title;//职称或者职位
		private String user_level;//认证等级
		private String user_face_url;

		// dcust圈子
		private String coterie_desc;
		private String coterie_name;//圈子名字
		private String user_seq_id;
		private String coterie_face_url;

	
	
	public SearchEntity() {
		super();
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPictureURL() {
		return pictureURL;
	}
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getProfessional_title() {
		return professional_title;
	}
	public void setProfessional_title(String professional_title) {
		this.professional_title = professional_title;
	}
	public String getUser_level() {
		return user_level;
	}
	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}
	public String getUser_face_url() {
		return user_face_url;
	}
	public void setUser_face_url(String user_face_url) {
		this.user_face_url = user_face_url;
	}
	public String getCoterie_desc() {
		return coterie_desc;
	}
	public void setCoterie_desc(String coterie_desc) {
		this.coterie_desc = coterie_desc;
	}
	public String getCoterie_name() {
		return coterie_name;
	}
	public void setCoterie_name(String coterie_name) {
		this.coterie_name = coterie_name;
	}
	public String getUser_seq_id() {
		return user_seq_id;
	}
	public void setUser_seq_id(String user_seq_id) {
		this.user_seq_id = user_seq_id;
	}
	public String getCoterie_face_url() {
		return coterie_face_url;
	}
	public void setCoterie_face_url(String coterie_face_url) {
		this.coterie_face_url = coterie_face_url;
	}
	@Override
	public String toString() {
		return "SearchEntity [searchType=" + searchType + ", recordId="
				+ recordId + ", recordType=" + recordType + ", title=" + title
				+ ", author=" + author + ", summary=" + summary
				+ ", pictureURL=" + pictureURL + ", updateTime=" + updateTime
				+ ", user_name=" + user_name + ", sex=" + sex + ", user_type="
				+ user_type + ", company_name=" + company_name + ", dept_name="
				+ dept_name + ", professional_title=" + professional_title
				+ ", user_level=" + user_level + ", user_face_url="
				+ user_face_url + ", coterie_desc=" + coterie_desc
				+ ", coterie_name=" + coterie_name + ", user_seq_id="
				+ user_seq_id + ", coterie_face_url=" + coterie_face_url + "]";
	}
	
	
	
	
	
}
