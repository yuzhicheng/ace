package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.Category;

public interface CategoryRepository extends BaseRepository<Category>,JpaRepository<Category, String>{

}
