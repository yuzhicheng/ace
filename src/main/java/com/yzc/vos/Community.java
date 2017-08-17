package com.yzc.vos;
import com.yzc.vos.validator.Create;
import com.yzc.vos.validator.Modify;
import com.yzc.vos.validator.Other;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class Community implements Serializable{

	private static final long serialVersionUID = -8279849649107958893L;
	private Integer id;
	/** 省ID */
    @NotBlank(message = "地区省不能为空", groups = { Create.class, Modify.class })
	private String province;
	/** 市 */
	private String city;
	/** 区 */
	private String area;
	
	/**名称 */
    @NotBlank(message = "学校名称不能为空", groups = { Create.class, Modify.class })
	private String name;
	
	/** 上级教育行政主管部门 */
    @NotBlank(message = "上级教育行政主管部门不能为空", groups = { Create.class, Modify.class })
    @Length(max = 100, message = "上级教育行政主管部门不能超过100个字符", groups={Create.class, Modify.class})
	private String chargeDep;
	
	/** 学校类型 */
    @NotNull(message = "学校类型不能为空", groups = { Create.class, Modify.class })
	private Integer schoolType;
	
	private String schoolTypeDesc;
	/** 办学水平*/
    @NotNull(message = "办学水平不能为空", groups = { Create.class, Modify.class })
	private Integer schoolLevel;
	/** 办学水平*/
	private Integer level;
	
	private String levelDesc;
	/** 官网 */
	@Length(max = 255, message = "官网不能超过255个字符", groups={Create.class, Modify.class})
	private String websites;
	/** 微信公众号 */
	@Length(max = 100, message = "微信公众号不能超过100个字符", groups={Create.class, Modify.class})
	private String wiexinNumber;
	
	/** 联系人姓名*/
    @NotBlank(message = "联系人姓名不能为空", groups = { Create.class, Modify.class })
    @Length(max = 50, message = "联系人姓名不能超过50个字符", groups={Create.class, Modify.class})
	private String fullName;
	/** 职务*/
    @NotBlank(message = "职务不能为空", groups = { Create.class, Modify.class })
    @Length(max = 50, message = "职务不能超过50个字符", groups={Create.class, Modify.class})
	private String posts;
	/** 电话 */
	@Length(max = 15, message = "办公电话不能超过15个字符", groups={Create.class, Modify.class})
	private String tel;
	/** 手机号 */
    @NotBlank(message = "手机号不能为空", groups = { Create.class, Modify.class })
	@Length(max = 15, message = "手机不能超过15个字符", groups={Create.class, Modify.class})
	private String phone;
	/** 邮箱 */
    @NotBlank(message = "邮箱不能为空", groups = { Create.class, Modify.class })
	@Email(message="邮箱格式不正确", groups={Create.class, Modify.class})
	@Length(max = 50, message = "邮箱不能超过50个字符", groups={Create.class, Modify.class})
	private String mail;
	/**qq */
    @NotBlank(message = "qq不能为空", groups = { Create.class, Modify.class })
	@Length(max = 32, message = "qq不能超过32个字符", groups={Create.class, Modify.class})
	private String qq;
	/** 地址 */
    @NotBlank(message = "地址不能为空", groups = { Create.class, Modify.class })
	@Length(max = 255, message = "地址不能超过255个字符", groups={Create.class, Modify.class})
	private String address;
	/**附件多个;分隔*/
	private String files;
	
	/** 标签 0表示基地校, 1表示项目校 */
    @NotNull(message = "申请类型不能为空", groups = { Create.class, Modify.class })
	private Integer label;
	
	/** 审核 */
	private Integer status;
	/** 审核人UID */
	private String auditUid;
	/** 审核人名称*/
	private String auditUserName;
	/** 原因 */
    @Length(max = 255, message = "原因不能超过255个字符", groups = { Other.class })
	private String reason;
	
	/** 0表示未发送, 1表示发送*/
	@Range(min=0,max=1,message="发送邮件状态值必须为0或1")
	private Integer sendMail;
	/**当前用户id**/
	private String userId;

	/**管理员id**/
	private String adminId;

	private Date addTime;
	private Date auditTime;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChargeDep() {
		return chargeDep;
	}
	public void setChargeDep(String chargeDep) {
		this.chargeDep = chargeDep;
	}
	public Integer getSchoolType() {
		return schoolType;
	}
	public void setSchoolType(Integer schoolType) {
		this.schoolType = schoolType;
	}
	public String getSchoolTypeDesc() {
		return schoolTypeDesc;
	}
	public void setSchoolTypeDesc(String schoolTypeDesc) {
		this.schoolTypeDesc = schoolTypeDesc;
	}
	public Integer getSchoolLevel() {
		return schoolLevel;
	}
	public void setSchoolLevel(Integer schoolLevel) {
		this.schoolLevel = schoolLevel;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getLevelDesc() {
		return levelDesc;
	}
	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}
	public String getWebsites() {
		return websites;
	}
	public void setWebsites(String websites) {
		this.websites = websites;
	}
	public String getWiexinNumber() {
		return wiexinNumber;
	}
	public void setWiexinNumber(String wiexinNumber) {
		this.wiexinNumber = wiexinNumber;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPosts() {
		return posts;
	}
	public void setPosts(String posts) {
		this.posts = posts;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public Integer getLabel() {
		return label;
	}
	public void setLabel(Integer label) {
		this.label = label;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getAuditUid() {
		return auditUid;
	}
	public void setAuditUid(String auditUid) {
		this.auditUid = auditUid;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSendMail() {
		return sendMail;
	}
	public void setSendMail(Integer sendMail) {
		this.sendMail = sendMail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	@Override
	public String toString() {
		return "Community [id=" + id + ", province=" + province + ", city="
				+ city + ", area=" + area + ", name=" + name + ", chargeDep="
				+ chargeDep + ", schoolType=" + schoolType
				+ ", schoolTypeDesc=" + schoolTypeDesc + ", schoolLevel="
				+ schoolLevel + ", level=" + level + ", levelDesc=" + levelDesc
				+ ", websites=" + websites + ", wiexinNumber=" + wiexinNumber
				+ ", fullName=" + fullName + ", posts=" + posts + ", tel="
				+ tel + ", phone=" + phone + ", mail=" + mail + ", qq=" + qq
				+ ", address=" + address + ", files=" + files + ", label="
				+ label + ", status=" + status + ", auditUid=" + auditUid
				+ ", auditUserName=" + auditUserName + ", reason=" + reason
				+ ", sendMail=" + sendMail + ", userId=" + userId + ", adminId="
				+ adminId + ", addTime=" + addTime
				+ ", auditTime=" + auditTime + "]";
	}
	
}
