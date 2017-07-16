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
import com.yzc.mongo.domain.OrderDomain;
import com.yzc.mongo.service.OrderService;
import com.yzc.support.ValidResultHelper;

@RestController
@RequestMapping("/mongo")
public class OrderController {
		
	@Autowired
	OrderService orderService;

	@RequestMapping(value = { "/", "index" }, method = RequestMethod.GET)
	public OrderDomain index() {
		
		OrderDomain order = new OrderDomain();
		order.setCustomer("gg");
		order.setType("2");
		Collection<Item> itemList=new LinkedHashSet<>();
		Item item=new Item();
		item.setIdentifier(UUID.randomUUID().toString());
		item.setPrice(12.5);
		item.setProduct("food");
		item.setQuantity(5);
		itemList.add(item);
		order.setItems(itemList);
		OrderDomain o=orderService.save(order);
		return o;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public OrderDomain createOrder(@Valid @RequestBody OrderDomain order, BindingResult validResult) {

		// 校验
		ValidResultHelper.valid(validResult, "CREATE_ORDER_PARAM_VALID_FAIL","OrderController", "createOrder");
		OrderDomain o = orderService.createOrder(order);
		return o;
	}
	
	@RequestMapping(value = "/list/order", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<OrderDomain> getOrderListByCustomer(@RequestParam String customer) {

		List<OrderDomain> comList = orderService.getOrderList(customer);

		return comList;
	}
}

