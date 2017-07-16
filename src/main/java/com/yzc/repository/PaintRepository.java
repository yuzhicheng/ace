package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.Paint;

public interface PaintRepository extends BaseRepository<Paint>,
JpaRepository<Paint, String>{

}
