package com.yzc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yzc.models.ResourceModel;
import com.yzc.vos.ListViewModel;

public interface ResourceService {

	
    /**
     * 资源检索 -- 直接查询数据库,数据可以保证实时性
     * 资源检索升级目的主要是使得查询效率更高，准确度更高。
     * 使得用户可以根据分类维度数据，关系维度数据，覆盖范围，属性，关键字进行分页查询。 
     * 在这个几个条件下，优化数据结构，提高检索效率。
     * @param resType           资源类型
     * @param resCodes         支持多种资源查询,resType=eduresource时生效
     * @param includes    默认情况下，只返回资源的通用属性，不返回资源的其他扩展属性。TI：技术属性, LC：生命周期属性, EDU：教育属性, 
     * CG：分类维度数据属性, CR:版权信息。该检索接口只支持:TI,EDU,LC,CG,CR
     * @param categories      分类维度数据
     * @param categoryExclude    排除的分类维度数据
     * @param relations         关系维度数据
     * @param coverages       覆盖范围，根据目标类型，目标值以及覆盖方式进行查询
     * @param props              属性入参
     * @param words              关键字
     * @param limit                分页参数，第一个值为记录索引参数，第二个值为偏移量
     * @param orders             排序
     * @param reverse            判断关系查询是否反转
     * @param tags                 标签
     * @return
     */
	public ListViewModel<ResourceModel> resourceQueryByDB(String resType, String resCodes, List<String> includes,
			Set<String> categories, Set<String> categoryExclude, List<Map<String, String>> relations,List<String> coverages,
			Map<String, Set<String>> props, Map<String, String> orders, String words, String limit,
			boolean reverse, List<String> tags);

    /**
     * 创建资源
     * @author yzc
     * @param resourceType
     * @param resourceModel
     * @return
     */
    public ResourceModel create(String resourceType, ResourceModel resourceModel);

}
