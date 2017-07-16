package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.ResCategory;

public interface ResCategoryRepository extends BaseRepository<ResCategory>,JpaRepository<ResCategory, String>{

}
