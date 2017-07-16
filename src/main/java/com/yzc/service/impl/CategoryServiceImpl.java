package com.yzc.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;
import com.yzc.entity.Category;
import com.yzc.entity.CategoryData;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.models.CategoryDataModel;
import com.yzc.models.CategoryModel;
import com.yzc.repository.CategoryDataRepository;
import com.yzc.repository.CategoryRepository;
import com.yzc.repository.index.AdaptQueryRequest;
import com.yzc.repository.index.QueryRequest;
import com.yzc.repository.index.QueryResponse;
import com.yzc.service.CategoryService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.enums.NdCodePattern;
import com.yzc.support.enums.RecordStatus;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.utils.StringUtils;
import com.yzc.vos.ListViewModel;


@Service("CategoryServiceImpl")
@Transactional
public class CategoryServiceImpl implements CategoryService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryDataRepository categoryDataRepository;
	


	@Override
	public CategoryModel creatCategory(CategoryModel categoryModel) {
		CategoryModel result = null;
		Category testModel = new Category();
		testModel.setNdCode(categoryModel.getNdCode());
		try {
			testModel = categoryRepository.getByExample(testModel);
		} catch (EspStoreException e) {
			LOG.error("校验ndCode出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (testModel != null) {
			LOG.error("检测ndCode已存在");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckNDCodeExist);
		}
		// 入参转换
		Category bean = BeanMapperUtils.beanMapper(categoryModel,Category.class);
		bean.setCreateTime(new Timestamp(System.currentTimeMillis()));
		bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		bean.setState(RecordStatus.NORMAL);
		Category beanResult = new Category();		
		try {
			beanResult = categoryRepository.add(bean);
		} catch (EspStoreException e) {
			LOG.error("创建category出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (beanResult == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail);
		}			
			result = BeanMapperUtils.beanMapper(beanResult, CategoryModel.class);
		return result;
	}

	@Override
	public CategoryModel updateCategory(CategoryModel categoryModel) {
		
		Category category = null;
		try {
			category=categoryRepository.get(categoryModel.getIdentifier());
		} catch (EspStoreException e) {
			LOG.error("校验分类维度id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (category == null) {
			LOG.error(ErrorMessageMapper.CategoryNotFound.getMessage());
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CategoryNotFound);
		}
		// 判断ndCode是否重复,如果是修改成原本的ndCode是允许的
		Category s4ndCode = new Category();
		s4ndCode.setNdCode(categoryModel.getNdCode());
		try {
			s4ndCode = categoryRepository.getByExample(s4ndCode);
		} catch (EspStoreException e) {
			LOG.error("校验是否重复时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (s4ndCode != null && !s4ndCode.getIdentifier().equals(category.getIdentifier())) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckCategoryExist);
		}
		// 入参
		Category bean = BeanMapperUtils.beanMapper(categoryModel,Category.class);
		bean.setCreateTime(category.getCreateTime());
		bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		bean.setState(category.getState());
		// 调用sdk
		Category beanResult=null;
		try {
			beanResult = categoryRepository.update(bean);
		} catch (EspStoreException e) {
			LOG.debug("调用sdk方法:update");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (beanResult == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail);
		}
		return BeanMapperUtils.beanMapper(beanResult, CategoryModel.class);
	}

	@Override
	public ListViewModel<CategoryModel> readCategory(String words, String limit) {
		
		ListViewModel<CategoryModel> result = new ListViewModel<CategoryModel>();
		// requestParam
		QueryRequest queryRequest = new QueryRequest();
		Integer limitResult[] = ParamCheckUtil.checkLimit(limit);// 这里其实只需要分解数据
		queryRequest.setKeyword(words);
		queryRequest.setLimit(limitResult[1]);
		queryRequest.setOffset(limitResult[0]);
		
		try {
			// 调用sdk
			LOG.debug("调用sdk方法:search");
			QueryResponse<Category> response=categoryRepository.search(queryRequest);		
			// 处理返回数据
			long total = 0L;
			List<CategoryModel> items = new ArrayList<CategoryModel>();
			if (response != null && response.getHits() != null) {
				items = ObjectUtils.fromJson(ObjectUtils.toJson(response.getHits().getDocs()),new TypeToken<List<CategoryModel>>() {});
				total = response.getHits().getTotal();
			}
			result.setTotal(total);
			result.setItems(items);
			result.setLimit(limit);
		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
	
		return result;
	}

	@Override
	public void deleteCategory(String cid) {
		
		Category c4Detail = null;
		try {
			c4Detail = categoryRepository.get(cid);
		} catch (EspStoreException e) {
			LOG.error("校验分类维度id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (c4Detail == null) {
			LOG.error(ErrorMessageMapper.CategoryNotFound.getMessage());
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CategoryNotFound);
		}
		if (hasCategoryData(c4Detail.getNdCode())) {
			LOG.error(ErrorMessageMapper.CheckCategoryHasData.getMessage());
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckCategoryHasData);
		}		
		try {
			categoryRepository.del(cid);
			LOG.debug("删除分类维度资源:{}", cid);
		} catch (EspStoreException e) {
			LOG.error("删除分类维度出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		
		
	}
	
	/**
	 * 分类维度是否存在维度数据(通过ndCode)
	 * 
	 * @param ndCode
	 * @return
	 * @author linsm
	 */
	private boolean hasCategoryData(String ndCode) {
		// 只需要判断是否至少存在一个数据，若有时，则parent = "ROOT"时也必有;
		Long total = 0L;
		total = readCategoryData(ndCode, true, "ROOT", "", "(0,1)", 2).getTotal();
		return total > 0;
	}

	@Override
	public CategoryDataModel createCategoryData(CategoryDataModel categoryDataModel) {

		//校验NdCode是否唯一
		CategoryData testModel = new CategoryData();
		testModel.setNdCode(categoryDataModel.getNdCode());
		try {
			testModel = categoryDataRepository.getByExample(testModel);
		} catch (EspStoreException e) {
			LOG.error("校验ndCode在CategoryData中是否唯一出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (testModel != null) {
			LOG.error(ErrorMessageMapper.CheckNDCodeExist.getMessage());
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckNDCodeExist);
		}
		// 检测parent, category对应的资源是否存在
		// parent
		CategoryDataModel parent = assertCategoryDataExistById(categoryDataModel.getParent().getIdentifier());
		// category
		CategoryModel category = assertCategoryExistById(categoryDataModel.getCategory().getIdentifier());
		NdCodePattern.checkNdCodeRelation(category.getNdCode(),parent.getNdCode(), categoryDataModel.getNdCode());
		// 增加维度数据：shortName, title 局部惟一性校验
		checkLocalUniqueTitleAndShortName(categoryDataModel, true, true);
		// 处理dimension_path
		categoryDataModel.setDimensionPath(parent.getDimensionPath() + "/"+ categoryDataModel.getNdCode());
		// 入参
		CategoryData bean = changeCategoryDataModel2Bean(categoryDataModel);
		bean.setCreateTime(new Timestamp(System.currentTimeMillis()));
		bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		bean.setState(RecordStatus.NORMAL);
		// 调用sdk
		LOG.debug("调用sdk方法:add");
		LOG.debug("创建维度数据资源:{}", bean.getIdentifier());
		CategoryData resultBean=null;
		try {
			resultBean=categoryDataRepository.add(bean);
		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (resultBean == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail);
		}
		return changeBean2CategoryDataModel(resultBean);
	}
	
	@Override
	public CategoryDataModel updateCategoryData(CategoryDataModel categoryDataModel) {
		// 通过uuid, 获取数据
		CategoryData cd = null;
		try {
			cd = categoryDataRepository.get(categoryDataModel.getIdentifier());
		} catch (EspStoreException e) {
			LOG.error("根据id查询维度数据出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (cd == null) {
			LOG.error(ErrorMessageMapper.CategoryDataNotFound.getMessage());
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CategoryDataNotFound);
		}

		String categoryDataNdCode = categoryDataModel.getNdCode();
		if (!cd.getNdCode().equals(categoryDataNdCode)) {
			// 发生改变的属性：判断“不重复”
			CategoryData categoryData = new CategoryData();
			categoryData.setNdCode(categoryDataNdCode);
			try {
				categoryData = categoryDataRepository.getByExample(categoryData);
			} catch (EspStoreException e) {
				LOG.error("校验ndCode在CategoryData中是否唯一出错");
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
			}
		
			if (categoryData != null&& !categoryData.getIdentifier().equals(cd)) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckNDCodeExist);
			}

			// 若存在子结点，则不允许修改维度数据编码
			if (hasChildNode(cd.getIdentifier())) {
				LOG.error("存在子结点，不允许修改nd_code");
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
						ErrorMessageMapper.CategoryDataHasChildNode.getCode(), "存在子结点，不允许修改nd_code");
			}
		}
		// 通过uuid 对应的资源是否存在：parent, category
		CategoryDataModel parent = assertCategoryDataExistById(categoryDataModel.getParent().getIdentifier());
		// category
		CategoryModel category = assertCategoryExistById(categoryDataModel.getCategory().getIdentifier());

		// 添加了对维度ndCode的校验
		NdCodePattern.checkNdCodeRelation(category.getNdCode(),parent.getNdCode(), categoryDataModel.getNdCode());
		// 暂时不用处理gbCode
		// 增加维度数据：shortName, title 局部惟一性校验
		boolean isTitleNeed = false; // 默认无需校验惟一性
		boolean isShortNameNeed = false;
		if (!categoryDataModel.getTitle().equals(cd.getTitle())) {
			isTitleNeed = true;
		}
		if (!categoryDataModel.getShortName().equals(cd.getShortName())) {
			isShortNameNeed = true;
		}
		checkLocalUniqueTitleAndShortName(categoryDataModel, isTitleNeed,isShortNameNeed);

		// 有可能会发生了改变，当改变父结点，需要重新生成
		categoryDataModel.setDimensionPath(parent.getDimensionPath() + "/"+ categoryDataModel.getNdCode());

		// 入参
		CategoryData bean = changeCategoryDataModel2Bean(categoryDataModel);

		// 不可改变的值
		if (StringUtils.isEmpty(bean.getPreview())) {
			bean.setPreview(cd.getPreview());
		}
		bean.setCreateTime(cd.getCreateTime());
		bean.setState(cd.getState());
	    bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		// 调用sdk
		CategoryData beanResult=null;
		try {
			beanResult = categoryDataRepository.update(bean);
		} catch (EspStoreException e) {
			LOG.debug("调用sdk方法:update");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (beanResult == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail);
		}

		return changeBean2CategoryDataModel(beanResult);
	}
	
	@Override
	public ListViewModel<CategoryDataModel> readCategoryData(String ndCode, boolean all, String parentId, String words,String limit, Integer isDefault) {
		ListViewModel<CategoryDataModel> result = new ListViewModel<CategoryDataModel>();
		// 根据 ndCode 找到 categoryId;
		Category testModel = new Category();
		testModel.setNdCode(ndCode);
		Category category = null;
		try {
			category = categoryRepository.getByExample(testModel);
		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail);
		}
		if (category == null) {
			LOG.error(ErrorMessageMapper.CategoryNotFound.getMessage() + ndCode);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CategoryNotFound);
		}
		String categoryId = category.getIdentifier();
		// requestParam
		AdaptQueryRequest<CategoryData> queryRequest = new AdaptQueryRequest<>();
		CategoryData bean = new CategoryData();
		bean.setCategory(categoryId);
		bean.setParent(parentId);
		if (null == isDefault) {
			bean.setIsDefault(1);
		}
		if (isDefault != 2) {
			bean.setIsDefault(isDefault);
		}
		queryRequest.setParam(bean);
		if (!StringUtils.isEmpty(parentId)) {
			// 只返回一层数据，此时，words 和limit 起作用
			Integer limitResult[] = ParamCheckUtil.checkLimit(limit);// 这里其实只需要分解数据
			queryRequest.setKeyword(words);
			queryRequest.setLimit(limitResult[1]);
			queryRequest.setOffset(limitResult[0]);
		} else {
			// 通过多一次的查询获得总量;
			queryRequest.setKeyword("");
			queryRequest.setLimit(1);
			queryRequest.setOffset(0);
			// 调用sdk
			LOG.debug("调用sdk方法:searchByExample");
			QueryResponse<CategoryData> firstResponse = null;
			try {
				firstResponse = categoryDataRepository.searchByExample(queryRequest);
			} catch (EspStoreException e) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail);
			}
			if (firstResponse != null && firstResponse.getHits() != null) {
				int total = (int) firstResponse.getHits().getTotal();
				LOG.info("共有维度数据：" + total);
				// 没有数据，不用再查询
				if (total <= 0) {
					result.setTotal(0L);
					// limit 不起作用时，构造成(0,total)的形式
					result.setLimit("(0,0)");
					return result;
				}
				queryRequest.setLimit(total); // 设置总数
			}
		}
		// 调用sdk
		LOG.debug("调用sdk方法:searchByExample");
		QueryResponse<CategoryData> response=null;
		try {
			response = categoryDataRepository.searchByExample(queryRequest);
		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail);
		}
		// 处理返回数据
		result.setTotal(0L);
		if (response != null && response.getHits() != null) {
			List<CategoryDataModel> items = new ArrayList<>();
			List<CategoryData> categoryDatas = response.getHits().getDocs();
			if (categoryDatas != null) {
				for (CategoryData categoryData : categoryDatas) {
					CategoryDataModel categoryDataModel = changeBean2CategoryDataModel(categoryData);
					items.add(categoryDataModel);
				}
			}
			result.setItems(items);
			result.setTotal(response.getHits().getTotal());
		}
		if (!StringUtils.isEmpty(parentId)) {
			// 只返回一层数据，此时，words 和limit 起作用
			result.setLimit(limit);
		} else {
			// limit 不起作用时，构造成(0,total)的形式
			result.setLimit("(0," + result.getTotal() + ")");
		}

		return result;
	}
	
	@Override
	public void deleteCategoryData(String did) {
		// 先断定是否存在
		assertCategoryDataExistByIdWithoutRoot(did);// 没有可能是ROOT， 是资源就存在uuid
		// 判断是否存在子结点，是某个维度数据的父结点
		if (hasChildNode(did)) {
			LOG.error(ErrorMessageMapper.CategoryDataHasChildNode.getMessage() + did);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CategoryDataHasChildNode);
		}

		try {
			categoryDataRepository.del(did);
		} catch (EspStoreException e) {
			LOG.error("删除分类维度数据出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		LOG.debug("删除维度数据资源:{}", did);	
	}
	
	
	private CategoryDataModel changeBean2CategoryDataModel(CategoryData bean) {
		if (bean == null) {
			return null;
		}
		CategoryDataModel result = BeanMapperUtils.beanMapper(bean,CategoryDataModel.class);
		CategoryModel category = new CategoryModel();
		category.setIdentifier(bean.getCategory());
		result.setCategory(category);
		CategoryDataModel categoryData = new CategoryDataModel();
		categoryData.setIdentifier(bean.getParent());
		result.setParent(categoryData);
		return result;
	}
	
	/**
	 * 把维度数据转换成sdk类型
	 * 
	 * @param categoryDataModel
	 * @return
	 * @author linsm
	 */
	private CategoryData changeCategoryDataModel2Bean(CategoryDataModel categoryDataModel) {
		if (categoryDataModel == null) {
			return null;
		}
		CategoryData bean = BeanMapperUtils.beanMapper(categoryDataModel,
				CategoryData.class);
		bean.setCategory(categoryDataModel.getCategory().getIdentifier());
		bean.setParent(categoryDataModel.getParent().getIdentifier());
		return bean;
	}
	
	/**
	 * 通过uuid（包含ROOT） 确认维度数据存在 修改，保证生成CategoryDataModel,当did ==
	 * ROOT时，手动生成CategoryData（uuid== ROOT, dimension_path=ROOT, ndCode=ROOT） <br>
	 * Created 2015年5月4日 上午11:45:49
	 * 
	 * @param did
	 * @return
	 * @author linsm
	 */
	private CategoryDataModel assertCategoryDataExistById(String did) {
		CategoryDataModel testModel = null;
		if (!did.equals(NdCodePattern.CATEGORYDATA_TOP_NODE_PARENT)) {
			// 非顶级节点
			testModel = assertCategoryDataExistByIdWithoutRoot(did);
		} else {
			testModel = new CategoryDataModel();
			testModel.setIdentifier(NdCodePattern.CATEGORYDATA_TOP_NODE_PARENT);
			testModel.setNdCode(NdCodePattern.CATEGORYDATA_TOP_NODE_PARENT);
			testModel.setDimensionPath(NdCodePattern.CATEGORYDATA_TOP_NODE_PARENT);
		}
		return testModel;
	}
	
	/**
	 * 确认维度数据存在(排除root)
	 * 
	 * <br>
	 * Created 2015年5月4日 上午11:39:06
	 * 
	 * @param id
	 * @return
	 * @author linsm
	 */
	private CategoryDataModel assertCategoryDataExistByIdWithoutRoot(String id) {
		
		CategoryData testModel = null;
		try {
			testModel = categoryDataRepository.get(id);
		} catch (EspStoreException e) {
			LOG.error("根据id查询维度数据出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (testModel == null) {
			LOG.error(ErrorMessageMapper.CategoryDataNotFound.getMessage());
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CategoryDataNotFound);
		}
		
		return changeBean2CategoryDataModel(testModel);
	}
	
	/**
	 * 根据id查询分类维度资源是否存在
	 * 	
	 * @param id
	 * @author linsm
	 */
	private CategoryModel assertCategoryExistById(String id) {
		Category category= null;
		try {
			category = categoryRepository.get(id);
		} catch (EspStoreException e) {
			LOG.error("检测分类维度是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		//不存在抛出异常
		if (category == null) {
			LOG.error(ErrorMessageMapper.CategoryNotFound.getMessage());
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CategoryNotFound);
		}
		return BeanMapperUtils.beanMapper(category, CategoryModel.class);
	}
	
	/**
	 * 校验维度数据title shortName 局部惟一性 (非惟一性，抛出异常)
	 * 
	 * @param categoryDataModel
	 * @param isTitleNeed
	 * @param isShortNameNeed
	 * @since
	 */
	private void checkLocalUniqueTitleAndShortName(CategoryDataModel categoryDataModel, boolean isTitleNeed,boolean isShortNameNeed) {
		// 都不需要校验
		if (!isTitleNeed && !isShortNameNeed) {
			return;
		}
		if (categoryDataModel == null|| categoryDataModel.getCategory() == null|| categoryDataModel.getParent() == null) {
			// 不需要校验
			return;
		}
		CategoryData condition = new CategoryData();
		condition.setCategory(categoryDataModel.getCategory().getIdentifier());
		condition.setParent(categoryDataModel.getParent().getIdentifier());
		List<CategoryData> datas = null;
		try {
			LOG.debug("调用sdk方法:getAllByExample");
			datas = categoryDataRepository.getAllByExample(condition);
		} catch (EspStoreException e) {
			LOG.error(ErrorMessageMapper.StoreSdkFail.getMessage(), e);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (CollectionUtils.isEmpty(datas)) {
			LOG.debug("还不存在维度数据");
			return;
		}
		// 重名抛出异常
		for (CategoryData data : datas) {
			if (data != null) {
				if (isTitleNeed&& categoryDataModel.getTitle().equals(data.getTitle())&& categoryDataModel.getIsDefault().equals(data.getIsDefault())) {
					LOG.error(ErrorMessageMapper.CheckTitleDuplicate.getMessage());
					throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckTitleDuplicate);
				}
				if (isShortNameNeed&& categoryDataModel.getShortName().equals(data.getShortName())) {
					LOG.error(ErrorMessageMapper.CheckShortNameDuplicate.getMessage());
					throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckShortNameDuplicate);
				}
			}
		}
	}
	
	/**
	 * 该维度数据是否存在子结点(如小学存在一、二、三、四年级等子结点)
	 * 
	 * @param did
	 * @return
	 * @author linsm
	 */
	private boolean hasChildNode(String did) {
		
		QueryResponse<CategoryData> queryResponse = null;
		AdaptQueryRequest<CategoryData> queryRequest = new AdaptQueryRequest<>(new QueryRequest());
		CategoryData bean = new CategoryData();
		bean.setParent(did);
		queryRequest.setParam(bean);
		try {
			LOG.debug("调用sdk方法:searchByExample");
			queryResponse = categoryDataRepository.searchByExample(queryRequest);
		} catch (EspStoreException e) {
			LOG.error("检测维度数据是否存在子节点时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
		}
		if (queryResponse != null && queryResponse.getHits() != null&& queryResponse.getHits().getDocs() != null&& !queryResponse.getHits().getDocs().isEmpty()) {
			return true;
		}
		return false;
	}

			
}
