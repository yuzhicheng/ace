package com.yzc.service;

import java.util.List;

import com.yzc.models.ResCoverageModel;
import com.yzc.vos.CoverageViewModel;

public interface CoverageService {

    /**
     * 批量增加覆盖范围	
     * @param coverageModels        创建时的入参集合
     * @return
     */
    public List<CoverageViewModel> batchCreateCoverage(List<ResCoverageModel> coverageModels,boolean isCreateWithResource);

}
