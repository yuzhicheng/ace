/* =============================================================
 * Created: [2017年5月16日] by tangcc
 * =============================================================
 *
 * Copyright 2014-2015 NetDragon Websoft Inc. All Rights Reserved
 *
 * =============================================================
 */

package com.yzc.vos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author tangcc
 * @date 2017年5月16日 下午2:07:55
 *
 */
@Document(collection = "community_ext_info")
@CompoundIndexes({ @CompoundIndex(name = "communityId_1_index", def = "{communityId:1}") })
public class CommunityExtInfo {

    @Id
    private String extId;

    private Integer communityId;

    /** 建校时间 */
    private String schoolCreateTime;

    /** 基地校类型（1-区县基地校，2-地市基地校）   */
    private Integer baseSchoolType;

    /** 基地证书编号 */
    private String schoolCertNo;

    /** 学校规模：年级个数 */
    private Integer schoolGradeNum;

    /** 学校规模：班级个数 */
    private Integer schoolClassNum;

    /** 学校规模：学生人数 */
    private Integer schoolStudentNum;

    /** 学校规模：教师人数 */
    private Integer schoolTeacherNum;

    /** 青年教师（35岁及以下）人数 */
    private Integer youngTeacherNum;

    /** 骨干教师区县级人数 */
    private Integer keyTeacherAreaNum;

    /** 骨干教师地市级人数 */
    private Integer keyTeacherCityNum;

    /** 骨干教师省级人数 */
    private Integer keyTeacherProvinceNum;

    /** 学科带头人区县级人数 */
    private Integer subjectLeaderAreaNum;

    /** 学科带头人地市级人数 */
    private Integer subjectLeaderCityNum;

    /** 学科带头人省级人数 */
    private Integer subjectLeaderProvinceNum;

    /** 特级教师人数 */
    private Integer specialTeacherNum;

    /** 其他荣誉称号教师人数 */
    private Integer otherTeacherNum;

    /** 校长姓名 */
    private String principalName;

    /** 校长性别（1-男，2-女）  */
    private Integer principalGender;

    /** 校长年龄 */
    private Integer principalAge;

    /** 校长职称 */
    private String principalTitle;

    /** 校长手机号码 */
    private String principalMobile;

    /** 校长任现职时间 */
    private String principalServiceTime;

    /** 在其他学校任校长的年限 */
    private Integer principalServiceOthersTime;

    /** 所获荣誉 */
    private String principalHonor;

    /** 手拉手帮扶学校（单位：所） */
    private Integer handInHandSchoolNum;

    /** 手拉手帮扶学校（农村学校，单位：所） */
    private Integer handInHandSchoolVillageNum;

    /** 集团化办学学校（单位：所） */
    private Integer companySchoolNum;

    /** 集团化办学学校（农村学校，单位：所） */
    private Integer companySchoolVillageNum;

    /** 学区制办学学校（单位：所） */
    private Integer districtSchoolNum;

    /** 学区制办学学校（农村学校，单位：所） */
    private Integer districtSchoolVillageNum;

    /** 其他形式的共同体（单位：所） */
    private Integer otherSchoolNum;

    /** 其他形式的共同体名称 */
    private String otherSchoolName;

    /** 名师工作室（单位：个）  */
    private Integer teacherStudio;

    /** 名师工作室覆盖学科（详细填写）  */
    private String teacherStudioDetail;

    /** 覆盖学校数（单位：所） */
    private Integer coverSchoolNum;

    /** 覆盖学校参与人数（单位：人） */
    private Integer coverSchoolPersonNum;

    /** 名校长工作室（单位：个） */
    private Integer principalStudio;

    /** 名校长工作室带动学校（单位：个） */
    private Integer principalStudioSchoolNum;

    /** 名校长工作室参与人数（单位：人） */
    private Integer principalStudioPersonNum;

    /** 名班主任工作室（单位：个） */
    private Integer masterStudio;

    /** 名班主任工作室带动学校（单位：个） */
    private Integer masterStudioSchoolNum;

    /** 名班主任工作室参与人数（单位：人） */
    private Integer masterStudioPersonNum;

    /** 其他  */
    private String others;

    /** 学校需求意见  */
    private String schoolSuggestion;

    /** 办学特色  */
    private String schoolFeature;

    /** 成果（近五年所获荣誉）  */
    private String schoolAchievements;

    /** 育人特色（多选时值为或运算 1:师生关系,2:教书育人,4:班级建设,8:典型学生问题,16:教师自我发展,32: 其他）   */
    private Integer schoolEducateFeature;

    /** 育人特色其他说明  */
    private String schoolEducateFeatureDesc;

    /** dentryId，存放生成的pdf文件  */
    private String pdfFile;


        /**
     * @return the extId
     */
    public String getExtId() {
        return extId;
    }

    /**
     * @param extId the extId to set
     */
    public void setExtId(String extId) {
        this.extId = extId;
    }

    /**
     * @return the communityId
     */
    public Integer getCommunityId() {
        return communityId;
    }

    /**
     * @param communityId the communityId to set
     */
    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

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
}
