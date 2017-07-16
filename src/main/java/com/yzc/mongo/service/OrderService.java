package com.yzc.mongo.service;

import java.util.List;

import com.yzc.mongo.entity.Order;

public interface OrderService {

	public Order save(Order order);

	public List<Order> getOrderList(String customer);

	public Order createOrder(Order order);

}
