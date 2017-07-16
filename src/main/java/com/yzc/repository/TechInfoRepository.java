package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.TechInfo;

public interface TechInfoRepository extends BaseRepository<TechInfo>, JpaRepository<TechInfo, String> {

}
