package com.concentrix.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.concentrix.demo.model.Order;
import com.concentrix.demo.model.Ticket;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Test
    public void testProcessPayment() throws Exception {
        MockHttpSession session = new MockHttpSession();
        
        Order order = new Order();
        order.setOrderId(1);
        
        Ticket ticket = new Ticket();
        ticket.setTicketId(1);
        
        session.setAttribute("order", order);
        session.setAttribute("ticket", ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/processPayment")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ticket"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("ticket", ticket))
                .andExpect(model().attribute("order", order));
    }
    
}
