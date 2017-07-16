package com.yzc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yzc.entity.User;
import com.yzc.message.impl.UserMessageServiceImpl;
import com.yzc.models.user.UserModel;
import com.yzc.service.UserService;
import com.yzc.support.CommonHelper;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.ValidResultHelper;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.MessageConvertUtil;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.vos.ListViewModel;
import com.yzc.vos.user.UserViewModel;

/**
 * 用户管理
 * 
 * @author yzc
 * @date 2016年8月30
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Qualifier("userMessageServiceImpl")
	@Autowired
	private UserMessageServiceImpl userMessageServiceImpl;
	
//	@Autowired
//	protected RabbitTemplate rabbitTemplate;

	/**
	 * 创建用户
	 * 
	 * @author
	 * @date 2016年9月1日
	 * @param uvm
	 * @param validResult
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public UserViewModel createUser(@PathVariable String id,@Valid @RequestBody UserViewModel uvm,
			BindingResult validResult) {

		// 校验
		ValidResultHelper.valid(validResult, "CREATE_USER_PARAM_VALID_FAIL","UserController", "createUser");
		
		uvm.setIdentifier(id);
		UserModel um = userService.createUser(BeanMapperUtils.beanMapper(uvm,UserModel.class));
		
		return changeToViewModel(um);
	}

	/**
	 * 修改用户
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param id
	 * @param userViewModel
	 * @param validResult
	 * @return
	 */

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public UserViewModel updateUser(@PathVariable String id,
			@Valid @RequestBody UserViewModel userViewModel,
			BindingResult validResult) {

		// 校验
		ValidResultHelper.valid(validResult, "LC/UPDATE_USER_PARAM_VALID_FAIL","UserController", "updateUser");
		//校验id
		CommonHelper.checkUuidPattern(id);
		userViewModel.setIdentifier(id);
		
		UserModel com = userService.updateUser(BeanMapperUtils.beanMapper(userViewModel, UserModel.class));

		return changeToViewModel(com);
	}

	/**
	 * 删除用户
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> deleteUser(@PathVariable String id) {

		userService.deleteUser(id);

		return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeleteUserSuccess);
	}

	/**
	 * 分页查询用户列表
	 * 
	 * @author yzc
	 * @date 2016年9月2日
	 * @param words
	 *            查询关键字
	 * @param limit
	 *            查询分页limit(0,10)，第一个参数为页的索引，第二个参数为每页的数量
	 * @return
	 */
	@RequestMapping(value = "/all/user", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<User> getUserListPage(@RequestParam String words,
			@RequestParam String limit) {

		// limit校验
		ParamCheckUtil.checkLimit(limit);
		List<User> comList = userService.getUserListPage(words, limit);

		return comList;
	}

	/**
	 * 模糊查询用户列表
	 * 
	 * @author yzc
	 * @date 2016年9月2日
	 * @param words
	 *            查询关键字
	 * @param limit
	 *            查询分页limit(0,10)，第一个参数为记录的索引，第二个参数为偏移量
	 * @return ListViewModel<UserViewModel>
	 */
	@RequestMapping(value = "/fuzzy/words/list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ListViewModel<UserViewModel> fuzzyGetUserList(@RequestParam String words,
			@RequestParam String limit) {

		// limit校验
		ParamCheckUtil.checkLimit(limit);

		ListViewModel<UserModel> comList = userService.fuzzyGetUserList(words, limit);

		ListViewModel<UserViewModel> viewListResult = new ListViewModel<UserViewModel>();
		viewListResult.setLimit(comList.getLimit());
		viewListResult.setTotal(comList.getTotal());
		List<UserModel> modelItems = comList.getItems();
		List<UserViewModel> viewItems = new ArrayList<UserViewModel>();
		if (CollectionUtils.isNotEmpty(modelItems)) {
			for (UserModel model : modelItems) {
				UserViewModel viewModel = changeToViewModel(model);
				viewItems.add(viewModel);
			}
		}
		viewListResult.setItems(viewItems);
		return viewListResult;
	}
	/**
	 * 精确查询用户列表
	 * 
	 * @author yzc
	 * @date 2016年9月2日
	 * @param words
	 *            查询关键字
	 * @param limit
	 *            查询分页limit(0,10)，第一个参数为记录的索引，第二个参数为偏移量
	 * @return ListViewModel<UserViewModel>
	 */
	@RequestMapping(value = "/words/list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ListViewModel<UserViewModel> getUserList(@RequestParam String words,
			@RequestParam String limit) {

		// limit校验
		ParamCheckUtil.checkLimit(limit);

		ListViewModel<UserModel> comList = userService.getUserList(words, limit);

		ListViewModel<UserViewModel> viewListResult = new ListViewModel<UserViewModel>();
		viewListResult.setLimit(comList.getLimit());
		viewListResult.setTotal(comList.getTotal());
		List<UserModel> modelItems = comList.getItems();
		List<UserViewModel> viewItems = new ArrayList<UserViewModel>();
		if (CollectionUtils.isNotEmpty(modelItems)) {
			for (UserModel model : modelItems) {
				UserViewModel viewModel = changeToViewModel(model);
				viewItems.add(viewModel);
			}
		}
		viewListResult.setItems(viewItems);
		return viewListResult;
	}

	/**
	 * 自定义资源查询
	 * 
	 * @author yzc
	 * @date 2016年8月4日
	 * @param id
	 * @param include
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<UserViewModel> QueryUser(
			@PathVariable(value = "id") String id,
			@RequestParam(required = false, value = "include", defaultValue = "BASE") String include) {

		List<UserModel> list = userService.queryUser(id, include);

		List<UserViewModel> uvmlist = new ArrayList<UserViewModel>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (UserModel model : list) {
				UserViewModel uvm = new UserViewModel();
				uvm.setIdentifier(model.getIdentifier());
				uvm.setUsername(model.getUsername());
				uvm.setPassword(model.getPassword());
				uvmlist.add(uvm);
			}
		}
		return uvmlist;
	}

	/**
	 * 查询用户信息，可以根据title或者description进行查询。
	 * 
	 * @URLPattern /students?{words=123}&limit=(0,20)}
	 * @Method GET
	 * 
	 * @param words
	 * @param limit
	 */
	@RequestMapping(value = { "/search", "/student" }, method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ListViewModel<UserViewModel> requestQueryUser(
			@RequestParam(value = "words", required = true) String words,
			@RequestParam(value = "limit", required = true) String limit) {
		// 检查limit参数
		ParamCheckUtil.checkLimit(limit);// 有抛出异常
		// 调用service 接口
		ListViewModel<UserModel> modelListResult = null;

		modelListResult = userService.queryUserList(words, limit);

		// 结果转换 ，数据转化，有没有更好的方式， 内部泛型数组，使用 ModelMapper 需要一个个转。
		ListViewModel<UserViewModel> viewListResult = new ListViewModel<UserViewModel>();
		viewListResult.setLimit(modelListResult.getLimit());
		viewListResult.setTotal(modelListResult.getTotal());
		List<UserModel> modelItems = modelListResult.getItems();
		List<UserViewModel> viewItems = new ArrayList<UserViewModel>();
		if (modelItems != null && !modelItems.isEmpty()) {
			for (UserModel model : modelItems) {
				UserViewModel viewModel = changeToViewModel(model);
				viewItems.add(viewModel);
			}
		}
		viewListResult.setItems(viewItems);
		return viewListResult;
	}

	/**
	 * Model转成viewModel
	 * 
	 * @author yzc
	 * @date 2016年10月9日
	 * @param um
	 * @return
	 */
	private UserViewModel changeToViewModel(UserModel um) {

		return BeanMapperUtils.beanMapper(um, UserViewModel.class);
	}
	
	@RequestMapping(value = "/send/message", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> sendUser(@RequestBody UserViewModel uvm) {

//	    this.rabbitTemplate.convertAndSend("","ace_queue",ObjectUtils.toJson(uvm));
	    userMessageServiceImpl.send(uvm);

		return MessageConvertUtil
				.getMessageString(ErrorMessageMapper.DeleteUserSuccess);
	}
	
}
