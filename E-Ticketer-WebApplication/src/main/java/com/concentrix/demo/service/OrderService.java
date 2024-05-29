package com.concentrix.demo.service;

import java.util.List;

import com.concentrix.demo.model.Order;


public interface OrderService {
	public void saveOrder(Order order);
	public List<Order> getAll(int userId);
	public Order getOrderByTicketId(int ticketId);
}
