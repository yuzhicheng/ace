package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.ResCoverage;

public interface ResCoverageRepository extends BaseRepository<ResCoverage>, JpaRepository<ResCoverage, String> {

}
