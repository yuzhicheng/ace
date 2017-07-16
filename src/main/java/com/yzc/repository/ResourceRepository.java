package com.yzc.repository;

import org.springframework.data.repository.NoRepositoryBean;

import com.yzc.entity.BaseEntity;

@NoRepositoryBean //指明当前这个接口不是领域类的接口（如StudentRepository）
public interface ResourceRepository<T extends BaseEntity> extends BaseRepository<T>{
	
}
