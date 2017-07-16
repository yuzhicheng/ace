package com.yzc.controller.paints;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yzc.models.paints.AuthorModel;
import com.yzc.models.paints.PaintModel;
import com.yzc.service.PaintService;
import com.yzc.support.CommonHelper;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.ValidResultHelper;
import com.yzc.support.enums.LifecycleStatus;
import com.yzc.support.enums.ResourceNdCode;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.MessageConvertUtil;
import com.yzc.utils.StringUtils;
import com.yzc.vos.ListViewModel;
import com.yzc.vos.paints.AuthorViewModel;
import com.yzc.vos.paints.PaintViewModel;
import com.yzc.vos.valid.PaintGroup;

/**
 * 名画API
 * 
 * @author yzc
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/paints")
public class PaintController {
	@Autowired
	private PaintService paintService;

	/**
	 * 创建名画
	 * 
	 * @param pvm
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public PaintViewModel create(@PathVariable String id,@Validated(PaintGroup.class) @RequestBody PaintViewModel pvm,
			BindingResult validResult) {
		// 入参合法性校验
		ValidResultHelper.valid(validResult, "LC/CREATE_PAINT_PARAM_VALID_FAIL", "PaintController", "create");

		// 校验status
		if (StringUtils.isNotEmpty(pvm.getEstatus())) {
			if (!LifecycleStatus.isLegalStatus(pvm.getEstatus())) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
						ErrorMessageMapper.StatusIsNotExist.getCode(), "资源状态不是有效值");
			}
		}

		pvm.setIdentifier(id);
		PaintModel pm = change2PaintModel(pvm);
		pm = paintService.savePaintModel(pm);
		pvm = change2PaintViewModel(pm);
		return pvm;
	}

	/**
	 * 修改名画
	 * 
	 * @param pvm
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public PaintViewModel update(@Validated(PaintGroup.class) @RequestBody PaintViewModel pvm,
			BindingResult validResult, @PathVariable(value = "id") String id) {
		// 入参合法性校验
		ValidResultHelper.valid(validResult, "LC/UPDATE_PAINT_PARAM_VALID_FAIL", "PaintControllerV06", "update");

		// 校验status
		if (StringUtils.isNotEmpty(pvm.getEstatus())) {
			if (!LifecycleStatus.isLegalStatus(pvm.getEstatus())) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
						ErrorMessageMapper.StatusIsNotExist.getCode(), "资源状态不是有效值");
			}
		}

		pvm.setIdentifier(id);
		PaintModel pm = change2PaintModel(pvm);
		pm = paintService.updatePaintModel(pm);
		pvm = change2PaintViewModel(pm);
		return pvm;
	}

	/**
	 * 获取名画详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public PaintViewModel getDetail(@PathVariable(value = "id") String id) {
		PaintModel pm = paintService.getPaintModel(id);
		PaintViewModel pvm = change2PaintViewModel(pm);
		return pvm;
	}

	/**
	 * 删除名画
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> delete(@PathVariable(value = "id") String id) {
		paintService.deletePaintModel(id);
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeletePaintSuccess);
	}

	/**
	 * 根据title、titleCn模糊查找名画列表
	 * 
	 * @param title
	 * @param tags
	 * @param authorName
	 * @param nationality
	 * @param creator
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/actions/query", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ListViewModel<PaintViewModel> queryListByCond(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "tags", required = false) List<String> tags,
			@RequestParam(value = "author_name", required = false) String authorName,
			@RequestParam(value = "nationality", required = false) String nationality,
			@RequestParam(value = "creator", required = false) String creator,
			@RequestParam(value = "limit") String limit) {
		ListViewModel<PaintViewModel> returnValue = new ListViewModel<PaintViewModel>();
		List<PaintViewModel> pvmList = new ArrayList<PaintViewModel>();
		ListViewModel<PaintModel> value = paintService.queryListByCond(title, tags, authorName, nationality, creator,limit);
		if (CollectionUtils.isNotEmpty(value.getItems())) {
			for (PaintModel pm : value.getItems()) {
				PaintViewModel pvm = change2PaintViewModel(pm);
				pvmList.add(pvm);
			}
		}
		returnValue.setItems(pvmList);
		returnValue.setLimit(value.getLimit());
		returnValue.setTotal(value.getTotal());
		return returnValue;
	}

	/**
	 * 创建名画作者
	 * 
	 * @param avm
	 * @return
	 */
	@RequestMapping(value = "/author", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public AuthorViewModel createAuthor(@Valid @RequestBody AuthorViewModel avm, BindingResult validResult) {
		// 入参合法性校验
		ValidResultHelper.valid(validResult, "LC/CREATE_AUTHOR_PARAM_VALID_FAIL", "PaintController", "createAuthor");

		AuthorModel am = BeanMapperUtils.beanMapper(avm, AuthorModel.class);
		am = paintService.saveAuthorModel(am);
		avm = BeanMapperUtils.beanMapper(am, AuthorViewModel.class);
		return avm;
	}

	/**
	 * 修改名画作者
	 * 
	 * @param avm
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/author/{id}", method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public AuthorViewModel updateAuthor(@Valid @RequestBody AuthorViewModel avm, BindingResult validResult,
			@PathVariable(value = "id") String id) {
		// 入参合法性校验
		ValidResultHelper.valid(validResult, "LC/UPDATE_AUTHOR_PARAM_VALID_FAIL", "PaintController", "updateAuthor");

		avm.setAuthorId(id);
		AuthorModel am = BeanMapperUtils.beanMapper(avm, AuthorModel.class);
		am = paintService.updateAuthorModel(am);
		avm = BeanMapperUtils.beanMapper(am, AuthorViewModel.class);
		return avm;
	}

	/**
	 * 获取名画作者详情信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/author/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public AuthorViewModel getAuthorDetail(@PathVariable(value = "id") String id) {
		AuthorModel am = paintService.getAuthorModel(id);
		AuthorViewModel avm = BeanMapperUtils.beanMapper(am, AuthorViewModel.class);
		return avm;
	}

	/**
	 * 删除名画作者
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/author/{id}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> deleteAuthor(@PathVariable(value = "id") String id) {
		paintService.deleteAuthorModel(id);
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeleteAuthorSuccess);
	}

	/**
	 * 根据条件查找作者列表
	 * 
	 * @param authorName
	 *            作者姓名
	 * @param limit
	 *            分页参数
	 * @return
	 */
	@RequestMapping(value = "/author/actions/query")
	public ListViewModel<AuthorViewModel> queryAuthorListByName(
			@RequestParam(required = false, value = "author_name") String authorName,
			@RequestParam(value = "limit") String limit) {
		ListViewModel<AuthorViewModel> returnValue = new ListViewModel<AuthorViewModel>();
		ListViewModel<AuthorModel> value = paintService.queryAuthorList(authorName, limit);
		List<AuthorViewModel> avmList = new ArrayList<AuthorViewModel>();
		if (CollectionUtils.isNotEmpty(value.getItems())) {
			for (AuthorModel am : value.getItems()) {
				AuthorViewModel vm = BeanMapperUtils.beanMapper(am, AuthorViewModel.class);
				avmList.add(vm);
			}
		}
		returnValue.setLimit(value.getLimit());
		returnValue.setTotal(value.getTotal());
		returnValue.setItems(avmList);
		return returnValue;
	}
	/*
      * 根据条件查找作者列表
	 * 
	 * @param authorName
	 * @nationality           
	 * @param limit        
	 * @return
	 */
	@RequestMapping(value = "/author/query")
	public ListViewModel<AuthorViewModel> queryAuthorByNameAndNationality(
			@RequestParam(required = true, value = "author_name") String authorName,
			@RequestParam(required = true, value = "nationality") String nationality,
			@RequestParam(value = "limit") String limit) {
		ListViewModel<AuthorViewModel> returnValue = new ListViewModel<AuthorViewModel>();
		ListViewModel<AuthorModel> value = paintService.queryAuthorByNameAndNationality(authorName,nationality,limit);
		List<AuthorViewModel> avmList = new ArrayList<AuthorViewModel>();
		if (CollectionUtils.isNotEmpty(value.getItems())) {
			for (AuthorModel am : value.getItems()) {
				AuthorViewModel vm = BeanMapperUtils.beanMapper(am, AuthorViewModel.class);
				avmList.add(vm);
			}
		}
		returnValue.setLimit(value.getLimit());
		returnValue.setTotal(value.getTotal());
		returnValue.setItems(avmList);
		return returnValue;
	}

	private PaintModel change2PaintModel(PaintViewModel pvm) {
		PaintModel pm = BeanMapperUtils.beanMapper(pvm, PaintModel.class);
	    if (pvm.getCategories() != null) {
	            pm.setCategoryList(CommonHelper.map2List4Categories(pvm.getCategories(), pvm.getIdentifier(), ResourceNdCode.paints, false));
	    }
		if (pvm.getTechInfo() != null) {
			pm.setTechInfoList(CommonHelper.map2List4TechInfo(pvm.getTechInfo()));
		}
		return pm;
	}

	private PaintViewModel change2PaintViewModel(PaintModel pm) {
		PaintViewModel pvm = BeanMapperUtils.beanMapper(pm, PaintViewModel.class);
		if(pm.getCategoryList() != null){
			pvm.setCategories(CommonHelper.list2map4Categories(pm.getCategoryList(), "res_type"));
		}
		if (pm.getTechInfoList() != null) {
			pvm.setTechInfo(CommonHelper.list2Map4TechInfo(pm.getTechInfoList()));
		}
		return pvm;
	}

}
