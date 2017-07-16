package com.yzc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yzc.models.CategoryDataModel;
import com.yzc.models.CategoryModel;
import com.yzc.service.CategoryService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.ValidResultHelper;
import com.yzc.support.enums.AreaAndLanguage;
import com.yzc.support.enums.NdCodePattern;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.MessageConvertUtil;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.utils.StringUtils;
import com.yzc.vos.CategoryDataViewModel;
import com.yzc.vos.CategoryViewModel;
import com.yzc.vos.ListViewModel;

@RestController
@RequestMapping(value="/category")
public class CategoryController {

	private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	@Qualifier("CategoryServiceImpl")
	private CategoryService categoryService;
	
	/**
	 * 创建一个分类维度,需要明确其目的以及对于分类维度的schema的描述，这个模式的描述一般都是树形结构，描述其约束关系
	 * 
	 * @author yzc
	 * @date 2016年12月1日
	 * @param categoryViewModel
	 * @param bindingResult
	 * @return CategoryViewModel
	 */
	@RequestMapping(value = { "/categorys", "/categories" }, method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public CategoryViewModel createCategory(@Valid @RequestBody CategoryViewModel categoryViewModel,BindingResult bindingResult) {
		
		// 入参校验
		ValidResultHelper.valid(bindingResult,ErrorMessageMapper.InvalidArgumentsError.getCode());
		// 校验ndCode
		checkCategoryNdCode(categoryViewModel.getNdCode());
		// 校验gbCode类型
		if (StringUtils.hasText(categoryViewModel.getGbCode())) {
			String realGbCode = AreaAndLanguage.validGbCodeType(categoryViewModel.getGbCode());
			if (StringUtils.isEmpty(realGbCode)) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.GbCodeNotExist);
			}
			categoryViewModel.setGbCode(realGbCode);
		}
		// 生成参数
		CategoryModel paramModel = BeanMapperUtils.beanMapper(categoryViewModel, CategoryModel.class);
		paramModel.setIdentifier(UUID.randomUUID().toString());// 设置UUID;
		// 调用service 接口
		CategoryModel resultModel = categoryService.creatCategory(paramModel);
		// 转成出参
		CategoryViewModel resultViewModel = BeanMapperUtils.beanMapper(resultModel, CategoryViewModel.class);
		return resultViewModel;
	}
	
	/**
	 * 修改维度分类，主要修改维度分类的名称，缩写，介绍以及schema等信息
	 * 
	 * @author yzc
	 * @date 2016年12月1日
	 * @param categoryViewModel
	 * @param bindingResult
	 * @return CategoryViewModel
	 */
	@RequestMapping(value = { "/categorys/{cid}", "/categories/{cid}" }, method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public  CategoryViewModel updateCategory(@PathVariable String cid,@Valid @RequestBody CategoryViewModel categoryViewModel,BindingResult bindingResult) {
		// 入参校验
		ValidResultHelper.valid(bindingResult,ErrorMessageMapper.InvalidArgumentsError.getCode());
		// ndCode正则校验
		checkCategoryNdCode(categoryViewModel.getNdCode());

		// 校验gbCode类型
		if (StringUtils.hasText(categoryViewModel.getGbCode())) {
			String realGbCode = AreaAndLanguage.validGbCodeType(categoryViewModel.getGbCode());
			if (StringUtils.isEmpty(realGbCode)) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.GbCodeNotExist);
			}
			categoryViewModel.setGbCode(realGbCode);
		}

		categoryViewModel.setIdentifier(cid);
		CategoryModel modifyModel = BeanMapperUtils.beanMapper(categoryViewModel, CategoryModel.class);

		CategoryModel resultModel = categoryService.updateCategory(modifyModel);
		CategoryViewModel resultViewModel = BeanMapperUtils.beanMapper(resultModel, CategoryViewModel.class);
		return resultViewModel;
	}
	
	/**
	 * 查询分类信息，可以根据名称或者介绍进行查询分类维度
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param words
	 * @param limit
	 * @return ListViewModel<CategoryViewModel>
	 */
	@RequestMapping(value = { "/categorys", "/categories" }, method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ListViewModel<CategoryViewModel> readCategory(@RequestParam(value = "words", required = true) String words,@RequestParam(value = "limit", required = true) String limit) {
		// 检查limit参数
		ParamCheckUtil.checkLimit(limit);// 有抛出异常
		// 调用service 接口
		ListViewModel<CategoryModel> modelListResult = categoryService.readCategory(words, limit);

		// 结果转换 ，数据转化，有没有更好的方式， 内部泛型数组，使用 ModelMapper 需要一个个转。
		ListViewModel<CategoryViewModel> viewListResult = new ListViewModel<CategoryViewModel>();
		viewListResult.setLimit(modelListResult.getLimit());
		viewListResult.setTotal(modelListResult.getTotal());
		List<CategoryModel> modelItems = modelListResult.getItems();
		List<CategoryViewModel> viewItems = new ArrayList<CategoryViewModel>();
		if (modelItems != null && !modelItems.isEmpty()) {
			for (CategoryModel model : modelItems) {
				CategoryViewModel viewModel = BeanMapperUtils.beanMapper(model,CategoryViewModel.class);
				viewItems.add(viewModel);
			}
		}
		viewListResult.setItems(viewItems);
		return viewListResult;
	}
	
	/**
	 * 删除一个维度，维度删除的时候，需要将维度所关联的维度数据一并删除
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param cid 分类维度的唯一标识
	 * @return Map<String,String>
	 */
	@RequestMapping(value = { "/categorys/{cid}", "/categories/{cid}" }, method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> deleteCategory(@PathVariable String cid) {

			categoryService.deleteCategory(cid);
	
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeleteCategorySuccess);
	}
	
	/**
	 * 检查categoryNdCode 是不是符合规范且已经备案，若无，则抛出异常
	 * 
	 * @param ndCode
	 */
	private void checkCategoryNdCode(String ndCode) {
		// ndCode正则校验
		boolean ndCodeFlag = checkNdCodeRegex(ndCode);
		if (!ndCodeFlag) {
			LOG.error(ErrorMessageMapper.CheckNdCodeRegularFail.getMessage() + ndCode);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckNdCodeRegularFail);
		}
		NdCodePattern ndCodePattern = NdCodePattern.fromString(ndCode);
		if (ndCodePattern == null) {
			// 不允许创建该分类维度，请到LC备案
			LOG.error("分类维度nd_code=" + ndCode + " 还没有备案，有需要请与LC沟通");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckNdCodeRegularFail.getCode(),"分类维度nd_code=" + ndCode + " 还没有备案，有需要请与LC沟通");
		}
	}
	
	/**
	 * ND编码标识正则校验 规则：两位大写英文字母标识，可以首位可以使用$符号开始，第二位不允许出现$符号
	 * 
	 * @param ndCode
	 * @return
	 */
	private static boolean checkNdCodeRegex(String ndCode) {
		Pattern pattern = Pattern.compile("^[A-Z]{2}$|^\\$[A-Z]{1}$");
		Matcher matcher = pattern.matcher(ndCode);
		boolean f = matcher.find();
		return f;
	}
	
	/**
	 * 创建分类维度数据
	 * 
	 * @author yzc
	 * @date 2016年12月1日
	 * @param categoryDataViewModel
	 * @param bindingResult
	 * @return CategoryDataViewModel
	 */
	@RequestMapping(value = "/categorys/datas", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public CategoryDataViewModel createCategoryData(@Valid @RequestBody CategoryDataViewModel categoryDataViewModel,BindingResult bindingResult) {
		// 入参校验
		ValidResultHelper.valid(bindingResult,ErrorMessageMapper.InvalidArgumentsError.getCode());
		// 校验gbCode类型
		if (StringUtils.hasText(categoryDataViewModel.getGbCode())) {
			String realGbCode = AreaAndLanguage.validGbCodeType(categoryDataViewModel.getGbCode());
			if (StringUtils.isEmpty(realGbCode)) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.GbCodeNotExist);
			}
			categoryDataViewModel.setGbCode(realGbCode);
		}
		// 生成参数
		CategoryDataModel paramModel = changeCategoryDataFromView(categoryDataViewModel);
		
		paramModel.setIdentifier(UUID.randomUUID().toString());// 设置UUID;
		// 调用service 接口
		CategoryDataModel resultModel = categoryService.createCategoryData(paramModel);
		// 转成出参
		CategoryDataViewModel resultViewModel = changeCategoryDataToView(resultModel);

		return resultViewModel;
	}
	
	/**
	 * 修改分类维度数据
	 * 
	 * @author yzc
	 * @date 2016年12月1日
	 * @param did
	 * @param categoryDataViewModel
	 * @param bindingResult
	 * @return CategoryDataViewModel
	 */
	@RequestMapping(value = "/categorys/datas/{did}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public CategoryDataViewModel updateCategoryData(
			@PathVariable String did,
			@Valid @RequestBody CategoryDataViewModel categoryDataViewModel,
			BindingResult bindingResult) {
		// 入参校验
		ValidResultHelper.valid(bindingResult,ErrorMessageMapper.InvalidArgumentsError.getCode());

		// 校验gbCode类型
		if (StringUtils.hasText(categoryDataViewModel.getGbCode())) {
			String realGbCode = AreaAndLanguage
					.validGbCodeType(categoryDataViewModel.getGbCode());
			if (StringUtils.isEmpty(realGbCode)) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.GbCodeNotExist);
			}
			categoryDataViewModel.setGbCode(realGbCode);
		}
		
		categoryDataViewModel.setIdentifier(did);
		CategoryDataModel modifyModel = changeCategoryDataFromView(categoryDataViewModel);

		CategoryDataModel resultModel=categoryService.updateCategoryData(modifyModel);
		
		CategoryDataViewModel resultViewModel = changeCategoryDataToView(resultModel);

		return resultViewModel;
	}
	
	/**
	 * 查询分类信息。分类信息的查询是根据关联的树形结构进行查询，根据等级进行查询 根据上级节点类型和名称模糊匹配下级节点内容
	 * 
	 * @author yzc
	 * @date 2016年12月1日
	 * @param ndCode 分类维度的nd_code
	 * @param words
	 * @param limit
	 * @param parent 父节点的id，为ROOT的时候，表示当前分类维度下的根节点
	 * @return ListViewModel<CategoryDataViewModel>
	 */
	@RequestMapping(value = "/categorys/{nd_code}/datas", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ListViewModel<CategoryDataViewModel> readCategoryData(@PathVariable("nd_code") String ndCode,
			@RequestParam(value = "parent", required = false) String parent,
			@RequestParam(value = "limit", required = true) String limit,
			@RequestParam(value = "words", required = true) String words,
			@RequestParam(value = "is_default", required = false, defaultValue = "1") Integer isDefault) {

		// 检查limit参数
		ParamCheckUtil.checkLimit(limit);// 有抛出异常
		// 调用service 接口
		ListViewModel<CategoryDataModel> modelListResult = categoryService.readCategoryData(ndCode, true,parent, words, limit, isDefault);
		ListViewModel<CategoryDataViewModel> viewListResult = new ListViewModel<CategoryDataViewModel>();
		viewListResult.setLimit(modelListResult.getLimit());
		viewListResult.setTotal(modelListResult.getTotal());
		List<CategoryDataModel> modelItems = modelListResult.getItems();
		List<CategoryDataViewModel> viewItems = new ArrayList<CategoryDataViewModel>();
		if (modelItems != null && !modelItems.isEmpty()) {
			for (CategoryDataModel model : modelItems) {
				CategoryDataViewModel viewModel = changeCategoryDataToView(model);
				viewItems.add(viewModel);
			}
		}
		viewListResult.setItems(viewItems);
		return viewListResult;
	}
	
	/**
	 * 删除分类维度数据信息
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param did 分类维度数据的唯一标识
	 * @return Map<String,String>
	 */
	@RequestMapping(value = "/categorys/datas/{did}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> deleteCategoryData(@PathVariable String did) {
		
			categoryService.deleteCategoryData(did);
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeleteCategoryDataSuccess);
	}
	
	/**
	 * 将维度数据转换成生命周期类型
	 * @param categoryDataViewModel
	 * @return
	 * @author yzc
	 */
	private CategoryDataModel changeCategoryDataFromView(CategoryDataViewModel categoryDataViewModel) {
		if (categoryDataViewModel == null) {
			return null;
		}
		Integer isDefault = categoryDataViewModel.getIsDefault();
		if (null == isDefault) {
			categoryDataViewModel.setIsDefault(1);
		}
		CategoryDataModel paramModel = BeanMapperUtils.beanMapper(categoryDataViewModel, CategoryDataModel.class);
		CategoryModel category = new CategoryModel();
		category.setIdentifier(categoryDataViewModel.getCategory());
		paramModel.setCategory(category);
		CategoryDataModel parent = new CategoryDataModel();
		parent.setIdentifier(categoryDataViewModel.getParent());
		paramModel.setParent(parent);

		return paramModel;
	}
	/**
	 * 将维度数据转换成view类型
	 * @param resultModel
	 * @return
	 * @author yzc
	 */
	private CategoryDataViewModel changeCategoryDataToView(CategoryDataModel resultModel) {
		if (resultModel == null) {
			return null;
		}
		CategoryDataViewModel resultViewModel = BeanMapperUtils.beanMapper(resultModel, CategoryDataViewModel.class);
		resultViewModel.setCategory(resultModel.getCategory().getIdentifier());
		resultViewModel.setParent(resultModel.getParent().getIdentifier());
		return resultViewModel;
	}
}
