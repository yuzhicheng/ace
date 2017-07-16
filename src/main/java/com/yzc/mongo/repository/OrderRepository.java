package com.yzc.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.yzc.mongo.domain.OrderDomain;

public interface OrderRepository extends MongoRepository<OrderDomain, String>{
             
	List<OrderDomain> findByCustomer(String c);

	List<OrderDomain> findByCustomerLike(String c);

	List<OrderDomain> findByCustomerAndType(String c, String t);

	List<OrderDomain> findByCustomerLikeAndType(String c, String t);
	
	@Query("{'customer':'Chuck Wagon','type':?0}")
	List<OrderDomain> findChucksOrders(String t);
}
