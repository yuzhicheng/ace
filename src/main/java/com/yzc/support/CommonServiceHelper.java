package com.yzc.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.yzc.entity.BaseEntity;
import com.yzc.entity.Category;
import com.yzc.entity.CategoryData;
import com.yzc.entity.resource.Asset;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.models.ResourceModel;
import com.yzc.models.resource.AssetModel;
import com.yzc.repository.CategoryRepository;
import com.yzc.repository.ResourceRepository;
import com.yzc.repository.resource.AssetRepository;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.StringUtils;
import com.yzc.vos.ResourceViewModel;
import com.yzc.vos.resource.AssetViewModel;

public class CommonServiceHelper {

	private static final Logger LOG = LoggerFactory.getLogger(CommonServiceHelper.class);
	
    //素材
    @Autowired
    AssetRepository assetRepository;
    
	@Autowired
	CategoryRepository categoryRepository;

	Map<String, RepositoryAndModelAndView> repositoryAndModelMap;
	
    @PostConstruct
    public void postConstruct() {
        repositoryAndModelMap = new HashMap<String, RepositoryAndModelAndView>();
//        //素材
//        repositoryAndModelMap.put("assets", new RepositoryAndModelAndView(assetRepository, AssetModel.class, AssetViewModel.class,Asset.class,true,true));
//        //教案
//        repositoryAndModelMap.put("lessonplans", new RepositoryAndModelAndView(lessonPlansRepository, LessonPlanModel.class, LessonPlanViewModel.class,LessonPlan.class,true,true));
//        // 学案
//        repositoryAndModelMap.put("learningplans", new RepositoryAndModelAndView(learningPlansRepository,LearningPlanModel.class, LearningPlanViewModel.class,LearningPlan.class,true,true));
//        //作业
//        repositoryAndModelMap.put("homeworks", new RepositoryAndModelAndView(homeWorkRepository, HomeworkModel.class,HomeworkViewModel.class,HomeWork.class,true,true));
//        
//        //教材
//        repositoryAndModelMap.put("teachingmaterials", new RepositoryAndModelAndView(teachingMaterialRepository,TeachingMaterialModel.class,TeachingMaterialViewModel.class,TeachingMaterial.class,true,true));
//																			 
//		//习题
//        repositoryAndModelMap.put("questions", new RepositoryAndModelAndView(questionRepository,QuestionModel.class,QuestionViewModel.class,Question.class,true,true));
//        // 课件颗粒
//        repositoryAndModelMap.put("coursewareobjects", new RepositoryAndModelAndView(coursewareObjectRepository,CourseWareObjectModel.class,CourseWareObjectViewModel.class,CoursewareObject.class,true,true));
//        
//        // 学科工具
//        repositoryAndModelMap.put("tools", new RepositoryAndModelAndView(toolsRepository,CourseWareObjectModel.class,CourseWareObjectViewModel.class,CoursewareObject.class,true,true));
//        
//        // 试卷
//        repositoryAndModelMap.put("examinationpapers", new RepositoryAndModelAndView(examinationPaperRepository,ExaminationPaperModel.class,ExaminationPaperViewModel.class,ExaminationPaper.class,true,true));
//        
//        // 习题集
//        repositoryAndModelMap.put("exercisesset", new RepositoryAndModelAndView(examinationPaperRepository,ExaminationPaperModel.class,ExaminationPaperViewModel.class,ExaminationPaper.class, true,true));
//           
//        // 课时
//        repositoryAndModelMap.put("lessons", new RepositoryAndModelAndView(lessonRepository,LessonModel.class,LessonViewModel.class,Lesson.class,false,false));
//        
//        // 知识点
//        repositoryAndModelMap.put("knowledges", new RepositoryAndModelAndView(chapterRepository,KnowledgeModel.class,KnowledgeViewModel4Out.class,Chapter.class,false,false));
//        
//        // 教学目标
//        repositoryAndModelMap.put("instructionalobjectives", new RepositoryAndModelAndView(instructionalObjectiveRepository, InstructionalObjectiveModel.class,InstructionalObjectiveViewModel.class,InstructionalObjective.class,false,false));
//
//        // 子教学目标
//        repositoryAndModelMap.put("subInstruction", new RepositoryAndModelAndView(subInstructionRepository,SubInstructionModel.class,SubInstructionViewModel.class,SubInstruction.class,false,false));
//        
//        //课件
//        repositoryAndModelMap.put("coursewares", new RepositoryAndModelAndView(coursewareRepository, CoursewareModel.class,CoursewareViewModel.class,Courseware.class,true,true));
//        // 电子教材
//        repositoryAndModelMap.put("ebooks", new RepositoryAndModelAndView(ebookRepository,EbookModel.class,EbookViewModel.class,Ebook.class, true,true));
//        
//        //课件颗粒模板
//        repositoryAndModelMap.put("coursewareobjecttemplates", new RepositoryAndModelAndView(coursewareObjectTemplateRepository, CourseWareObjectTemplateModel.class, CoursewareObjectTemplateViewModel.class, CoursewareObjectTemplate.class, true,true));
//        
//        //教辅
//        repositoryAndModelMap.put("guidancebooks", new RepositoryAndModelAndView(teachingMaterialRepository, TeachingMaterialModel.class,TeachingMaterialViewModel.class,  TeachingMaterial.class,false,false));
//        
//        //元课程
//        repositoryAndModelMap.put("metacurriculums", new RepositoryAndModelAndView(teachingMaterialRepository, TeachingMaterialModel.class,TeachingMaterialViewModel.class, TeachingMaterial.class,true,true));
//        
//        //教学活动
//        repositoryAndModelMap.put("teachingactivities", new RepositoryAndModelAndView(teachingActivitiesRepository,CoursewareModel.class,CoursewareViewModel.class,TeachingActivities.class,true,true));
//        
//        //eduresource
//        repositoryAndModelMap.put(Constant.RESTYPE_EDURESOURCE, new RepositoryAndModelAndView(null,  null,ResourceViewModel.class, null, false,false));
//        
//        //knowledgebase
//        repositoryAndModelMap.put("knowledgebases", new RepositoryAndModelAndView(knowledgeBaseRepository,KnowledgeBaseModel.class,KnowledgeBaseViewModel.class,KnowledgeBase.class,false,false));
//        //默认行为
//        repositoryAndModelMap.put("behaviors", new RepositoryAndModelAndView(behaviorRepository, AssetModel.class, AssetViewModel.class,Behavior.class,true,true));
//        
        //paint(先用素材的，目的是能返回session)
        repositoryAndModelMap.put("assets", new RepositoryAndModelAndView(assetRepository, AssetModel.class, AssetViewModel.class,Asset.class,true,true));
    }

	/**
	 * 辅助类，用于绑定LC model 与SDK repository View
	 * 
	 * @author linsm
	 * @since
	 */
	private static class RepositoryAndModelAndView {

		ResourceRepository<? extends BaseEntity> repository;
		Class<? extends ResourceModel> modelClass;
		Class<? extends ResourceViewModel> viewClass;
		Class<? extends BaseEntity> beanClass;
		boolean downloadable;
		boolean uploadable;

		public ResourceRepository<? extends BaseEntity> getRepository() {
			return repository;
		}

		public Class<? extends ResourceModel> getModel() {
			return modelClass;
		}

		public boolean isDownloadable() {
			return downloadable;
		}

		public boolean isUploadable() {
			return uploadable;
		}

		public Class<? extends ResourceViewModel> getViewClass() {
			return viewClass;
		}

		public Class<? extends BaseEntity> getBeanClass() {
			return beanClass;
		}

		/**
		 * 
		 */
		public RepositoryAndModelAndView(ResourceRepository<? extends BaseEntity> repository,
				Class<? extends ResourceModel> modelClass, Class<? extends ResourceViewModel> viewClass,
				Class<? extends BaseEntity> beanClass, boolean downloadable, boolean uploadable) {
			this.repository = repository;
			this.modelClass = modelClass;
			this.viewClass = viewClass;
			this.beanClass = beanClass;
			this.downloadable = downloadable;
			this.uploadable = uploadable;
		}
	}

	public ResourceRepository<? extends BaseEntity> getRepository(String resourceType) {

		RepositoryAndModelAndView repositoryAndModel = repositoryAndModelMap.get(resourceType);
		if (repositoryAndModel != null) {
			return repositoryAndModel.getRepository();
		} else {
			LOG.error(ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.NotSupportResourceType.getCode(),
					ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
		}
	}

	public Class<? extends ResourceModel> getModel(String resourceType) {
		RepositoryAndModelAndView repositoryAndModel = repositoryAndModelMap.get(resourceType);
		if (repositoryAndModel != null) {
			return repositoryAndModel.getModel();
		} else {

			LOG.error(ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);

			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.NotSupportResourceType.getCode(),
					ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
		}
	}

	public Class<? extends ResourceViewModel> getViewClass(String resourceType) {
		RepositoryAndModelAndView repositoryAndModel = repositoryAndModelMap.get(resourceType);
		if (repositoryAndModel != null) {
			return repositoryAndModel.getViewClass();
		} else {

			LOG.error(ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);

			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.NotSupportResourceType.getCode(),
					ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
		}
	}

	public Class<? extends BaseEntity> getBeanClass(String resourceType) {
		RepositoryAndModelAndView repositoryAndModel = repositoryAndModelMap.get(resourceType);
		if (repositoryAndModel != null) {
			return repositoryAndModel.getBeanClass();
		} else {

			LOG.error(ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);

			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.NotSupportResourceType.getCode(),
					ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
		}
	}

	public void assertUploadable(String resourceType) {
		RepositoryAndModelAndView repositoryAndModel = repositoryAndModelMap.get(resourceType);
		if (repositoryAndModel != null) {
			if (!repositoryAndModel.isUploadable()) {
				LOG.error(ErrorMessageMapper.CSNotSupportResourceType.getMessage() + resourceType);
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
						ErrorMessageMapper.CSNotSupportResourceType.getCode(),
						ErrorMessageMapper.CSNotSupportResourceType.getMessage() + resourceType);
			}

		} else {
			LOG.error(ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.NotSupportResourceType.getCode(),
					ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
		}

	}

	public void assertDownloadable(String resourceType) {
		RepositoryAndModelAndView repositoryAndModel = repositoryAndModelMap.get(resourceType);
		if (repositoryAndModel != null) {
			if (!repositoryAndModel.isDownloadable()) {
				LOG.error(ErrorMessageMapper.CSNotSupportResourceType.getMessage() + resourceType);
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
						ErrorMessageMapper.CSNotSupportResourceType.getCode(),
						ErrorMessageMapper.CSNotSupportResourceType.getMessage() + resourceType);
			}

		} else {
			LOG.error(ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.NotSupportResourceType.getCode(),
					ErrorMessageMapper.NotSupportResourceType.getMessage() + resourceType);
		}

	}
	
    /**
     * 根据资源类型判断是否有上传接口
     * 
     * @param resourceType
     * @return
     */
    public boolean isUploadable(String resourceType){
    	RepositoryAndModelAndView repositoryAndModel = repositoryAndModelMap.get(resourceType);
    	if(repositoryAndModel != null){
    		return repositoryAndModel.isUploadable();
    	}
    	return false;
    }

	/**
	 * 获取维度的shortName
	 * 
	 * @author:xuzy
	 * @date:2015年8月4日
	 * @param beanListResult
	 * @return
	 */
	public Map<String,String> getCategoryByData(List<CategoryData> beanListResult){
		Map<String,String> returnMap = new HashMap<String, String>();
		List<String> cList = new ArrayList<String>();
		List<Category> categoryList = new ArrayList<Category>();
		if(CollectionUtils.isNotEmpty(beanListResult)){
			for (CategoryData categoryData : beanListResult) {
				//维度
				String s = categoryData.getNdCode().substring(0, 2);
				if(!cList.contains(s)){
					cList.add(s);
				}
			}
			try {
				categoryList = categoryRepository.getListWhereInCondition("ndCode", cList);
				for (Category category : categoryList) {
					String ndCode = category.getNdCode();
					String shortName = category.getShortName();
					if(StringUtils.isNotEmpty(ndCode)){
						returnMap.put(ndCode, shortName);
					}
				}
			} catch (EspStoreException e) {
			    
			    LOG.error(ErrorMessageMapper.StoreSdkFail.getMessage(),e);
			    
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getLocalizedMessage());
			}
		}
		return returnMap;
	}

}
