package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.CategoryData;

public interface CategoryDataRepository extends BaseRepository<CategoryData>,JpaRepository<CategoryData, String>{

}
