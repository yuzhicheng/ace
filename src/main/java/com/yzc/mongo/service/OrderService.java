package com.yzc.mongo.service;

import java.util.List;

import com.yzc.mongo.domain.OrderDomain;

public interface OrderService {

	public OrderDomain save(OrderDomain order);

	public List<OrderDomain> getOrderList(String customer);

	public OrderDomain createOrder(OrderDomain order);

}
