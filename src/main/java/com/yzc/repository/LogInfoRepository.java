package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.LogInfo;

public interface LogInfoRepository extends BaseRepository<LogInfo>,
JpaRepository<LogInfo, String>{

}
