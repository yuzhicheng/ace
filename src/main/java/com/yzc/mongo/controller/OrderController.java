package com.yzc.mongo.controller;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yzc.mongo.entity.Item;
import com.yzc.mongo.entity.Order;
import com.yzc.mongo.service.OrderService;
import com.yzc.support.ValidResultHelper;

@RestController
@RequestMapping("/mongo")
public class OrderController {
		
	@Autowired
	OrderService orderService;

	@RequestMapping(value = { "/", "index" }, method = RequestMethod.GET)
	public Order index() {
		
		Order order = new Order();
		order.setCustomer("gg");
		order.setType("2");
		Collection<Item> itemList=new LinkedHashSet<Item>();
		Item item=new Item();
		item.setIdentifier(UUID.randomUUID().toString());
		item.setPrice(12.5);
		item.setProduct("food");
		item.setQuantity(5);
		itemList.add(item);
		order.setItems(itemList);
		Order o=orderService.save(order);
		return o;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Order createOrder(@Valid @RequestBody Order order,BindingResult validResult) {

		// 校验
		ValidResultHelper.valid(validResult, "CREATE_ORDER_PARAM_VALID_FAIL","OrderController", "createOrder");
		Order o = orderService.createOrder(order);
		return o;
	}

	@RequestMapping(value = "/list/order", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Order> getOrderListByCustomer(@RequestParam String customer) {

		List<Order> comList = orderService.getOrderList(customer);

		return comList;
	}
}

