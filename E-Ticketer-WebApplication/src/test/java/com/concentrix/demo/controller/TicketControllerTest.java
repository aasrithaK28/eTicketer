package com.concentrix.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.concentrix.demo.exception.TicketHasOrdersException;
import com.concentrix.demo.exception.TicketNotFoundException;
import com.concentrix.demo.model.Order;
import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.model.User;
import com.concentrix.demo.service.ITicketService;
import com.concentrix.demo.service.OrderService;

import jakarta.servlet.http.HttpSession;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITicketService ticketService;

    @MockBean
    private OrderService orderService;
    
    @Mock
    private HttpSession session;

    @Test
    public void testViewHomePage() throws Exception {
        User user = new User();
        user.setUserId(1);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", user);

        when(ticketService.getAll(user.getUserId())).thenReturn(tickets);

        mockMvc.perform(MockMvcRequestBuilders.get("/sellTicket")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("listTickets"));
    }

    @Test
    public void testShowNewTicketForm() throws Exception {
        User user = new User();
        user.setUserId(1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", user);

        mockMvc.perform(MockMvcRequestBuilders.get("/showNewTicketForm")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("ticket"));
    }

    @Test
    public void testSaveTicket() throws Exception {
        Ticket ticket = new Ticket();

        mockMvc.perform(MockMvcRequestBuilders.post("/saveTicket")
                .flashAttr("ticket", ticket))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/sellTicket"));

        verify(ticketService, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void testShowFormForUpdate() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setTicketId(1);
        User user = new User();
        user.setUserId(1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", user);

        when(ticketService.getTicketByTicketId(1)).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.get("/showFormForUpdate/1")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("ticket"));
    }

    @Test
    public void testDeleteTicket_Success() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setTicketId(1);

        when(ticketService.getTicketByTicketId(1)).thenReturn(ticket);
        when(ticketService.hasOrders(1)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/deleteTicket/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/sellTicket"));

        verify(ticketService, times(1)).deleteTicketByTicketId(1);
    }
    
    @Test
    public void testDeleteTicket_TicketNotFound() throws Exception {
        when(ticketService.getTicketByTicketId(1)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/deleteTicket/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TicketNotFoundException))
                .andExpect(result -> assertEquals("Ticket not found with id: 1", result.getResolvedException().getMessage()));

        verify(ticketService, times(0)).deleteTicketByTicketId(1); 
    }
    
    
    @Test
    public void testDeleteTicket_TicketHasOrders() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setTicketId(1);

        when(ticketService.getTicketByTicketId(1)).thenReturn(ticket);
        when(ticketService.hasOrders(1)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/deleteTicket/1"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TicketHasOrdersException))
                .andExpect(result -> assertEquals("Ticket with id: 1 has orders and cannot be deleted", result.getResolvedException().getMessage()));

        verify(ticketService, times(0)).deleteTicketByTicketId(1); 
    }

    @Test
    public void testShowHome() throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());

        when(ticketService.getList()).thenReturn(tickets);

        mockMvc.perform(MockMvcRequestBuilders.get("/buy"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("tickets"));
    }

    @Test
    public void testShowOrderDetails_Success() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setTicketId(1);

        MockHttpSession session = new MockHttpSession();

        when(ticketService.getTicketByTicketId(1)).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.get("/orderDetails/1")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("ticket"));

        verify(ticketService, times(1)).getTicketByTicketId(1);
    }
    
    @Test
    public void testShowOrderDetails_TicketNotFound() throws Exception {
        MockHttpSession session = new MockHttpSession();

        when(ticketService.getTicketByTicketId(1)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/orderDetails/1")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TicketNotFoundException))
                .andExpect(result -> assertEquals("Ticket not found with id: 1", result.getResolvedException().getMessage()));

        verify(ticketService, times(1)).getTicketByTicketId(1);
    }


    @Test
    public void testShowPay() throws Exception {
        
    	MockHttpSession session = new MockHttpSession();
        
        Order order = new Order();
        Ticket ticket = new Ticket();
        int quantity = 2;
        double totalPrice = 100.0;

        session.setAttribute("order", order);
        session.setAttribute("ticket", ticket);
        session.setAttribute("quantity", quantity);
        session.setAttribute("totalAmount", totalPrice);

        mockMvc.perform(get("/pay").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("payment"))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attribute("ticket", ticket))
                .andExpect(model().attribute("quantity", quantity))
                .andExpect(model().attribute("totalPrice", totalPrice));

    }
    
    
    @Test
    public void testResellTicket_Success() throws Exception {
        
        Order order = new Order();
        order.setOrderId(1);
        order.setQuantity(10);

        Ticket originalTicket = new Ticket();
        originalTicket.setEventName("Test Event");
        originalTicket.setDescription("Test Description");
        originalTicket.setPrice(100.0);
        originalTicket.setImageUrl("testImageUrl");

        order.setMyTicket(originalTicket);

        User user = new User();
        user.setUserId(1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", user);

        
        when(orderService.getOrderById(1)).thenReturn(order);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/resellTicket")
                .param("orderId", "1")
                .param("userId", "1")
                .param("resellQuantity", "5")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/sellTicket"));      
    }
    
    @Test
    public void testResellTicket_OrderNotFound() throws Exception {
        when(orderService.getOrderById(1)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/resellTicket")
                .param("orderId", "1")
                .param("userId", "1")
                .param("resellQuantity", "5")
                .session(new MockHttpSession()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }
    
    @Test
    public void testResellTicket_InvalidResellQuantity_Zero() throws Exception {
        Order order = new Order();
        order.setOrderId(1);
        order.setQuantity(10);

        when(orderService.getOrderById(1)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/resellTicket")
                .param("orderId", "1")
                .param("userId", "1")
                .param("resellQuantity", "0")
                .session(new MockHttpSession()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
                
 
    }

    
    @Test
    public void testResellTicket_InvalidResellQuantity_High() throws Exception {
        Order order = new Order();
        order.setOrderId(1);
        order.setQuantity(10);

        when(orderService.getOrderById(1)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/resellTicket")
                .param("orderId", "1")
                .param("userId", "1")
                .param("resellQuantity", "15")
                .session(new MockHttpSession()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
                

        
    }

}
