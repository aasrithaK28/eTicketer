package com.concentrix.demo.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concentrix.demo.model.Order;
import com.concentrix.demo.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
	private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
	
	@Autowired
    private OrderRepository orderRepository;

	@Override
	public void saveOrder(Order order) {
		
		orderRepository.save(order);
		logger.info("Order saved successfully");
	}

	@Override
	public List<Order> getAll(int userId) {
		logger.info("Retrieving all orders for user with ID: {}", userId);
        List<Order> orders = orderRepository.getAll(userId);
        logger.info("Retrieved {} orders for user with ID: {}", orders.size(), userId);
        return orders;
	}

	@Override
	public Order getOrderByTicketId(int ticketId) {
		logger.info("Retrieving order for ticket with ID: {}", ticketId);
        Order order = orderRepository.getOrderByTicketId(ticketId);
        if (order != null) {
            logger.info("Order found for ticket with ID: {}", ticketId);
        } else {
            logger.warn("No order found for ticket with ID: {}", ticketId);
        }
        return order;
	}
	
	public Order getOrderById(int orderId) {
	    return orderRepository.findById(orderId).orElse(null);
	}

}
