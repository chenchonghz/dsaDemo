package com.szrjk.entity;

import java.io.Serializable;

public class OtherHomePageInfo implements Serializable{
	//用户id
		private String userSeqId;
		//用户姓名
		private String userName;
		//用户性别
		private String sex;
		//用户头像地址
		private String userFaceUrl;
		//用户省份
		private String province;
		//用户城市
		private String cityCode;
		//用户公司/医院
		private String companyName;
		//用户科室
		private String deptName;
		//用户职称
		private String professionalTitle;
		//用户背景墙地址
		private String backgroundUrl;
		//是否好友
		private String isFriend;
		//是否关注
		private String isFollow;
		//用户生日
		private String birthdate;
		//type
		private String userType;
		//用户level
		private String userLevel;
		//入学时间
		private String entrySchoolDate;
		//学历
		private String educationLevel;
		//媒体类型
		private String mediaType;
		//职位
		private String jobTitle;
		
		public OtherHomePageInfo() {
			super();
		}
		public OtherHomePageInfo(String userSeqId, String userName, String sex,
				String userFaceUrl, String province, String cityCode,
				String companyName, String deptName, String professionalTitle,
				String backgroundUrl, String isFriend, String isFollow,
				String birthdate, String userType, String userLevel,
				String entrySchoolDate, String educationLevel, String mediaType,
				String jobTitle) {
			super();
			this.userSeqId = userSeqId;
			this.userName = userName;
			this.sex = sex;
			this.userFaceUrl = userFaceUrl;
			this.province = province;
			this.cityCode = cityCode;
			this.companyName = companyName;
			this.deptName = deptName;
			this.professionalTitle = professionalTitle;
			this.backgroundUrl = backgroundUrl;
			this.isFriend = isFriend;
			this.isFollow = isFollow;
			this.birthdate = birthdate;
			this.userType = userType;
			this.userLevel = userLevel;
			this.entrySchoolDate = entrySchoolDate;
			this.educationLevel = educationLevel;
			this.mediaType = mediaType;
			this.jobTitle = jobTitle;
		}
		public String getUserSeqId() {
			return userSeqId;
		}
		public void setUserSeqId(String userSeqId) {
			this.userSeqId = userSeqId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getUserFaceUrl() {
			return userFaceUrl;
		}
		public void setUserFaceUrl(String userFaceUrl) {
			this.userFaceUrl = userFaceUrl;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCityCode() {
			return cityCode;
		}
		public void setCityCode(String cityCode) {
			this.cityCode = cityCode;
		}
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		public String getDeptName() {
			return deptName;
		}
		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}
		public String getProfessionalTitle() {
			return professionalTitle;
		}
		public void setProfessionalTitle(String professionalTitle) {
			this.professionalTitle = professionalTitle;
		}
		public String getBackgroundUrl() {
			return backgroundUrl;
		}
		public void setBackgroundUrl(String backgroundUrl) {
			this.backgroundUrl = backgroundUrl;
		}
		public String getIsFriend() {
			return isFriend;
		}
		public void setIsFriend(String isFriend) {
			this.isFriend = isFriend;
		}
		public String getIsFollow() {
			return isFollow;
		}
		public void setIsFollow(String isFollow) {
			this.isFollow = isFollow;
		}
		public String getBirthdate() {
			return birthdate;
		}
		public void setBirthdate(String birthdate) {
			this.birthdate = birthdate;
		}
		public String getUserType() {
			return userType;
		}
		public void setUserType(String userType) {
			this.userType = userType;
		}
		public String getUserLevel() {
			return userLevel;
		}
		public void setUserLevel(String userLevel) {
			this.userLevel = userLevel;
		}
		public String getEntrySchoolDate() {
			return entrySchoolDate;
		}
		public void setEntrySchoolDate(String entrySchoolDate) {
			this.entrySchoolDate = entrySchoolDate;
		}
		public String getEducationLevel() {
			return educationLevel;
		}
		public void setEducationLevel(String educationLevel) {
			this.educationLevel = educationLevel;
		}
		public String getMediaType() {
			return mediaType;
		}
		public void setMediaType(String mediaType) {
			this.mediaType = mediaType;
		}
		
		public String getJobTitle() {
			return jobTitle;
		}
		public void setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
		}
		@Override
		public String toString() {
			return "UserHomePageInfo [userSeqId=" + userSeqId + ", userName="
					+ userName + ", sex=" + sex + ", userFaceUrl=" + userFaceUrl
					+ ", province=" + province + ", cityCode=" + cityCode
					+ ", companyName=" + companyName + ", deptName=" + deptName
					+ ", professionalTitle=" + professionalTitle
					+ ", backgroundUrl=" + backgroundUrl + ", isFriend=" + isFriend
					+ ", isFollow=" + isFollow + ", birthdate=" + birthdate
					+ ", userType=" + userType + ", userLevel=" + userLevel
					+ ", entrySchoolDate=" + entrySchoolDate + ", educationLevel="
					+ educationLevel + ", mediaType=" + mediaType + 
					", jobTitle="+jobTitle+"]";
		}
	
	
	


}
