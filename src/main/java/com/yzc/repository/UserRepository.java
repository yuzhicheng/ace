package com.yzc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yzc.entity.User;

public interface UserRepository extends BaseRepository<User>,JpaRepository<User, String> {

	User findByUsername(String username);

	User findByUsernameAndPasswordAllIgnoreCase(String username, String password);

	List<User> findDistinctUserByUsernameOrTel(String username, String tel);

	List<User> findByNameOrderByUsernameAsc(String name);

	Page<User> findByName(String name, Pageable pageable);
	
	List<User> findByName(String name, Sort sort);

	@Query(value = "select * from user_inf u where u.cardId like %?1%", nativeQuery = true)
	public User findByCardId(String cardId);

	@Query(value = "select * from user_inf u where u.id=?1", nativeQuery = true)
	public User findById(String card);

	@Query("select u from User u where u.password = :password")
	public List<User> findByPassword(@Param("password") String password);

	// 注意：
	// 1：方法的返回值应该是int，表示更新语句所影响的行数
	// 2：在调用的地方必须加事务，没有事务不能正常执行
	@Modifying
	@Query("update User u set u.count = ?1 where u.count < ?2")
	public int increaseCount(double after, double before);

}
