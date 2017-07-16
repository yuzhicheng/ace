package com.yzc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.Student;

public interface StudentRepository extends BaseRepository<Student>,
		JpaRepository<Student, String> {

}
