package com.yzc.mongo.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yzc.mongo.domain.OrderDomain;
import com.yzc.mongo.repository.OrderRepository;
import com.yzc.mongo.service.OrderService;


@Service("OrderService")
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public OrderDomain save(OrderDomain order) {
		
		return orderRepository.save(order);
	}

	@Override
	public List<OrderDomain> getOrderList(String customer) {
		
		return orderRepository.findByCustomer(customer);
	}

	@Override
	public OrderDomain createOrder(OrderDomain order) {
		
		return orderRepository.save(order);
	}

}
