package com.concentrix.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.concentrix.demo.model.Order;
import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.model.User;
import com.concentrix.demo.service.OrderServiceImpl;
import com.concentrix.demo.service.TicketServiceImpl;
import static org.mockito.ArgumentMatchers.anyInt;


@SpringBootTest
@AutoConfigureMockMvc

class OrderControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;

    @MockBean
    private TicketServiceImpl ticketService;

    @Test
    public void testProcessOrderDetails_Success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", new User());
        session.setAttribute("ticketId", 1);

        Ticket ticket = new Ticket();
        ticket.setTicketsAvailable(10);
        when(ticketService.getTicketByTicketId(1)).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/orderDetails")
                .session(session)
                .param("quantity", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testProcessOrderDetails_InvalidQuantity() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", new User());
        session.setAttribute("ticketId", 1);

        Ticket ticket = new Ticket();
        ticket.setTicketsAvailable(10);
        when(ticketService.getTicketByTicketId(1)).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/orderDetails")
                .session(session)
                .param("quantity", "15"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void testShowOrder() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("ticketId", 1);

        Ticket ticket = new Ticket();
        when(ticketService.getTicketByTicketId(1)).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.get("/errorOrderQuantity")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetMyOrders() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", new User());

        List<Order> ordersList = new ArrayList<>();
        when(orderService.getAll(anyInt())).thenReturn(ordersList);

        mockMvc.perform(MockMvcRequestBuilders.get("/allOrders")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
}
