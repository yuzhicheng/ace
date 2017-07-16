package com.yzc.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import com.yzc.models.CoverageModelForUpdate;
import com.yzc.models.ResClassificationModel;
import com.yzc.models.ResCoverageModel;
import com.yzc.models.ResTechInfoModel;
import com.yzc.models.ResourceModel;
import com.yzc.support.enums.AreaAndLanguage;
import com.yzc.support.enums.OperationType;
import com.yzc.support.enums.ResourceNdCode;
import com.yzc.support.statics.Constant;
import com.yzc.support.statics.CoverageConstant;
import com.yzc.utils.ArrayUtils;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.utils.StringUtils;
import com.yzc.vos.ResClassificationViewModel;
import com.yzc.vos.ResCoverageViewModel;
import com.yzc.vos.ResEducationalViewModel;
import com.yzc.vos.ResLifeCycleViewModel;
import com.yzc.vos.ResRelationViewModel;
import com.yzc.vos.ResRightViewModel;
import com.yzc.vos.ResTechInfoViewModel;
import com.yzc.vos.ResourceViewModel;
import com.yzc.vos.constant.IncludesConstant;

public class CommonHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommonHelper.class);
//	private static final String CATEGORY_PATTERN_SEPARTOR="/";
//	private static final String DEFAULT_CATEGORY_PATTERN="K12";
	private static final String HREF = "href";
	private static final String SOURCE = "source";
	private final static ExecutorService primaryExecutorService = Executors.newCachedThreadPool();
		
	/**
	 * 高优先级线程池
	 * 
	 * @return
	 */
	public static ExecutorService getPrimaryExecutorService() {
		return primaryExecutorService;
	}

	/**
	 * 用于tech_info的model转换,map->list
	 * 
	 * @author yzc
	 * @param map
	 * @return
	 */
	public static List<ResTechInfoModel> map2List4TechInfo(Map<String, ? extends ResTechInfoViewModel> map) {
		List<ResTechInfoModel> resultList = new ArrayList<ResTechInfoModel>();

		for (String key : map.keySet()) {
			ResTechInfoViewModel rtvm = map.get(key);
			if (rtvm != null) {
				rtvm.setTitle(key);
				ResTechInfoModel resTechInfoModel = BeanMapperUtils.beanMapper(rtvm, ResTechInfoModel.class);
				resultList.add(resTechInfoModel);
			}

		}

		return resultList;
	}

	/**
	 * 用于tech_info的model转换,list->map
	 * 
	 * @author yzc
	 * @param list
	 * @return
	 */
	public static Map<String, ? extends ResTechInfoViewModel> list2Map4TechInfo(List<? extends ResTechInfoModel> list) {

		return list2Map4TechInfo(list, null);
	}

	/**
	 * list2Map4TechInfo扩展 -- 根据终端信息过滤
	 * 
	 * @author yzc
	 * @date 2016年9月20日
	 * @param list
	 * @param terminal
	 * @return
	 */
	public static Map<String, ? extends ResTechInfoViewModel> list2Map4TechInfo(List<? extends ResTechInfoModel> list,String terminal) {
		Map<String, ResTechInfoViewModel> resultMap = new HashMap<String, ResTechInfoViewModel>();

		for (ResTechInfoModel rtm : list) {
			ResTechInfoViewModel resTechInfoViewModel = BeanMapperUtils.beanMapper(rtm, ResTechInfoViewModel.class);

			if (StringUtils.hasText(terminal)) {
				if (rtm.getTitle().equals(HREF) || rtm.getTitle().equals(SOURCE)
						|| rtm.getTitle().startsWith(terminal)) {
					resultMap.put(rtm.getTitle(), resTechInfoViewModel);
				}
			} else {
				resultMap.put(rtm.getTitle(), resTechInfoViewModel);
			}
		}

		return resultMap;
	}
	
	public static List<ResClassificationModel> map2List4Categories(Map<String, List<? extends ResClassificationViewModel>> map,String resourceId, ResourceNdCode rc) {
		return map2List4Categories(map, resourceId, rc, false);
	}
	
	/**
	 * 用于categories的model转换,map->list
	 * 
	 * @param map 需要转换的map
	 * @param resourceId  资源的id
	 * @return
	 */
	public static List<ResClassificationModel> map2List4Categories(Map<String, List<? extends ResClassificationViewModel>> map,String resourceId, ResourceNdCode rc, boolean patchMode) {
		List<ResClassificationModel> resultList = new ArrayList<ResClassificationModel>();
		// 用于判断是否有资源类型的ND_CODE
		boolean defaultResCode = false;

		for (String key : map.keySet()) {
			List<? extends ResClassificationViewModel> rcvmList = map.get(key);
			ResClassificationModel resClassificationModel;
			int index = 0;

			if (key.equals("phase")) {
				index = 1;
			} else if (key.equals("grade")) {
				index = 2;
			} else if (key.equals("subject")) {
				index = 3;
			} else if (key.equals("edition")) {
				index = 4;
			} else if (key.equals("sub_edition")) {
				index = 5;
			}

			if (index > 0) {// phase,grade,subject,edition,sub_edition
				for (ResClassificationViewModel rcvm : rcvmList) {
					if (!StringUtils.isEmpty(rcvm.getTaxonpath())) {// taxonpath不为空
						resClassificationModel = new ResClassificationModel();
						if (!patchMode) {
							resClassificationModel.setIdentifier(UUID.randomUUID().toString());
						} else {
							resClassificationModel.setIdentifier(rcvm.getIdentifier());
						}
						resClassificationModel.setResourceId(resourceId);
						resClassificationModel.setTaxonpath(rcvm.getTaxonpath());
						resClassificationModel.setTaxoncode(Arrays.asList(rcvm.getTaxonpath().split("/")).get(index));
						resClassificationModel.setOperation(rcvm.getOperation());

						// 如果taxonCode为空就不加入到resultList中
						if (!StringUtils.isEmpty(Arrays.asList(
								rcvm.getTaxonpath().split("/")).get(index))) {
							resultList.add(resClassificationModel);
						}
					} else if (StringUtils.hasText(rcvm.getTaxoncode())) {
						resClassificationModel = new ResClassificationModel();
						if (!patchMode) {
							resClassificationModel.setIdentifier(UUID.randomUUID().toString());
						} else {
							resClassificationModel.setIdentifier(rcvm.getIdentifier());
						}
						resClassificationModel.setResourceId(resourceId);
						resClassificationModel.setTaxoncode(rcvm.getTaxoncode());
						resClassificationModel.setOperation(rcvm.getOperation());
						resultList.add(resClassificationModel);
					}
				}
			} else {
				for (ResClassificationViewModel rcvm : rcvmList) {
					if (StringUtils.isNotEmpty(rcvm.getTaxoncode())) {
						String categoryPattern = null;
						if (rcvm.getTaxonpath() != null) {
							categoryPattern = rcvm.getTaxonpath().split("/")[0];
						}
						resClassificationModel = new ResClassificationModel();
						if (!patchMode) {
							resClassificationModel.setIdentifier(UUID.randomUUID().toString());
						} else {
							resClassificationModel.setIdentifier(rcvm.getIdentifier());
						}
						resClassificationModel.setResourceId(resourceId);
						// 因为此时taxonpath没有任何作用，还会造成taxoncode重复数据，所以将taxonpath置为null.
						// modify by xuzy 20150201
						// resClassificationModel.setTaxonpath(null);

						// 非K12维度模式，需要保存taxOnPath值
						if (categoryPattern != null&& !"K12".equals(categoryPattern)) {
							resClassificationModel.setTaxonpath(rcvm.getTaxonpath());
						}

						resClassificationModel.setTaxoncode(rcvm.getTaxoncode());
						resClassificationModel.setOperation(rcvm.getOperation());
						resultList.add(resClassificationModel);

						if (rcvm.getTaxoncode() != null && rcvm.getTaxoncode().equals(rc.getNdCode())) {
							defaultResCode = true;
						}
					}
				}
			}
		}

		if (!defaultResCode) {
			Assert.assertNotNull(rc);
			ResClassificationModel resClassificationModel = new ResClassificationModel();
			resClassificationModel.setIdentifier(UUID.randomUUID().toString());
			resClassificationModel.setResourceId(resourceId);
			resClassificationModel.setTaxoncode(rc.getNdCode());
			resultList.add(resClassificationModel);
		}

		return resultList;
	}
	
	/**
	 * 用于categories的model转换,list->map
	 * @param list
	 *            需要转换的list
	 * @param resourceType
	 *            资源需求类型
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, List<? extends ResClassificationViewModel>> list2map4Categories(
			List<? extends ResClassificationModel> list, String resourceType) {
		// 旧的维度数据处理
		String[] cc = { "$O", "$S", "$E", "$R" };
		// 根据ndcode排序
		Collections.sort(list);

		Map<String, List<? extends ResClassificationViewModel>> resultMap = new HashMap<String, List<? extends ResClassificationViewModel>>();
		resultMap.put("phase", new ArrayList<ResClassificationViewModel>());
		resultMap.put("grade", new ArrayList<ResClassificationViewModel>());
		resultMap.put("subject", new ArrayList<ResClassificationViewModel>());
		resultMap.put("edition", new ArrayList<ResClassificationViewModel>());
		resultMap.put("sub_edition",new ArrayList<ResClassificationViewModel>());
		if (StringUtils.isNotEmpty(resourceType)) {
			resultMap.put(resourceType,new ArrayList<ResClassificationViewModel>());
		}

		if (CollectionUtils.isNotEmpty(list)) {
			for (ResClassificationModel rcm : list) {
				ResClassificationViewModel resClassificationViewModel = new ResClassificationViewModel();
				resClassificationViewModel.setIdentifier(rcm.getIdentifier());
				resClassificationViewModel.setTaxonpath(rcm.getTaxonpath());
				resClassificationViewModel.setTaxoncode(rcm.getTaxoncode());
				resClassificationViewModel.setTaxonname(rcm.getTaxonname());

				// 获取维度模式
				String categoryPattern = null;
				if (StringUtils.isNotEmpty(rcm.getTaxonpath())) {
					categoryPattern = rcm.getTaxonpath().split("/")[0];
				}

				// 动态生成维度数据列表的key
				if (!ArrayUtils.contains(cc, rcm.getCategoryCode())|| (categoryPattern != null && !categoryPattern.equals("K12"))) {
					String k = rcm.getCategoryName();
					if (k != null) {
						if (resultMap.get(k) == null) {
							List<ResClassificationViewModel> l = new ArrayList<ResClassificationViewModel>();
							l.add(resClassificationViewModel);
							resultMap.put(k, l);
						} else {
							((List) resultMap.get(k))	.add(resClassificationViewModel);
						}
						continue;
					}
				}

				// 确定是放入哪个key中的list
				String key = resourceType;

				if (StringUtils.isEmpty(rcm.getTaxonpath())) {
					key = getResCategoryKey(rcm.getTaxoncode(), resourceType);
				} else {// phase,grade,subject,edition,sub_edition
					List<String> path = Arrays.asList(rcm.getTaxonpath().split("/"));
					int size = path.size();
					if (size > 1 && rcm.getTaxoncode().equals(path.get(1))) {
						key = "phase";
					} else if (size > 2	&& rcm.getTaxoncode().equals(path.get(2))) {
						key = "grade";
					} else if (size > 3&& rcm.getTaxoncode().equals(path.get(3))) {
						key = "subject";
					} else if (size > 4&& rcm.getTaxoncode().equals(path.get(4))) {
						key = "edition";
					} else if (size > 5&& rcm.getTaxoncode().equals(path.get(5))) {
						key = "sub_edition";
					}
				}

				if (resultMap.get(key) != null) {
					((List) resultMap.get(key)).add(resClassificationViewModel);
				}
			}
		}

		// 没有数据不返回
		Iterator<String> iterator = resultMap.keySet().iterator();
		while (iterator.hasNext()) {
			String k = iterator.next();
			if (resultMap.get(k).size() == 0) {
				iterator.remove();
			}
		}

		// 资源类型排序
		if (resultMap.get(resourceType) != null) {
			Collections.sort((List) resultMap.get(resourceType));
		}
		return resultMap;
	}
	
	public static String getResCategoryKey(String taxOnCode, String defaultKey) {
		String key = defaultKey;

		if (StringUtils.isNotEmpty(taxOnCode)) {
			if (taxOnCode.contains("$S")) {
				key = "subject";
			} else if (taxOnCode.contains("$O")) {
				if (!taxOnCode.endsWith("000000")) {
					if (taxOnCode.endsWith("0000")) { // phase
						key = "phase";
					} else {
						key = "grade";
					}
				}
			} else if (taxOnCode.contains("$E")) {
				if (taxOnCode.endsWith("000")) {
					key = "edition";
				} else {
					key = "sub_edition";
				}
			}
		}

		return key;
	}
	
	/**
	 * Coverage批量创建和修改时入参一个OWNER的校验 保证批量传入的覆盖范围,一个资源的覆盖范围，有且仅有一个OWNNER的覆盖范围策略
	 * @param co
	 * @param isCreate
	 */
	public static void checkCoverageHaveOnlyOneOwner(List<?> co,boolean isCreate) {
		Map<String, String> haveOwner = new HashMap<String, String>();
		for (Object object : co) {
			CoverageModelForUpdate cm = null;
			if (isCreate) {
				cm = BeanMapperUtils.beanMapper((ResCoverageModel) object,CoverageModelForUpdate.class);
			} else {
				cm = (CoverageModelForUpdate) object;
			}
			if (cm.getStrategy().equals(CoverageConstant.STRATEGY_OWNER)) {
				if (haveOwner.containsKey(cm.getResource())) {// 该资源已经有OWNER
					LOG.error("入参错误,对同一资源strategy=OWNER的覆盖范围超过两个--一个资源的覆盖范围，有且仅有一个OWNNER的覆盖范围策略");
					throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,"LC/CREATE_COVERAGE_INPUT_ERROR",
							"入参错误,对同一资源strategy=OWNER的覆盖范围超过两个--一个资源的覆盖范围，有且仅有一个OWNNER的覆盖范围策略");
				} else {
					haveOwner.put(cm.getResource(), cm.getStrategy());
				}
			}
		}
	}

	/**
	 * 限制limit分页参数的最大size
	 * 
	 * @param limit
	 * @return
	 */
	public static String checkLimitMaxSize(String limit) {
		Integer[] result = ParamCheckUtil.checkLimit(limit);
		if (result[1] > Constant.MAX_LIMIT) {
			return "(" + result[0] + "," + Constant.MAX_LIMIT + ")";
		}

		return limit;
	}
	
	public static ResourceViewModel changeToView(ResourceModel model,
			String resourceType, List<String> includes,
			CommonServiceHelper commonServiceHelper) {

		return changeToView(model, resourceType, includes, commonServiceHelper,
				null);
	}

	@SuppressWarnings("unchecked")
	public static ResourceViewModel changeToView(ResourceModel model, String resourceType, List<String> includes,
			CommonServiceHelper commonServiceHelper, String terminal) {
		if (model == null) {
			return null;
		}
		ResourceViewModel view = BeanMapperUtils.beanMapper(model,
				commonServiceHelper.getViewClass(resourceType));

		if (model.getLifeCycle() != null && view.getLifeCycle() != null) {
			view.getLifeCycle().setVersion(model.getLifeCycle().getVersion());
		}
		view.setCoverages(null);
		if (model.getCategoryList() != null) {
			view.setCategories(CommonHelper.list2map4Categories(
					model.getCategoryList(),
					changeResourceTypeToCategoryKey(resourceType)));
		}
		if (model.getTechInfoList() != null) {
			view.setTechInfo(CommonHelper.list2Map4TechInfo(
					model.getTechInfoList(), terminal));
		}
		if (StringUtils.isNotEmpty(model.getCustomProperties())) {
			view.setCustomProperties(ObjectUtils.fromJson(
					model.getCustomProperties(), Map.class));
		}

		// 统一处理所有的附加属性：
		if (includes == null) {
			includes = new ArrayList<String>();
		}

		// CG
		if (includes.contains(IncludesConstant.INCLUDE_CG)) {
			if (view.getCategories() == null) {
				view.setCategories(new HashMap<String, List<? extends ResClassificationViewModel>>());
			}
		} else {
			view.setCategories(null);
		}

		// CR
		if (includes.contains(IncludesConstant.INCLUDE_CR)) {
			if (view.getCopyright() == null) {
				view.setCopyright(new ResRightViewModel());
			}
		} else {
			view.setCopyright(null);
		}

		// EDU
		if (includes.contains(IncludesConstant.INCLUDE_EDU)) {
			if (view.getEducationInfo() == null) {
				view.setEducationInfo(new ResEducationalViewModel());
			}
		} else {
			view.setEducationInfo(null);
		}

		// LC
		if (includes.contains(IncludesConstant.INCLUDE_LC)) {
			if (view.getLifeCycle() == null) {
				view.setLifeCycle(new ResLifeCycleViewModel());
			}
		} else {
			view.setLifeCycle(null);
		}

		// TI
		if (includes.contains(IncludesConstant.INCLUDE_TI)) {
			if (view.getTechInfo() == null) {
				view.setTechInfo(new HashMap<String, ResTechInfoViewModel>());
			}
		} else {
			view.setTechInfo(null);
		}

		// ask by cst 2015.11.27 sort preview by key (String asc)
		if (CollectionUtils.isNotEmpty(view.getPreview())) {
			Map<String, String> treeMap = new TreeMap<String, String>(
					RESOURCE_PREIVEW_PREFIX_COMPARATOR);
			treeMap.putAll(view.getPreview());
			view.setPreview(treeMap);
		}

		return view;
	}
	
	/**
	 * @param resourceType
	 * @return
	 * @since
	 * @update by liuwx at 201510.22
	 * @updateContent 从 NDResourceController类迁移过来,解决多处对两种资源类型的判断
	 */
	public static String changeResourceTypeToCategoryKey(String resourceType) {
		if (StringUtils.isEmpty(resourceType)) {
			return resourceType;
		}
		if ("assets".equals(resourceType)) {
			return resourceType + "_type";
		} else {
			return "res_type";
		}

	}
	
	// preview key排序 by liuwx 20151208
	final static Comparator<String> RESOURCE_PREIVEW_PREFIX_COMPARATOR = new Comparator<String>() {
		String searchStr = Constant.RESOURCE_PREIVEW_PREFIX;

		@Override
		public int compare(String key2, String key1) {
			int index = 0;
			if ((index = hasSilde(key1, key2)) > -1) {
				int searchIndex = index + searchStr.length();
				try {
					return Integer
							.valueOf(key2.substring(searchIndex))
							.compareTo(
									Integer.valueOf(key1.substring(searchIndex)));
				} catch (Exception e) {
					return key2.compareTo(key1);
				}

			}
			return key2.compareTo(key1);
		}

		// 设计原则上，使用boolean会更好
		private int hasSilde(String key1, String key2) {
			int index1 = key1.indexOf(searchStr);
			int index2 = key2.indexOf(searchStr);
			return index1 > -1 ? index2 : index1; // 返回index，减少一次查询
			// return index1>-1 &&index2>-1;

		}

	};

	/**
	 * 资源入参通用业务校验
	 * 
	 * @author:yzc
	 * @date:2017年1月18日
	 * @param viewModel
	 * @param checkflag
	 * <p>
	 * 检验标志位，由二进制数字组成，第一位代表UUID校验，第二位代表keywords字符长度校验，第三位代表tags字符长度校验,
	 * 第四位代表techInfo属性校验，第五位代表categories属性校验
	 * </p>
	 * 如只检验UUID，入参为00001
	 */
	public static void ResourceParamValid(ResourceViewModel viewModel,String checkflag, OperationType ot) {
		
		// UUID校验
		if (checkflag == null || (checkflag != null && checkflag.charAt(4) == '1')) {
			if (!checkUuidPattern(viewModel.getIdentifier())) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckIdentifierFail.getCode(),ErrorMessageMapper.CheckIdentifierFail.getMessage());
			}
		}

		// keywords字符长度校验
		if (checkflag == null || (checkflag != null && checkflag.charAt(3) == '1')) {
			if (!checkListLength(viewModel.getKeywords(), 1000)) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckKeywordsFail.getCode(),ErrorMessageMapper.CheckKeywordsFail.getMessage());
			}
		}

		// tags字符长度校验
		if (checkflag == null || (checkflag != null && checkflag.charAt(2) == '1')) {
			if (!checkListLength(viewModel.getTags(), 1000)) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckTagsFail.getCode(),ErrorMessageMapper.CheckTagsFail.getMessage());
			}
		}

		// techInfo属性校验
		if (checkflag == null || (checkflag != null && checkflag.charAt(1) == '1')) {
			Map<String, ? extends ResTechInfoViewModel> techInfoMap = viewModel.getTechInfo();
			if (techInfoMap == null || !techInfoMap.containsKey("href")) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckTechInfoFail);
			}
		}

		// categories属性校验
		if (checkflag == null || (checkflag != null && checkflag.charAt(0) == '1')) {
			Map<String, List<? extends ResClassificationViewModel>> categories = viewModel.getCategories();
			if (categories != null) {
				for (String key : categories.keySet()) {
					List<? extends ResClassificationViewModel> cList = categories.get(key);
					if (cList != null && !cList.isEmpty()) {
						for (ResClassificationViewModel c : cList) {
							if (StringUtils.isNotEmpty(c.getTaxonpath())) {
//								if (!checkCategoryPattern(c.getTaxonpath())) {
//									LOG.error("taxonpath不对，{}",c.getTaxonpath());
//									throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckTaxonpathFail.getCode(),ErrorMessageMapper.CheckTaxonpathFail.getMessage());
//								}
							}
						}
					}
				}
			}
		}

		// 创建时，对覆盖范围的数据单独作校验，不允许有多个OWNER数据
		if (ot == OperationType.CREATE) {
			List<? extends ResCoverageViewModel> coverageList = viewModel.getCoverages();
			if (CollectionUtils.isNotEmpty(coverageList)) {
				int num = 0;
				for (ResCoverageViewModel resCoverageViewModel : coverageList) {
					if (resCoverageViewModel.getStrategy().toUpperCase().equals("OWNER")) {
						num++;
					}
				}
				if (num > 1) {
					throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckCoverageFail);
				}
			}
		}

		// 校验版权的起始与结束时间合法性
		if (viewModel.getCopyright() != null) {
			ResRightViewModel vm = viewModel.getCopyright();
			if (vm.getRightStartDate() != null&& vm.getRightEndDate() != null&& vm.getRightEndDate().longValue() < vm.getRightStartDate().longValue()) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckCopyRightFail);
			}
		}

		// 校验国际化
		checkGlobalField(viewModel);

	}
	
	/**
	 * 将techInfo中的href值或者source值互相拷贝，即如果href或source只有一个有值时将另一个没有值的key，设置为相同的值
	 * 如href有值，source没有值，则将href的值重新赋值给source
	 * 
	 * @param techInfoMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean copyTechInfoValue(Map techInfoMap) {
		boolean flag = false;
		if (techInfoMap != null) {
			if (techInfoMap.containsKey(HREF)&& !techInfoMap.containsKey(SOURCE)) {
				Object ti = techInfoMap.get(HREF);
				techInfoMap.put(SOURCE, ti);
				flag = true;
			} else if (!techInfoMap.containsKey(HREF)&& techInfoMap.containsKey(SOURCE)) {
				ResTechInfoViewModel ti = new ResTechInfoViewModel();
				BeanUtils.copyProperties(techInfoMap.get(SOURCE), ti);
				((ResTechInfoViewModel) ti).setRequirements(null);
				techInfoMap.put(HREF, ti);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 校验UUID
	 * 
	 * @param identifier
	 * @return 检验结果
	 */
	public static boolean checkUuidPattern(String identifier) {
		
		String uuidPattern = "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
		return Pattern.matches(uuidPattern, identifier);
	}
	
	/**
	 * 检验List<String>里面元素的字符长度
	 * 
	 * @param list
	 * @param length
	 * @return 校验结果
	 */
	private static boolean checkListLength(List<String> list, int length) {
		if (list != null && list.size() > 0) {
			String s = StringUtils.join(list, ",");
			if (s.length() >= length) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 校验K12模式是否合法
	 * 
	 * @param taxonPath
	 * @return
	 */
//	public static boolean checkCategoryPattern(String taxonPath) {
//		
//		if (StringUtils.isNotEmpty(taxonPath) && taxonPath.contains(CATEGORY_PATTERN_SEPARTOR)) {
//			String categoryPattern = taxonPath.substring(0,taxonPath.indexOf(CATEGORY_PATTERN_SEPARTOR));
//			String[] segment = null;
//			if (StaticDatas.CATEGORY_PATTERN_MAP.containsKey(categoryPattern)) {
//				CategoryPatternModel cpm = StaticDatas.CATEGORY_PATTERN_MAP.get(categoryPattern);
//				String seg = cpm.getSegment();
//				if (StringUtils.isNotEmpty(seg)) {
//					segment = seg.split(",");
//				}
//			}
//
//			int num = 0;
//			for (int i = 0; i < taxonPath.length(); i++) {
//				if (taxonPath.charAt(i) == '/') {
//					num++;
//				}
//			}
//			if (segment == null) {
//				return true;
//			}
//
//			if (ArrayUtils.contains(segment, String.valueOf(num))) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * 校验多语言字段
	 * @param viewModel
     */
	private static void checkGlobalField(ResourceViewModel viewModel) {
		
		// 校验国际化title
		if (viewModel.getGlobalTitle() != null) {
			viewModel.setGlobalTitle(changeGlobalField(viewModel.getGlobalTitle()));
		}

		// 校验国际化 description
		if (viewModel.getGlobalDescription() != null) {
			viewModel.setGlobalDescription(changeGlobalField(viewModel.getGlobalDescription()));
		}

		// 校验国际化 keywords
		if (viewModel.getGlobalKeywords() != null) {
			viewModel.setGlobalKeywords(changeGlobalField(viewModel.getGlobalKeywords()));
		}

		// 校验国际化 tags
		if (viewModel.getGlobalTags() != null) {
			viewModel.setGlobalTags(changeGlobalField(viewModel.getGlobalTags()));
		}

		// 校验国际化 edu.description
		if (viewModel.getEducationInfo() != null) {
			if (viewModel.getEducationInfo().getGlobalEduDescription() != null) {
				viewModel.getEducationInfo().setGlobalEduDescription(changeGlobalField(viewModel.getEducationInfo().getGlobalEduDescription()));
			}
		}

		// 校验国际化 cr.description
		if (viewModel.getCopyright() != null) {
			if (viewModel.getCopyright().getGlobalCrDescription() != null) {
				viewModel.getCopyright().setGlobalCrDescription(changeGlobalField(viewModel.getCopyright().getGlobalCrDescription()));
			}
		}
		
	}

	/**
	 * 将语言版本转化为标准定义的格式{@link AreaAndLanguage}
	 * @param global
	 * @return
     */
	private static Map<String, String> changeGlobalField(Map<String, String> global) {
		Map<String, String> newGlobal=new HashMap<>();
		for(Map.Entry<String, String> entry:global.entrySet()){
			String language=AreaAndLanguage.validGbCodeType(entry.getKey());
			if (StringUtils.isNotEmpty(language)) {
				newGlobal.put(language, entry.getValue());
			} else {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckGlobalFieldFail);

			}
		}
		return newGlobal;
	}

	/**
	 * 资源入参模型转换
	 * 
	 * @author:yzc
	 * @date:2017年1月18日
	 * @param viewModel
	 * @param t
	 * @author rc
	 */
	public static <T extends ResourceModel> T convertViewModelIn(ResourceViewModel viewModel, Class<T> t, ResourceNdCode rc) {
		
		return convertViewModelIn(viewModel, t, rc, false);
	}

	private static<T extends ResourceModel> T convertViewModelIn(ResourceViewModel viewModel, Class<T> t, ResourceNdCode rc, boolean patchMode) {
		
		T m=null;
		if (viewModel==null) {
			throw new NullPointerException("对象不能为空");
		}
		Assert.assertNotNull(viewModel.getIdentifier());
		// 设置生命周期enable默认为true
		ResLifeCycleViewModel lifeCycleViewModel = viewModel.getLifeCycle();
		if (lifeCycleViewModel != null) {
			lifeCycleViewModel.setEnable(true);
		}

		// 设置关系enable默认为true
		List<? extends ResRelationViewModel> relationViewModels = viewModel.getRelations();
		if (CollectionUtils.isNotEmpty(relationViewModels)) {
			for (ResRelationViewModel relationViewModel : relationViewModels) {
				relationViewModel.setEnable(true);
			}
		}
		m = BeanMapperUtils.beanMapper(viewModel, t);
		
		if (viewModel.getCategories() != null) {
			m.setCategoryList(map2List4Categories(viewModel.getCategories(),viewModel.getIdentifier(), rc, patchMode));
		}
		if (viewModel.getTechInfo() != null) {
			m.setTechInfoList(map2List4TechInfo(viewModel.getTechInfo()));
		}
		if (viewModel.getCustomProperties() != null) {
			m.setCustomProperties(ObjectUtils.toJson(viewModel.getCustomProperties()));
		}

		return m;
	}

	/**
	 * 
	 * 资源出参数据转换model-->viewModel
	 * 
	 * @author yzc
	 * @date:2017年1月20日
	 * @param model
	 * @param t
	 * @param resType  返回的categories中用来存放单个维度数据的key值，默认为res_type         
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ResourceViewModel> T convertViewModelOut(ResourceModel model, Class<T> t, String resType) {
		T v = null;
		if (model != null) {
			v = BeanMapperUtils.beanMapper(model, t);
			if (model.getCategoryList() != null) {
				v.setCategories(list2map4Categories(model.getCategoryList(),StringUtils.isEmpty(resType) ? "res_type": resType));
			}
			if (model.getTechInfoList() != null) {
				v.setTechInfo(list2Map4TechInfo(model.getTechInfoList()));
			}
			if (StringUtils.isNotEmpty(model.getCustomProperties())) {
				v.setCustomProperties(ObjectUtils.fromJson(model.getCustomProperties(), Map.class));
			}
			// 无须返回前台，将relations、coverages赋值为空
			v.setRelations(null);
			v.setCoverages(null);
		}
		return v;
	}
		

	
	
	
}
