package com.yzc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yzc.config.WebConfig;
import com.yzc.models.StudentModel;
import com.yzc.service.StudentService;
import com.yzc.support.CommonHelper;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.ValidResultHelper;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.MessageConvertUtil;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.vos.ListViewModel;
import com.yzc.vos.student.StudentViewModel;

/**
 * 学生管理
 *
 * @author yzc
 * @date 2016年8月30
 * @title 学生管理
 */
@RestController // 默认为所有方法使用消息转换
@RequestMapping("/student")
public class StudentController {

    private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private StudentService studentService;

    // UUID格式,这个用于验证uuid的正则表达式存在一定的问题（不够严格）
    private final static String uuidReg = "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";

    /**
     * 创建学生
     *
     * @param svm
     * @param validResult
     * @return StudentViewModel
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public StudentViewModel createStudent(@PathVariable String id, @Valid @RequestBody StudentViewModel svm, BindingResult validResult) {

        // 校验
        ValidResultHelper.valid(validResult, "CREATE_STUDENT_PARAM_VALID_FAIL", "StudentController", "createStudent");
        //校验id
        CommonHelper.checkUuidPattern(id);
        svm.setIdentifier(id);

        StudentModel sm = studentService.createStudent(BeanMapperUtils.beanMapper(svm, StudentModel.class));
        return changeToViewModel(sm);
    }

    /**
     * 修改学生
     *
     * @param id
     * @param studentViewModel
     * @param validResult
     * @return StudentViewModel
     * @author yzc
     * @date 2016年9月1日
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public StudentViewModel updateStudent(@PathVariable String id,
                                          @Valid @RequestBody StudentViewModel studentViewModel, BindingResult validResult) {

        // 校验
        ValidResultHelper.valid(validResult, "LC/UPDATE_STUDENT_PARAM_VALID_FAIL", "StudentController",
                "updateStudent");

        studentViewModel.setIdentifier(id);
        StudentModel com = studentService
                .updateStudent(BeanMapperUtils.beanMapper(studentViewModel, StudentModel.class));

        return changeToViewModel(com);
    }

    /**
     * 修改学生
     *
     * @param id
     * @param studentViewModel
     * @param validResult
     * @return StudentViewModel
     * @author yzc
     * @date 2016年9月1日
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public StudentViewModel patchStudent(@PathVariable String id, @Valid @RequestBody StudentViewModel studentViewModel,
                                         BindingResult validResult) {

        // 校验
        ValidResultHelper.valid(validResult, "LC/UPDATE_STUDENT_PARAM_VALID_FAIL", "StudentController",
                "updateStudent");

        studentViewModel.setIdentifier(id);
        StudentModel com = studentService
                .patchStudent(BeanMapperUtils.beanMapper(studentViewModel, StudentModel.class));

        return changeToViewModel(com);
    }

    /**
     * 删除学生
     *
     * @param id
     * @return Map<String,String>
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, String> deleteStudent(@PathVariable String id) {

        studentService.deleteUser(id);

        return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeleteStudentSuccess);
    }

    /**
     * 默认查询学生，根据title或者description进行查询。
     *
     * @param words
     * @param limit
     * @return ListViewModel<StudentViewModel>
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ListViewModel<StudentViewModel> getStudentList(
            @RequestParam(value = "words", required = true) String words,
            @RequestParam(value = "limit", required = true) String limit) {
        // 检查limit参数
        ParamCheckUtil.checkLimit(limit);// 有抛出异常
        // 调用service 接口
        ListViewModel<StudentModel> modelListResult = null;

        modelListResult = studentService.getStudentList(words, limit);

        // 结果转换 ，数据转化，有没有更好的方式， 内部泛型数组，使用 ModelMapper 需要一个个转。
        ListViewModel<StudentViewModel> viewListResult = new ListViewModel<StudentViewModel>();
        viewListResult.setLimit(modelListResult.getLimit());
        viewListResult.setTotal(modelListResult.getTotal());
        List<StudentModel> modelItems = modelListResult.getItems();
        List<StudentViewModel> viewItems = new ArrayList<StudentViewModel>();
        if (modelItems != null && !modelItems.isEmpty()) {
            for (StudentModel model : modelItems) {
                StudentViewModel viewModel = changeToViewModel(model);
                viewItems.add(viewModel);
            }
        }
        viewListResult.setItems(viewItems);
        return viewListResult;
    }

    /**
     * 模糊查询学生: 根据username字段模糊查询
     *
     * @param words
     * @param limit
     * @return ListViewModel<StudentViewModel>
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(value = {"/usernames", "/username"}, method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ListViewModel<StudentViewModel> readStudentList(@RequestParam String words, @RequestParam String limit) {

        ListViewModel<StudentModel> comList = studentService.readStudentList(words, limit);

        ListViewModel<StudentViewModel> viewListResult = new ListViewModel<StudentViewModel>();
        viewListResult.setLimit(comList.getLimit());
        viewListResult.setTotal(comList.getTotal());
        List<StudentModel> modelItems = comList.getItems();
        List<StudentViewModel> viewItems = new ArrayList<StudentViewModel>();
        if (CollectionUtils.isNotEmpty(modelItems)) {
            for (StudentModel model : modelItems) {
                StudentViewModel viewModel = changeToViewModel(model);
                viewItems.add(viewModel);
            }
        }
        viewListResult.setItems(viewItems);
        return viewListResult;
    }

    /**
     * 条件查询学生: 根据words关键字(title,description)精确查询,同时可以用可选参数username查询，两者之间的关系为and
     *
     * @param words
     * @param limit
     * @return ListViewModel<StudentViewModel>
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(value = "/condition", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ListViewModel<StudentViewModel> queryStudentList(
            @RequestParam(value = "words", required = true) String words,
            @RequestParam(value = "limit", required = true) String limit,
            @RequestParam(value = "username", required = false) String username) {

        ListViewModel<StudentModel> comList = studentService.queryStudentList(words, limit, username);

        ListViewModel<StudentViewModel> viewListResult = new ListViewModel<StudentViewModel>();
        viewListResult.setLimit(comList.getLimit());
        viewListResult.setTotal(comList.getTotal());
        List<StudentModel> modelItems = comList.getItems();
        List<StudentViewModel> viewItems = new ArrayList<StudentViewModel>();
        if (CollectionUtils.isNotEmpty(modelItems)) {
            for (StudentModel model : modelItems) {
                StudentViewModel viewModel = changeToViewModel(model);
                viewItems.add(viewModel);
            }
        }
        viewListResult.setItems(viewItems);
        return viewListResult;
    }

    /**
     * 查看学生
     *
     * @param content 可能是id和 username
     * @return
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(value = "/{content}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public StudentViewModel getDetailStudent(@PathVariable("content") String content) {
        StudentModel resultModel = null;
        // 两个接口，共用一个函数，需要判断是否为id
        if (checkReg(content, uuidReg)) {

            resultModel = studentService.queryStudentById(content);
        } else {

            resultModel = studentService.queryStudentByUsername(content);
        }

        if (null != resultModel) {
            return changeToViewModel(resultModel);
        }
        return null;
    }

    /**
     * 批量删除学生
     *
     * @param idSet
     * @return Map
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(value = "/batch/delete", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public Map<String, String> batchDeleteStudent(
            @RequestParam(value = "student_id", required = true) LinkedHashSet<String> idSet) {

        studentService.batchDeteleStudent(idSet);
        return MessageConvertUtil.getMessageString(ErrorMessageMapper.BatchDeleteStudentSuccess);
    }

    /**
     * 批量创建学生
     *
     * @param paramList
     * @param bindingResult
     * @return List
     */
    @RequestMapping(value = "/batch/create", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    List<StudentViewModel> batchAddStudent(@Valid @RequestBody List<StudentViewModel> paramList,
                                           BindingResult bindingResult) {
        // 入参校验
//		ValidResultHelper.valid(bindingResult, "BATCH_CREATE_STUDENT_PARAM_VALID_FAIL", "StudentController","batchAddStudent");
        ResourceBundleMessageSource source = WebConfig.getResourceBundleMessageSource();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<StudentViewModel>> allViolations = new HashSet<ConstraintViolation<StudentViewModel>>();
        for (StudentViewModel svm : paramList) {
            Set<ConstraintViolation<StudentViewModel>> violations = validator.validate(svm);
            allViolations.addAll(violations);
        }
        if (!allViolations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            List<String> el = new ArrayList<String>();
            for (ConstraintViolation<StudentViewModel> violation : allViolations) {
                String msg = violation.getMessage();
                String em = source.getMessage(msg.substring(1, msg.length() - 1), null, LocaleContextHolder.getLocale());
                if (!el.contains(em)) {
                    errors.append(em + ";");
                    el.add(em);
                }
            }
            throw new MessageException("BATCH_CREATE_STUDENT_PARAM_VALID_FAIL", errors.toString());
        }
        // 判断批量数据中是否存在重复的username
        if (paramList != null && paramList.size() > 1) {
            Set<String> duplicateHelperSet = new HashSet<String>();
            for (StudentViewModel view : paramList) {
                if (view != null) {
                    String content = view.getUsername();
                    boolean isSuccess = duplicateHelperSet.add(content);
                    if (!isSuccess) {
                        LOG.error(ErrorMessageMapper.BatchAddStudentDuplicate.getMessage());
                        throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
                                ErrorMessageMapper.BatchAddStudentDuplicate);
                    }
                }
            }
        }

        // model转换，生成参数
        List<StudentModel> studentModelList = new ArrayList<StudentModel>();
        for (StudentViewModel studentViewModel : paramList) {
            StudentModel paramModel = BeanMapperUtils.beanMapper(studentViewModel, StudentModel.class);
            studentModelList.add(paramModel);
        }

        // 调用service 接口
        List<StudentModel> resultModelList = null;
        resultModelList = studentService.batchCreateStudent(studentModelList);

        // 转成出参
        List<StudentViewModel> resultViewModelList = new ArrayList<StudentViewModel>();
        if (resultModelList != null && !resultModelList.isEmpty()) {
            for (StudentModel model : resultModelList) {
                if (model != null) {
                    resultViewModelList.add(changeToViewModel(model));
                }
            }
        }

        return resultViewModelList;
    }

    /**
     * 批量加载学生
     *
     * @param studentNameSet
     * @return Map
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, StudentViewModel> batchGetDetailStudent(
            @RequestParam(value = "student_name", required = true) Set<String> studentNameSet) {

        Map<String, StudentModel> modelMap = null;
        modelMap = studentService.batchGetDetailStudent(studentNameSet);

        // 处理出参
        Map<String, StudentViewModel> viewModelMap = new HashMap<String, StudentViewModel>();
        if (modelMap != null && !modelMap.isEmpty()) {
            for (String ndCode : modelMap.keySet()) {
                StudentViewModel viewModel = null;
                StudentModel model = modelMap.get(ndCode);
                if (model != null) {
                    viewModel = BeanMapperUtils.beanMapper(model, StudentViewModel.class);
                }
                viewModelMap.put(ndCode, viewModel);
            }
        }
        return viewModelMap;
    }

    /**
     * 根据username批量加载学生
     *
     * @param usernameSet
     * @return Map
     * @author yzc
     * @date 2016年9月1日
     */
    @RequestMapping(value = "/username/list", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public Map<String, StudentViewModel> batchGetDetailStudentByUsername(
            @RequestParam(value = "username", required = true) Set<String> usernameSet) {

        Map<String, StudentModel> modelMap = null;
        modelMap = studentService.batchGetDetailStudentByUsername(usernameSet);

        // 处理出参
        Map<String, StudentViewModel> viewModelMap = new HashMap<String, StudentViewModel>();
        if (modelMap != null && !modelMap.isEmpty()) {
            for (String ndCode : modelMap.keySet()) {
                StudentViewModel viewModel = null;
                StudentModel model = modelMap.get(ndCode);
                if (model != null) {
                    viewModel = BeanMapperUtils.beanMapper(model, StudentViewModel.class);
                }
                viewModelMap.put(ndCode, viewModel);
            }
        }
        return viewModelMap;
    }

    /**
     * Model转成viewModel
     *
     * @param um
     * @return
     * @author yzc
     * @date 2016年10月9日
     */
    private StudentViewModel changeToViewModel(StudentModel um) {

        return BeanMapperUtils.beanMapper(um, StudentViewModel.class);
    }

    /**
     * 正则校验
     *
     * @param value   值
     * @param pattern 正则表达式
     * @return boolean
     * @author yzc
     */
    private boolean checkReg(String value, String pattern) {
        return Pattern.matches(pattern, value);
    }

}
