package com.concentrix.demo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.concentrix.demo.exception.TicketNotFoundException;
import com.concentrix.demo.model.Order;
import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.model.User;
import com.concentrix.demo.service.ITicketService;
import com.concentrix.demo.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	
	private static final Logger logger = LogManager.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
    private  ITicketService ticketService;
	
		
	
	
//	 @PostMapping("/orderDetails")
//	    public String processOrderDetails(@RequestParam("quantity") int quantity,
//	                                      HttpSession session,Model model) throws TicketNotFoundException {
//		    int ticketId = (int) session.getAttribute("ticketId");
//	        User user=(User)session.getAttribute("userId");
//	        Ticket ticket = ticketService.getTicketByTicketId(ticketId);
//	        if (quantity <= 0 || quantity > ticket.getTicketsAvailable()) {
//	        	
//	        	logger.warn("Invalid quantity provided for order");
//	            return "redirect:/errorOrderQuantity";
//	            
//	        }
//	        session.setAttribute("ticket", ticket);
//	        ticket.setTicketsAvailable(ticket.getTicketsAvailable()-quantity);
//	        Order order=new Order();
//	        order.setMyTicket(ticket);
//	        order.setMyUser(user);
//	        order.setQuantity(quantity);
//	        orderService.saveOrder(order);
//	        session.setAttribute("order", order);
//	        double totalPrice = ticket.getPrice() * quantity;
//	        session.setAttribute("totalAmount",totalPrice);
//            model.addAttribute("ticket", ticket);
//            model.addAttribute("quantity", quantity);
//            model.addAttribute("order",order);
//            ticket.setTicketsAvailable(ticket.getTicketsAvailable()-quantity);
//            model.addAttribute("totalPrice", totalPrice);
//            return "payment";	          	        
//	    }
	
	
	 @PostMapping("/orderDetails")
	    public String processOrderDetails(@RequestParam("quantity") int quantity,
	                                      HttpSession session, Model model) throws TicketNotFoundException {
	        Ticket ticket = getTicketFromSession(session);
	        User user = getUserFromSession(session);
	        
	        if (!validateQuantity(quantity, ticket)) {
	            logger.warn("Invalid quantity provided for order");
	            return "redirect:/errorOrderQuantity";
	        }
	        
	        Order order = createAndSaveOrder(ticket, user, quantity);
	        setAttributes(session, model, ticket, quantity, order);
	        
	        return "payment";
	    }
	    
	    private Ticket getTicketFromSession(HttpSession session) throws TicketNotFoundException {
	        int ticketId = (int) session.getAttribute("ticketId");
	        return ticketService.getTicketByTicketId(ticketId);
	    }

	    private User getUserFromSession(HttpSession session) {
	        return (User) session.getAttribute("userId");
	    }

	    private boolean validateQuantity(int quantity, Ticket ticket) {
	        return quantity > 0 && quantity <= ticket.getTicketsAvailable();
	    }

	    private Order createAndSaveOrder(Ticket ticket, User user, int quantity) {
	        ticket.setTicketsAvailable(ticket.getTicketsAvailable() - quantity);
	        Order order = new Order();
	        order.setMyTicket(ticket);
	        order.setMyUser(user);
	        order.setQuantity(quantity);
	        orderService.saveOrder(order);
	        return order;
	    }

	    private void setAttributes(HttpSession session, Model model, Ticket ticket, int quantity, Order order) {
	        double totalPrice = ticket.getPrice() * quantity;
	        session.setAttribute("ticket", ticket);
	        session.setAttribute("order", order);
	        session.setAttribute("totalAmount", totalPrice);
	        model.addAttribute("ticket", ticket);
	        model.addAttribute("quantity", quantity);
	        model.addAttribute("order", order);
	        model.addAttribute("totalPrice", totalPrice);
	    }
	 
	 @GetMapping("/errorOrderQuantity")
	 public String showOrder(Model model, HttpSession session) throws TicketNotFoundException {
		  logger.info("Showing Order with Error for Invalid Quantity");
		  model.addAttribute("error", "invalid_quantity entered");
		  Ticket ticket = ticketService.getTicketByTicketId((int) session.getAttribute("ticketId"));
		  model.addAttribute("ticket", ticket);
		  return "orderDetails";
	 }
	 
	 
	 
	 @GetMapping("/allOrders")
	 public String getMyOrders(HttpSession session,Model model,
             @RequestParam(value = "errorOrderId", required = false) Integer errorOrderId,
             @RequestParam(value = "errorMessage", required = false) String errorMessage) {
		 logger.info("Fetching All Orders");
		 User user=(User)session.getAttribute("userId");
		 List<Order> ordersList=orderService.getAll(user.getUserId());
		 model.addAttribute("ordersList",ordersList);
		 if (errorOrderId != null && errorMessage != null) {
		        model.addAttribute("errorOrderId", errorOrderId);
		        model.addAttribute("errorMessage", errorMessage);
		 }
		 return "allOrders";
	 }
}
