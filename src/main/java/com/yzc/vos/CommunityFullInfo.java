/* =============================================================
 * Created: [2017年5月16日] by tangcc
 * =============================================================
 *
 * Copyright 2014-2015 NetDragon Websoft Inc. All Rights Reserved
 *
 * =============================================================
 */

package com.yzc.vos;

import com.yzc.vos.validator.Create;
import com.yzc.vos.validator.Modify;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 共同体全部信息，包含Community中原有信息以及新增的信息
 * @author tangcc
 * @date 2017年5月16日 上午10:17:52
 *
 */
public class CommunityFullInfo extends Community {

    /**
     * 
     */
    private static final long serialVersionUID = -6406058112809542705L;

    /** 建校时间 */
    @NotNull(message = "建校时间不能为空", groups = { Create.class, Modify.class })
    private String schoolCreateTime;

    /** 基地校类型（1-区县基地校，2-地市基地校）   */
    @Range(min = 1, max = 2, message = "基地校类型只能取值1或2", groups = { Create.class, Modify.class })
    private Integer baseSchoolType;

    /** 基地证书编号 */
    @Pattern(regexp = "^[a-zA-Z0-9]{1,15}$", message = "基地校证书编号格式不正确", groups = { Create.class, Modify.class })
    private String schoolCertNo;

    /** 学校规模：年级个数 */
    @NotNull(message = "学校规模：年级个数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学校规模：年级个数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer schoolGradeNum;

    /** 学校规模：班级个数 */
    @NotNull(message = "学校规模：班级个数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学校规模：班级个数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer schoolClassNum;

    /** 学校规模：学生人数 */
    @NotNull(message = "学校规模：学生人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学校规模：学生人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer schoolStudentNum;

    /** 学校规模：教师人数 */
    @NotNull(message = "学校规模：教师人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学校规模：教师人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer schoolTeacherNum;

    /** 青年教师（35岁及以下）人数 */
    @NotNull(message = "青年教师人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "青年教师人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer youngTeacherNum;

    /** 骨干教师区县级人数 */
    @NotNull(message = "骨干教师区县级人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "骨干教师区县级人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer keyTeacherAreaNum;

    /** 骨干教师地市级人数 */
    @NotNull(message = "骨干教师地市级人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "骨干教师地市级人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer keyTeacherCityNum;

    /** 骨干教师省级人数 */
    @NotNull(message = "骨干教师省级人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "骨干教师省级人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer keyTeacherProvinceNum;

    /** 学科带头人区县级人数 */
    @NotNull(message = "学科带头人区县级人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学科带头人区县级人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer subjectLeaderAreaNum;

    /** 学科带头人地市级人数 */
    @NotNull(message = "学科带头人地市级人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学科带头人地市级人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer subjectLeaderCityNum;

    /** 学科带头人省级人数 */
    @NotNull(message = "学科带头人省级人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学科带头人省级人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer subjectLeaderProvinceNum;

    /** 特级教师人数 */
    @NotNull(message = "特级教师人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "特级教师人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer specialTeacherNum;

    /** 其他荣誉称号教师人数 */
    @NotNull(message = "其他荣誉称号教师人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "其他荣誉称号教师人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer otherTeacherNum;

    /** 校长姓名 */
    @NotBlank(message = "校长姓名不能为空", groups = { Create.class, Modify.class })
    @Length(max = 30, message = "校长姓名不能超过30个字符", groups = { Create.class, Modify.class })
    private String principalName;

    /** 校长性别（1-男，2-女）  */
    @NotNull(message = "校长性别不能为空", groups = { Create.class, Modify.class })
    @Range(min = 1, max = 2, message = "校长性别只能取值1或2", groups = { Create.class, Modify.class })
    private Integer principalGender;

    /** 校长年龄 */
    @NotNull(message = "校长年龄不能为空", groups = { Create.class, Modify.class })
    @Min(value = 1, message = "校长年龄必须大于0", groups = { Create.class, Modify.class })
    private Integer principalAge;

    /** 校长职称 */
    @NotBlank(message = "校长职称不能为空", groups = { Create.class, Modify.class })
    @Length(max = 100, message = "校长职称不能超过100个字符", groups = { Create.class, Modify.class })
    private String principalTitle;

    /** 校长手机号码 */
    @NotBlank(message = "校长手机号码不能为空", groups = { Create.class, Modify.class })
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号不合法", groups = { Create.class, Modify.class })
    private String principalMobile;

    /** 校长任现职时间 */
    @NotNull(message = "校长任现职时间不能为空", groups = { Create.class, Modify.class })
    private String principalServiceTime;

    /** 在其他学校任校长的年限 */
    @NotNull(message = "在其他学校任校长的年限不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "在其他学校任校长的年限必须大于等于0", groups = { Create.class, Modify.class })
    private Integer principalServiceOthersTime;

    /** 所获荣誉 */
    @NotBlank(message = "所获荣誉不能为空", groups = { Create.class, Modify.class })
    @Length(max = 500, message = "所获荣誉不能超过500个字符", groups = { Create.class, Modify.class })
    private String principalHonor;

    /** 手拉手帮扶学校（单位：所） */
    @NotNull(message = "手拉手帮扶学校不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "手拉手帮扶学校必须大于等于0", groups = { Create.class, Modify.class })
    private Integer handInHandSchoolNum;

    /** 手拉手帮扶学校（农村学校，单位：所） */
    @NotNull(message = "手拉手帮扶学校（农村学校）不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "手拉手帮扶学校（农村学校）必须大于等于0", groups = { Create.class, Modify.class })
    private Integer handInHandSchoolVillageNum;

    /** 集团化办学学校（单位：所） */
    @NotNull(message = "集团化办学学校不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "集团化办学学校必须大于等于0", groups = { Create.class, Modify.class })
    private Integer companySchoolNum;

    /** 集团化办学学校（农村学校，单位：所） */
    @NotNull(message = "集团化办学学校（农村学校）不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "集团化办学学校（农村学校）必须大于等于0", groups = { Create.class, Modify.class })
    private Integer companySchoolVillageNum;

    /** 学区制办学学校（单位：所） */
    @NotNull(message = "学区制办学学校不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学区制办学学校必须大于等于0", groups = { Create.class, Modify.class })
    private Integer districtSchoolNum;

    /** 学区制办学学校（农村学校，单位：所） */
    @NotNull(message = "学区制办学学校（农村学校）不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "学区制办学学校（农村学校）必须大于等于0", groups = { Create.class, Modify.class })
    private Integer districtSchoolVillageNum;

    /** 其他形式的共同体（单位：所） */
    @NotNull(message = "其他形式的共同体不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "其他形式的共同体必须大于等于0", groups = { Create.class, Modify.class })
    private Integer otherSchoolNum;

    /** 其他形式的共同体名称 */
    @Length(max = 100, message = "其他形式的共同体名称不能超过100个字符", groups = { Create.class, Modify.class })
    private String otherSchoolName;

    /** 名师工作室（单位：个）  */
    @NotNull(message = "名师工作室不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "名师工作室必须大于等于0", groups = { Create.class, Modify.class })
    private Integer teacherStudio;

    /** 名师工作室覆盖学科（详细填写）  */
    @Length(max = 100, message = "名师工作室覆盖学科不能超过100个字符", groups = { Create.class, Modify.class })
    private String teacherStudioDetail;

    /** 覆盖学校数（单位：所） */
    @NotNull(message = "覆盖学校数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "覆盖学校数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer coverSchoolNum;

    /** 覆盖学校参与人数（单位：人） */
    @NotNull(message = "覆盖学校参与人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "覆盖学校参与人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer coverSchoolPersonNum;

    /** 名校长工作室（单位：个） */
    @NotNull(message = "名校长工作室不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "名校长工作室必须大于等于0", groups = { Create.class, Modify.class })
    private Integer principalStudio;

    /** 名校长工作室带动学校（单位：个） */
    @NotNull(message = "名校长工作室带动学校不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "名校长工作室带动学校必须大于等于0", groups = { Create.class, Modify.class })
    private Integer principalStudioSchoolNum;

    /** 名校长工作室参与人数（单位：人） */
    @NotNull(message = "名校长工作室参与人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "名校长工作室参与人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer principalStudioPersonNum;

    /** 名班主任工作室（单位：个） */
    @NotNull(message = "名班主任工作室不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "名班主任工作室必须大于等于0", groups = { Create.class, Modify.class })
    private Integer masterStudio;

    /** 名班主任工作室带动学校（单位：个） */
    @NotNull(message = "名班主任工作室带动学校不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "名班主任工作室带动学校必须大于等于0", groups = { Create.class, Modify.class })
    private Integer masterStudioSchoolNum;

    /** 名班主任工作室参与人数（单位：人） */
    @NotNull(message = "名班主任工作室参与人数不能为空", groups = { Create.class, Modify.class })
    @Min(value = 0, message = "名班主任工作室参与人数必须大于等于0", groups = { Create.class, Modify.class })
    private Integer masterStudioPersonNum;

    /** 其他  */
    @Length(max = 100, message = "其他不能超过100个字符", groups = { Create.class, Modify.class })
    private String others;

    /** 学校需求意见  */
    @Length(max = 1000, message = "学校需求意见不能超过1000个字符", groups = { Create.class, Modify.class })
    private String schoolSuggestion;

    /** 办学特色  */
    @Length(max = 1000, message = "办学特色不能超过1000个字符", groups = { Create.class, Modify.class })
    private String schoolFeature;

    /** 成果（近五年所获荣誉）  */
    @Length(max = 1000, message = "成果不能超过1000个字符", groups = { Create.class, Modify.class })
    private String schoolAchievements;

    /** 育人特色（多选时值为或运算 1:师生关系,2:教书育人,4:班级建设,8:典型学生问题,16:教师自我发展,32: 其他）   */
    @NotNull(message = "育人特色不能为空", groups = { Create.class, Modify.class })
    @Min(value = 1, message = "育人特色必须大于0", groups = { Create.class, Modify.class })
    private Integer schoolEducateFeature;

    /** 育人特色其他说明  */
    @Length(max = 100, message = "育人特色其他说明不能超过100个字符", groups = { Create.class, Modify.class })
    private String schoolEducateFeatureDesc;

    /** dentryId，存放生成的pdf文件  */
    private String pdfFile;

    /**
     * @return the schoolCreateTime
     */
    public String getSchoolCreateTime() {
        return schoolCreateTime;
    }

    /**
     * @param schoolCreateTime the schoolCreateTime to set
     */
    public void setSchoolCreateTime(String schoolCreateTime) {
        this.schoolCreateTime = schoolCreateTime;
    }

    /**
     * @return the baseSchoolType
     */
    public Integer getBaseSchoolType() {
        return baseSchoolType;
    }

    /**
     * @param baseSchoolType the baseSchoolType to set
     */
    public void setBaseSchoolType(Integer baseSchoolType) {
        this.baseSchoolType = baseSchoolType;
    }

    /**
     * @return the schoolCertNo
     */
    public String getSchoolCertNo() {
        return schoolCertNo;
    }

    /**
     * @param schoolCertNo the schoolCertNo to set
     */
    public void setSchoolCertNo(String schoolCertNo) {
        this.schoolCertNo = schoolCertNo;
    }

    /**
     * @return the schoolGradeNum
     */
    public Integer getSchoolGradeNum() {
        return schoolGradeNum;
    }

    /**
     * @param schoolGradeNum the schoolGradeNum to set
     */
    public void setSchoolGradeNum(Integer schoolGradeNum) {
        this.schoolGradeNum = schoolGradeNum;
    }

    /**
     * @return the schoolClassNum
     */
    public Integer getSchoolClassNum() {
        return schoolClassNum;
    }

    /**
     * @param schoolClassNum the schoolClassNum to set
     */
    public void setSchoolClassNum(Integer schoolClassNum) {
        this.schoolClassNum = schoolClassNum;
    }

    /**
     * @return the schoolStudentNum
     */
    public Integer getSchoolStudentNum() {
        return schoolStudentNum;
    }

    /**
     * @param schoolStudentNum the schoolStudentNum to set
     */
    public void setSchoolStudentNum(Integer schoolStudentNum) {
        this.schoolStudentNum = schoolStudentNum;
    }

    /**
     * @return the schoolTeacherNum
     */
    public Integer getSchoolTeacherNum() {
        return schoolTeacherNum;
    }

    /**
     * @param schoolTeacherNum the schoolTeacherNum to set
     */
    public void setSchoolTeacherNum(Integer schoolTeacherNum) {
        this.schoolTeacherNum = schoolTeacherNum;
    }

    /**
     * @return the youngTeacherNum
     */
    public Integer getYoungTeacherNum() {
        return youngTeacherNum;
    }

    /**
     * @param youngTeacherNum the youngTeacherNum to set
     */
    public void setYoungTeacherNum(Integer youngTeacherNum) {
        this.youngTeacherNum = youngTeacherNum;
    }

    /**
     * @return the keyTeacherAreaNum
     */
    public Integer getKeyTeacherAreaNum() {
        return keyTeacherAreaNum;
    }

    /**
     * @param keyTeacherAreaNum the keyTeacherAreaNum to set
     */
    public void setKeyTeacherAreaNum(Integer keyTeacherAreaNum) {
        this.keyTeacherAreaNum = keyTeacherAreaNum;
    }

    /**
     * @return the keyTeacherCityNum
     */
    public Integer getKeyTeacherCityNum() {
        return keyTeacherCityNum;
    }

    /**
     * @param keyTeacherCityNum the keyTeacherCityNum to set
     */
    public void setKeyTeacherCityNum(Integer keyTeacherCityNum) {
        this.keyTeacherCityNum = keyTeacherCityNum;
    }

    /**
     * @return the keyTeacherProvinceNum
     */
    public Integer getKeyTeacherProvinceNum() {
        return keyTeacherProvinceNum;
    }

    /**
     * @param keyTeacherProvinceNum the keyTeacherProvinceNum to set
     */
    public void setKeyTeacherProvinceNum(Integer keyTeacherProvinceNum) {
        this.keyTeacherProvinceNum = keyTeacherProvinceNum;
    }

    /**
     * @return the subjectLeaderAreaNum
     */
    public Integer getSubjectLeaderAreaNum() {
        return subjectLeaderAreaNum;
    }

    /**
     * @param subjectLeaderAreaNum the subjectLeaderAreaNum to set
     */
    public void setSubjectLeaderAreaNum(Integer subjectLeaderAreaNum) {
        this.subjectLeaderAreaNum = subjectLeaderAreaNum;
    }

    /**
     * @return the subjectLeaderCityNum
     */
    public Integer getSubjectLeaderCityNum() {
        return subjectLeaderCityNum;
    }

    /**
     * @param subjectLeaderCityNum the subjectLeaderCityNum to set
     */
    public void setSubjectLeaderCityNum(Integer subjectLeaderCityNum) {
        this.subjectLeaderCityNum = subjectLeaderCityNum;
    }

    /**
     * @return the subjectLeaderProvinceNum
     */
    public Integer getSubjectLeaderProvinceNum() {
        return subjectLeaderProvinceNum;
    }

    /**
     * @param subjectLeaderProvinceNum the subjectLeaderProvinceNum to set
     */
    public void setSubjectLeaderProvinceNum(Integer subjectLeaderProvinceNum) {
        this.subjectLeaderProvinceNum = subjectLeaderProvinceNum;
    }

    /**
     * @return the specialTeacherNum
     */
    public Integer getSpecialTeacherNum() {
        return specialTeacherNum;
    }

    /**
     * @param specialTeacherNum the specialTeacherNum to set
     */
    public void setSpecialTeacherNum(Integer specialTeacherNum) {
        this.specialTeacherNum = specialTeacherNum;
    }

    /**
     * @return the otherTeacherNum
     */
    public Integer getOtherTeacherNum() {
        return otherTeacherNum;
    }

    /**
     * @param otherTeacherNum the otherTeacherNum to set
     */
    public void setOtherTeacherNum(Integer otherTeacherNum) {
        this.otherTeacherNum = otherTeacherNum;
    }

    /**
     * @return the principalName
     */
    public String getPrincipalName() {
        return principalName;
    }

    /**
     * @param principalName the principalName to set
     */
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    /**
     * @return the principalGender
     */
    public Integer getPrincipalGender() {
        return principalGender;
    }

    /**
     * @param principalGender the principalGender to set
     */
    public void setPrincipalGender(Integer principalGender) {
        this.principalGender = principalGender;
    }

    /**
     * @return the principalAge
     */
    public Integer getPrincipalAge() {
        return principalAge;
    }

    /**
     * @param principalAge the principalAge to set
     */
    public void setPrincipalAge(Integer principalAge) {
        this.principalAge = principalAge;
    }

    /**
     * @return the principalTitle
     */
    public String getPrincipalTitle() {
        return principalTitle;
    }

    /**
     * @param principalTitle the principalTitle to set
     */
    public void setPrincipalTitle(String principalTitle) {
        this.principalTitle = principalTitle;
    }

    /**
     * @return the principalMobile
     */
    public String getPrincipalMobile() {
        return principalMobile;
    }

    /**
     * @param principalMobile the principalMobile to set
     */
    public void setPrincipalMobile(String principalMobile) {
        this.principalMobile = principalMobile;
    }

    /**
     * @return the principalServiceTime
     */
    public String getPrincipalServiceTime() {
        return principalServiceTime;
    }

    /**
     * @param principalServiceTime the principalServiceTime to set
     */
    public void setPrincipalServiceTime(String principalServiceTime) {
        this.principalServiceTime = principalServiceTime;
    }

    /**
     * @return the principalServiceOthersTime
     */
    public Integer getPrincipalServiceOthersTime() {
        return principalServiceOthersTime;
    }

    /**
     * @param principalServiceOthersTime the principalServiceOthersTime to set
     */
    public void setPrincipalServiceOthersTime(Integer principalServiceOthersTime) {
        this.principalServiceOthersTime = principalServiceOthersTime;
    }

    /**
     * @return the principalHonor
     */
    public String getPrincipalHonor() {
        return principalHonor;
    }

    /**
     * @param principalHonor the principalHonor to set
     */
    public void setPrincipalHonor(String principalHonor) {
        this.principalHonor = principalHonor;
    }

    /**
     * @return the handInHandSchoolNum
     */
    public Integer getHandInHandSchoolNum() {
        return handInHandSchoolNum;
    }

    /**
     * @param handInHandSchoolNum the handInHandSchoolNum to set
     */
    public void setHandInHandSchoolNum(Integer handInHandSchoolNum) {
        this.handInHandSchoolNum = handInHandSchoolNum;
    }

    /**
     * @return the handInHandSchoolVillageNum
     */
    public Integer getHandInHandSchoolVillageNum() {
        return handInHandSchoolVillageNum;
    }

    /**
     * @param handInHandSchoolVillageNum the handInHandSchoolVillageNum to set
     */
    public void setHandInHandSchoolVillageNum(Integer handInHandSchoolVillageNum) {
        this.handInHandSchoolVillageNum = handInHandSchoolVillageNum;
    }

    /**
     * @return the companySchoolNum
     */
    public Integer getCompanySchoolNum() {
        return companySchoolNum;
    }

    /**
     * @param companySchoolNum the companySchoolNum to set
     */
    public void setCompanySchoolNum(Integer companySchoolNum) {
        this.companySchoolNum = companySchoolNum;
    }

    /**
     * @return the companySchoolVillageNum
     */
    public Integer getCompanySchoolVillageNum() {
        return companySchoolVillageNum;
    }

    /**
     * @param companySchoolVillageNum the companySchoolVillageNum to set
     */
    public void setCompanySchoolVillageNum(Integer companySchoolVillageNum) {
        this.companySchoolVillageNum = companySchoolVillageNum;
    }

    /**
     * @return the districtSchoolNum
     */
    public Integer getDistrictSchoolNum() {
        return districtSchoolNum;
    }

    /**
     * @param districtSchoolNum the districtSchoolNum to set
     */
    public void setDistrictSchoolNum(Integer districtSchoolNum) {
        this.districtSchoolNum = districtSchoolNum;
    }

    /**
     * @return the districtSchoolVillageNum
     */
    public Integer getDistrictSchoolVillageNum() {
        return districtSchoolVillageNum;
    }

    /**
     * @param districtSchoolVillageNum the districtSchoolVillageNum to set
     */
    public void setDistrictSchoolVillageNum(Integer districtSchoolVillageNum) {
        this.districtSchoolVillageNum = districtSchoolVillageNum;
    }

    /**
     * @return the otherSchoolNum
     */
    public Integer getOtherSchoolNum() {
        return otherSchoolNum;
    }

    /**
     * @param otherSchoolNum the otherSchoolNum to set
     */
    public void setOtherSchoolNum(Integer otherSchoolNum) {
        this.otherSchoolNum = otherSchoolNum;
    }

    /**
     * @return the otherSchoolName
     */
    public String getOtherSchoolName() {
        return otherSchoolName;
    }

    /**
     * @param otherSchoolName the otherSchoolName to set
     */
    public void setOtherSchoolName(String otherSchoolName) {
        this.otherSchoolName = otherSchoolName;
    }

    /**
     * @return the teacherStudio
     */
    public Integer getTeacherStudio() {
        return teacherStudio;
    }

    /**
     * @param teacherStudio the teacherStudio to set
     */
    public void setTeacherStudio(Integer teacherStudio) {
        this.teacherStudio = teacherStudio;
    }

    /**
     * @return the teacherStudioDetail
     */
    public String getTeacherStudioDetail() {
        return teacherStudioDetail;
    }

    /**
     * @param teacherStudioDetail the teacherStudioDetail to set
     */
    public void setTeacherStudioDetail(String teacherStudioDetail) {
        this.teacherStudioDetail = teacherStudioDetail;
    }

    /**
     * @return the coverSchoolNum
     */
    public Integer getCoverSchoolNum() {
        return coverSchoolNum;
    }

    /**
     * @param coverSchoolNum the coverSchoolNum to set
     */
    public void setCoverSchoolNum(Integer coverSchoolNum) {
        this.coverSchoolNum = coverSchoolNum;
    }

    /**
     * @return the coverSchoolPersonNum
     */
    public Integer getCoverSchoolPersonNum() {
        return coverSchoolPersonNum;
    }

    /**
     * @param coverSchoolPersonNum the coverSchoolPersonNum to set
     */
    public void setCoverSchoolPersonNum(Integer coverSchoolPersonNum) {
        this.coverSchoolPersonNum = coverSchoolPersonNum;
    }

    /**
     * @return the principalStudio
     */
    public Integer getPrincipalStudio() {
        return principalStudio;
    }

    /**
     * @param principalStudio the principalStudio to set
     */
    public void setPrincipalStudio(Integer principalStudio) {
        this.principalStudio = principalStudio;
    }

    /**
     * @return the principalStudioSchoolNum
     */
    public Integer getPrincipalStudioSchoolNum() {
        return principalStudioSchoolNum;
    }

    /**
     * @param principalStudioSchoolNum the principalStudioSchoolNum to set
     */
    public void setPrincipalStudioSchoolNum(Integer principalStudioSchoolNum) {
        this.principalStudioSchoolNum = principalStudioSchoolNum;
    }

    /**
     * @return the principalStudioPersonNum
     */
    public Integer getPrincipalStudioPersonNum() {
        return principalStudioPersonNum;
    }

    /**
     * @param principalStudioPersonNum the principalStudioPersonNum to set
     */
    public void setPrincipalStudioPersonNum(Integer principalStudioPersonNum) {
        this.principalStudioPersonNum = principalStudioPersonNum;
    }

    /**
     * @return the masterStudio
     */
    public Integer getMasterStudio() {
        return masterStudio;
    }

    /**
     * @param masterStudio the masterStudio to set
     */
    public void setMasterStudio(Integer masterStudio) {
        this.masterStudio = masterStudio;
    }

    /**
     * @return the masterStudioSchoolNum
     */
    public Integer getMasterStudioSchoolNum() {
        return masterStudioSchoolNum;
    }

    /**
     * @param masterStudioSchoolNum the masterStudioSchoolNum to set
     */
    public void setMasterStudioSchoolNum(Integer masterStudioSchoolNum) {
        this.masterStudioSchoolNum = masterStudioSchoolNum;
    }

    /**
     * @return the masterStudioPersonNum
     */
    public Integer getMasterStudioPersonNum() {
        return masterStudioPersonNum;
    }

    /**
     * @param masterStudioPersonNum the masterStudioPersonNum to set
     */
    public void setMasterStudioPersonNum(Integer masterStudioPersonNum) {
        this.masterStudioPersonNum = masterStudioPersonNum;
    }

    /**
     * @return the others
     */
    public String getOthers() {
        return others;
    }

    /**
     * @param others the others to set
     */
    public void setOthers(String others) {
        this.others = others;
    }

    /**
     * @return the schoolSuggestion
     */
    public String getSchoolSuggestion() {
        return schoolSuggestion;
    }

    /**
     * @param schoolSuggestion the schoolSuggestion to set
     */
    public void setSchoolSuggestion(String schoolSuggestion) {
        this.schoolSuggestion = schoolSuggestion;
    }

    /**
     * @return the schoolFeature
     */
    public String getSchoolFeature() {
        return schoolFeature;
    }

    /**
     * @param schoolFeature the schoolFeature to set
     */
    public void setSchoolFeature(String schoolFeature) {
        this.schoolFeature = schoolFeature;
    }

    /**
     * @return the schoolAchievements
     */
    public String getSchoolAchievements() {
        return schoolAchievements;
    }

    /**
     * @param schoolAchievements the schoolAchievements to set
     */
    public void setSchoolAchievements(String schoolAchievements) {
        this.schoolAchievements = schoolAchievements;
    }

    /**
     * @return the schoolEducateFeature
     */
    public Integer getSchoolEducateFeature() {
        return schoolEducateFeature;
    }

    /**
     * @param schoolEducateFeature the schoolEducateFeature to set
     */
    public void setSchoolEducateFeature(Integer schoolEducateFeature) {
        this.schoolEducateFeature = schoolEducateFeature;
    }

    /**
     * @return the schoolEducateFeatureDesc
     */
    public String getSchoolEducateFeatureDesc() {
        return schoolEducateFeatureDesc;
    }

    /**
     * @param schoolEducateFeatureDesc the schoolEducateFeatureDesc to set
     */
    public void setSchoolEducateFeatureDesc(String schoolEducateFeatureDesc) {
        this.schoolEducateFeatureDesc = schoolEducateFeatureDesc;
    }

    /**
     * @return the pdfFile
     */
    public String getPdfFile() {
        return pdfFile;
    }

    /**
     * @param pdfFile the pdfFile to set
     */
    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    /**
     * 从CommunityFullInfo中抽取Community信息
     * 
     * @return
     * @since
     */
    public Community getCommunity() {
        Community community = new Community();
        BeanUtils.copyProperties(this, community);
        return community;
    }

    /**
     * 从CommunityFullInfo中抽取CommunityExtInfo信息
     * 
     * @return
     * @since
     */
    public CommunityExtInfo getCommunityExtInfo() {
        CommunityExtInfo extInfo = new CommunityExtInfo();
        BeanUtils.copyProperties(this, extInfo);
        return extInfo;
    }

    /**
     * 合并Community和CommunityExtInfo到CommunityFullInfo
     * 
     * @param community
     * @param extInfo
     * @return
     * @since
     */
    public CommunityFullInfo merge(Community community, CommunityExtInfo extInfo) {
        if (community != null) {
            BeanUtils.copyProperties(community, this);
        }
        if (extInfo != null) {
            BeanUtils.copyProperties(extInfo, this);
        }
        return this;
    }
}
