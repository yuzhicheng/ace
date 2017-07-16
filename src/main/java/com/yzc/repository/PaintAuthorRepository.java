package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.PaintAuthor;

public interface PaintAuthorRepository extends BaseRepository<PaintAuthor>, JpaRepository<PaintAuthor, String> {

}
