package com.yzc.support;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yzc.config.WebApplicationInitializer;

/**
 * @title 错误信息定义
 * @author yzc
 * @version 1.0
 * @create 2016年8月30日 上午11:19:35
 */
public enum ErrorMessageMapper implements MessageMapper{
    
    CheckUserExist("CheckUserExist",getPropertyValue("userController.check.exist")),
    UserNotFound("UserNotFound",getPropertyValue("userController.check.fail")),
    CreateUserFail("CreateUserFail",getPropertyValue("userController.createUser.operation.fail")),
    DeleteUserSuccess("DeleteUserSuccess",getPropertyValue("userController.deleteUser.operation.success")),
      
    CheckStudentExist("CheckStudentExist",getPropertyValue("studentController.createStudent.check.exist")),
    StudentNotFound("StudentNotFound",getPropertyValue("studentController.updateStudent.check.fail")),
    UpdateStudentSuccess("UpdateStudentSuccess",getPropertyValue("restClientController.updateStudent.successs")),
    DeleteStudentSuccess("DeleteStudentSuccess",getPropertyValue("studentController.deleteStudent.operation.success")),
    BatchDeleteStudentSuccess("BatchDeleteStudentSuccess",getPropertyValue("studentController.batchDeleteStudent.operation.success")),
    BatchAddStudentDuplicate("BatchAddStudentDuplicate",getPropertyValue("studentController.batchAddStudent.operation.fail")),
    
    StatusIsNotExist("StatusIsNotExist",getPropertyValue("paintController.status.isNotExist")),
    DeletePaintSuccess("DeletePaintSuccess",getPropertyValue("paintController.deletePaint.operation.success")),
    PaintNotFound("PaintNotFound",getPropertyValue("paintServiceImpl.checkPaint.paintNotFound")),
    AuthorNotFound("AuthorNotFound",getPropertyValue("paintServiceImpl.checkAuthor.authorNotFound")),
    DeleteAuthorSuccess("DeleteAuthorSuccess",getPropertyValue("paintController.deleteAuthor.operation.success")),
    
    InvalidArgumentsError("InvalidArgumentsError",getPropertyValue("DESUtils.checkParam.fail")),
    CheckParamEmpty("CheckParamEmpty",getPropertyValue("assertUtil.checkParam.fail")),
    RegValidationFail("RegValidationFail",getPropertyValue("regValidator.check.loadProp.fail")),
    LimitParamMissing("LimitParamMissing",getPropertyValue("paramCheckUtil.checkLimit.missParam.fail")),
    LimitParamIllegal("LimitParamIllegal",getPropertyValue("paramCheckUtil.checkLimit.illegalParam.fail")),
    FileIsEmpty("FileNotFound",getPropertyValue("file.is.empty")),
    UploadFileFail("UploadFileFail",getPropertyValue("upload.file.fail")),
    UploadFileSuccess("UploadSuccess",getPropertyValue("upload.file.success")),
    ExportExcelFail("ExportExcelFail",getPropertyValue("export.excel.fail")),
    ExportExcelSuccess("ExportExcelSuccess",getPropertyValue("export.excel.success")),
    ImportExcelFail("ImportExcelSuccess",getPropertyValue("import.excel.fail")),
    ImportExcelSuccess("ImportExcelSuccess",getPropertyValue("import.excel.success")),
    StoreSdkFail("StoreSdkFail",getPropertyValue("store.sdk.fail")),
	
	CoverageTargetTypeNotExist("CoverageTargetTypeNotExist",getPropertyValue("coverageController.targetType.check.fail")),
	CoverageStrategyNotExist("CoverageStrategyNotExist",getPropertyValue("coverageController.strategy.check.fail")),
	BatchCreateCoverageFail("BatchCreateCoverageFail",getPropertyValue("coverageServiceImpl.operation.fail")),
	GetCoverageListByConditionFail("GetCoverageListByConditionFail",getPropertyValue("coverageServiceImpl.getCoverageListByCondition.operation.fail")),
	
	GbCodeNotExist("GbCodeNotExist",getPropertyValue("categoryController.createCategory.gbCodeNotExist")),
	CheckNDCodeExist("CheckNDCodeExist",getPropertyValue("categoryServiceImpl.createCategory.check.ndCode.fail")),
	CheckNdCodeRegularFail("CheckNdCodeRegularFail",getPropertyValue("categoryController.createCategory.regularCheck.ndCode.fail")),
	CategoryNotFound("CategoryNotFound",getPropertyValue("categoryServiceImpl.updateCategory.check.id.fail")),
	CheckCategoryExist("CheckCategoryExist",getPropertyValue("categoryServiceImpl.updateCategory.check.category.fail")),
	CheckNdCodeFail("CheckNdCodeFail",getPropertyValue("categoryController.check.ndCode.fail")),
	CheckCategoryHasData("CheckCategoryHasData",getPropertyValue("categoryServiceImpl.deleteCategory.checkExistData.fail")),
	DeleteCategorySuccess("DeleteCategorySuccess",getPropertyValue("categoryController.deleteCategory.operation.success")),
	
	CategoryDataExtendLimit("CategoryDataExtendLimit",getPropertyValue("categoryServiceImpl.createCategoryData.extendLimit")),
	CategoryDataNotFound("CategoryDataNotFound",getPropertyValue("categoryServiceImpl.createCategoryData.check.id.fial")),
	CheckTitleDuplicate("CheckTitleDuplicate",getPropertyValue("categoryServiceImpl.createCategoryData.check.title.fail")),
	CheckShortNameDuplicate("CheckShortNameDuplicate",getPropertyValue("categoryServiceImpl.createCategoryData.check.shortName.fail")),
	CategoryDataHasChildNode("CategoryDataHasChildNode",getPropertyValue("categoryServiceImpl.deleteCategoryData.check.existChildNode.fail")),
	CategoryRelationHasCategoryData("CategoryRelationHasCategoryData",getPropertyValue("categoryServiceImpl.deleteCategoryData.check.existInCategoryRelation.fail")),
	DeleteCategoryDataSuccess("DeleteCategoryDataSuccess",getPropertyValue("categoryController.deleteCategoryData.operation.success")),
	
	CheckIdentifierFail("CheckIdentifierFail",getPropertyValue("CommonHelper.resourceParamValid.check.identifier.fail")),
	CheckKeywordsFail("CheckKeywordsFail",getPropertyValue("CommonHelper.resourceParamValid.check.keywords.fail")),
	CheckTagsFail("CheckTagsFail",getPropertyValue("CommonHelper.resourceParamValid.check.tags.fail")),
	CheckTechInfoFail("CheckTechInfoFail",getPropertyValue("CommonHelper.resourceParamValid.check.techInfo.fail")),
	CheckTaxonpathFail("CheckTaxonpathFail",getPropertyValue("CommonHelper.resourceParamValid.check.taxonpath.fail")),
	CheckCoverageFail("CheckCoverageFail",getPropertyValue("CommonHelper.resourceParamValid.check.coverage.fail")),
	CheckCopyRightFail("CheckCopyRightFail",getPropertyValue("CommonHelper.resourceParamValid.check.copyright.fail")),
	CheckGlobalFieldFail("CheckGlobalFieldFail",getPropertyValue("CommonHelper.resourceParamValid.check.globalField.fail")),
    CheckDuplicateIdFail("CheckDuplicateIdFail",getPropertyValue("ResourceServiceImpl.create.isDuplicateId.fail")),
    CheckDuplicateCodeFail("CheckDuplicateCodeFail",getPropertyValue("ResourceServiceImpl.create.isDuplicateCode.fail")),
	CommonSearchParamError("CommonSearchParamError",""),
	IncludesParamError("IncludesParamError",""),
	NotSupportResourceType("NotSupportResourceType",getPropertyValue("commonServiceHelper.getRepository.fail")),
	CSNotSupportResourceType("CSNotSupportResourceType",getPropertyValue("commonServiceHelper.uploadOrdowlaod.fail"))
	;
    
	private static final Logger log = LoggerFactory.getLogger(ErrorMessageMapper.class);
	
    private String code;
    
    private String message;
    
    ErrorMessageMapper(String code,String message){
        
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
	private static String getPropertyValue(String key){		
		try {
			return new String(WebApplicationInitializer.message_properties.getProperty(key).getBytes("ISO8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {

			log.warn("加载exception_message.properties出错！", e);

		} catch(Exception e){

			log.warn("加载exception_message.properties中(" + key + ")出错！", e);
		}
		return "";
	}
    
}
