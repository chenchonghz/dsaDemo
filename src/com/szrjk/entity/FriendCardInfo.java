package com.szrjk.entity;

public class FriendCardInfo {
		//用户id
		private String userSeqId;
		//用户姓名
		private String userName;
		//用户头像地址
		private String userFaceUrl;
		//用户公司/医院
		private String company;
		//用户科室
		private String department;
		//用户职称
		private String professionalTitle;
		//type
		private String userType;
		public FriendCardInfo() {
			super();
		}
		public FriendCardInfo(String userSeqId, String userName,
				String userFaceUrl, String company, String department,
				String professionalTitle, String userType) {
			super();
			this.userSeqId = userSeqId;
			this.userName = userName;
			this.userFaceUrl = userFaceUrl;
			this.company = company;
			this.department = department;
			this.professionalTitle = professionalTitle;
			this.userType = userType;
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
		public String getUserFaceUrl() {
			return userFaceUrl;
		}
		public void setUserFaceUrl(String userFaceUrl) {
			this.userFaceUrl = userFaceUrl;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		public String getProfessionalTitle() {
			return professionalTitle;
		}
		public void setProfessionalTitle(String professionalTitle) {
			this.professionalTitle = professionalTitle;
		}
		public String getUserType() {
			return userType;
		}
		public void setUserType(String userType) {
			this.userType = userType;
		}
		@Override
		public String toString() {
			return "FriendCardInfo [userSeqId=" + userSeqId + ", userName="
					+ userName + ", userFaceUrl="
					+ userFaceUrl + ", company=" + company + ", department="
					+ department + ", professionalTitle=" + professionalTitle
					+ ", userType=" + userType + "]";
		}
		
}
