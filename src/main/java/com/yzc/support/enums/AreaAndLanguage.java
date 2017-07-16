package com.yzc.support.enums;

import com.yzc.utils.StringUtils;

/**
 * 地区语言枚举
 * @author xiezy
 * @date 2016年10月17日
 */
public enum AreaAndLanguage {
	ZH_CN("zh-CN","简体中文(中国)"),
	ZH_TW("zh-TW","繁体中文(台湾地区)"),
	ZH_HK("zh-HK","繁体中文(香港)"),
	en_HK("en-HK","英语(香港)"),
	en_US("en-US","英语(美国)"),
	en_GB("en-GB","英语(英国)"),
	en_CA("en-CA","英语(加拿大)"),
	en_AU("en-AU","英语(澳大利亚)"),
	de_DE("de-DE","德语(德国)"),
	fr_FR("fr-FR","法语(法国)"),
	ru_RU("ru-RU","俄语(俄罗斯)"),
	it_IT("it-IT","意大利语(意大利)"),
	ja_JP("ja-JP","日语(日本)"),
	pt_BR("pt-BR","葡萄牙语(巴西)"),
	pt_PT("pt-PT","葡萄牙语(葡萄牙)"),
	en_BZ("en-BZ","英语(伯利兹)"),
	en_CB("en-CB","英语(加勒比海)"),
	en_IE("en-IE","英语(爱尔兰)"),
	en_JM("en-JM","英语(牙买加)"),
	en_NZ("en-NZ","英语(新西兰)"),
	en_PH("en-PH","英语(菲律宾)"),
	en_TT("en-TT","英语(特立尼达)"),
	en_ZA("en-ZA","英语(南非)"),
	en_ZW("en-ZW","英语(津巴布韦)"),
	el_GR("el-GR","希腊语"),
	af_ZA("af-ZA","南非语(南非)"),
	de_AT("de-AT","德语(奥地利)"),
	de_CH("de-CH","德语(瑞士)"),
	de_LU("de-LU","德语(卢森堡)"),
	ar_AE("ar-AE","阿拉伯语(阿联酋)"),
	ar_BH("ar-BH","阿拉伯语(巴林)"),
	ar_DZ("ar-DZ","阿拉伯语(阿尔及利亚)"),
	ar_EG("ar-EG","阿拉伯语(埃及)"),
	ar_IQ("ar-IQ","阿拉伯语(伊拉克)"),
	ar_JO("ar-JO","阿拉伯语(约旦)"),
	ar_KW("ar-KW","阿拉伯语(科威特)"),
	ar_LB("ar-LB","阿拉伯语(黎巴嫩)"),
	ar_LY("ar-LY","阿拉伯语(利比亚)"),
	ar_MA("ar-MA","阿拉伯语(摩洛哥)"),
	ar_OM("ar-OM","阿拉伯语(阿曼)"),
	ar_QA("ar-QA","阿拉伯语(卡塔尔)"),
	ar_SA("ar-SA","阿拉伯语(沙特阿拉伯)"),
	ar_SY("ar-SY","阿拉伯语(叙利亚)"),
	ar_TN("ar-TN","阿拉伯语(突尼斯)"),
	ar_YE("ar-YE","阿拉伯语(也门)"),
	be_BY("be-BY","比利时语(比利时)"),
	cs_CZ("cs-CZ","捷克语(捷克)"),
	cy_CB("cy-CB","威尔士(威尔士)"),
	da_DK("da-DK","丹麦"),
	es_AR("es-AR","西班牙语(阿根廷)"),
	es_BO("es-BO","西班牙语(玻利维亚)"),
	es_CL("es-CL","西班牙语(智利)"),
	es_CO("es-CO","西班牙语(哥伦比亚)"),
	es_DO("es-DO","西班牙语(多米尼加共和国)"),
	es_EC("es-EC","西班牙语(厄瓜多尔)"),
	es_GT("es-GT","西班牙语(危地马拉)"),
	es_HN("es-HN","西班牙语(洪多拉斯)"),
	es_MX("es-MX","西班牙语(墨西哥)"),
	es_NI("es-NI","西班牙语(尼加拉瓜)"),
	es_PA("es-PA","西班牙语(巴拿马)"),
	es_ES("es-ES","西班牙语(国际)"),
	es_PE("es-PE","西班牙语(秘鲁)"),
	es_PR("es-PR","西班牙语(波多黎各(美))"),
	es_PY("es-PY","西班牙语(巴拉圭)"),
	es_SV("es-SV","西班牙语(萨尔瓦多)"),
	es_UY("es-UY","西班牙语(乌拉圭)"),
	es_VE("es-VE","西班牙语(委瑞委拉)"),
	et_EE("et-EE","爱沙尼亚语"),
	eu_ES("eu-ES","巴士克语"),
	fa_IR("fa-IR","法斯语"),
	fi_FI("fi-FI","芬兰语"),
	fo_FO("fo-FO","法罗语"),
	fr_BE("fr-BE","法语(比利时)"),
	fr_CA("fr-CA","法语(加拿大)"),
	fr_CH("fr-CH","法语(瑞士)"),
	fr_LU("fr-LU","法语(卢森堡)"),
	fr_MC("fr-MC","法语(摩纳哥)"),
	gl_ES("gl-ES","加利西亚语"),
	gu_IN("gu-IN","古吉拉特语"),
	he_IL("he-IL","希伯来语"),
	hi_IN("hi-IN","印地语"),
	hr_BA("hr-BA","克罗地亚语(波斯尼亚和黑塞哥维那)"),
	hr_HR("hr-HR","克罗地亚语"),
	hu_HU("hu-HU","匈牙利语"),
	hy_AM("hy-AM","亚美尼亚语"),
	id_ID("id-ID","印度尼西亚语"),
	is_IS("is-IS","冰岛语"),
	it_CH("it-CH","意大利语(瑞士)"),
	ka_GE("ka-GE","格鲁吉亚语"),
	kk_KZ("kk-KZ","哈萨克语"),
	kn_IN("kn-IN","卡纳拉语"),
	ko_KR("ko-KR","朝鲜语"),
	kok_IN("kok-IN","孔卡尼语"),
	ky_KG("ky-KG","吉尔吉斯语(西里尔文)"),
	lt_LT("lt-LT","立陶宛语"),
	lv_LV("lv-LV","拉脱维亚语"),
	mi_NZ("mi-NZ","毛利语"),
	mk_MK("mk-MK","马其顿语"),
	mn_MN("mn-MN","蒙古语"),
	mr_IN("mr-IN","马拉地语"),
	ms_BN("ms-BN","马来语(文莱达鲁萨兰)"),
	ms_MY("ms-MY","马来语(马来西亚)"),
	mt_MT("mt-MT","马耳他语"),
	nb_NO("nb-NO","挪威语(伯克梅尔)(挪威)"),
	nl_BE("nl-BE","荷兰语(比利时)"),
	nl_NL("nl-NL","荷兰语(荷兰)"),
	nn_NO("nn-NO","挪威语(尼诺斯克)(挪威)"),
	ns_ZA("ns-ZA","北梭托语"),
	pa_IN("pa-IN","旁遮普语"),
	pl_PL("pl-PL","波兰语"),
	qu_BO("qu-BO","克丘亚语(玻利维亚)"),
	qu_EC("qu-EC","克丘亚语(厄瓜多尔)"),
	qu_PE("qu-PE","克丘亚语(秘鲁)"),
	ro_RO("ro-RO","罗马尼亚语"),
	;
	
	private AreaAndLanguage(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	private String code;
	private String description;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 校验是否是合法的国际化语言
	 * @author yzc
	 * @date 2016年10月18日
	 * @param gbCode
	 * @return boolean
	 */
	public static String validGbCodeType(String gbCode){
		if (StringUtils.hasText(gbCode)) {
            for(AreaAndLanguage type: AreaAndLanguage.values()){
                if(type.getCode().equalsIgnoreCase(gbCode)){
                    return type.getCode();
                }
            }
        }
		
		return "";
	}
}
