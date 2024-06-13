package com.concentrix.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.concentrix.demo.model.Order;
import com.concentrix.demo.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
	
	@Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testSaveOrder() {
        Order order = new Order();

        orderService.saveOrder(order);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testGetAll() {
        int userId = 1;
        List<Order> expectedOrders = new ArrayList<>();
        when(orderRepository.getAll(userId)).thenReturn(expectedOrders);
        List<Order> actualOrders = orderService.getAll(userId);

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void testGetOrderByTicketId_OrderExists() {
        int ticketId = 1;
        Order expectedOrder = new Order();
        when(orderRepository.getOrderByTicketId(ticketId)).thenReturn(expectedOrder);
        Order actualOrder = orderService.getOrderByTicketId(ticketId);

        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGetOrderByTicketId_OrderDoesNotExist() {
        int ticketId = 1;
        when(orderRepository.getOrderByTicketId(ticketId)).thenReturn(null);
        Order actualOrder = orderService.getOrderByTicketId(ticketId);

        assertNull(actualOrder);
    }

    @Test
    public void testGetOrderById_OrderExists() {
        int orderId = 1;
        Order expectedOrder = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));
        Order actualOrder = orderService.getOrderById(orderId);

        assertEquals(expectedOrder, actualOrder);
    }

    

}
