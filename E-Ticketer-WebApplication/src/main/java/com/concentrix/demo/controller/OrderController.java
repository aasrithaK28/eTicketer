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

import com.concentrix.demo.model.Order;
import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.model.User;

import com.concentrix.demo.service.OrderServiceImpl;
import com.concentrix.demo.service.TicketServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	
	private static final Logger logger = LogManager.getLogger(OrderController.class);
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	@Autowired
    private  TicketServiceImpl ticketServiceImpl;
	
	public OrderController(OrderServiceImpl orderServiceImpl) {
		super();
		this.orderServiceImpl = orderServiceImpl;
	}
		
	
	
	 @PostMapping("/orderDetails")
	    public String processOrderDetails(@RequestParam("quantity") int quantity,
	                                      HttpSession session,Model model) {
		    int ticketId = (int) session.getAttribute("ticketId");
	        User user=(User)session.getAttribute("userId");
	        Ticket ticket = ticketServiceImpl.getTicketByTicketId(ticketId);
	        if (quantity <= 0 || quantity > ticket.getTicketsAvailable()) {
	        	
	        	logger.warn("Invalid quantity provided for order");
	            return "redirect:/errorOrderQuantity";
	            
	        }
	        session.setAttribute("ticket", ticket);
	        ticket.setTicketsAvailable(ticket.getTicketsAvailable()-quantity);
	        Order order=new Order();
	        order.setMyTicket(ticket);
	        order.setMyUser(user);
	        order.setQuantity(quantity);
	        orderServiceImpl.saveOrder(order);
	        session.setAttribute("order", order);
	        double totalPrice = ticket.getPrice() * quantity;
	        session.setAttribute("totalAmount",totalPrice);
            model.addAttribute("ticket", ticket);
            model.addAttribute("quantity", quantity);
            model.addAttribute("order",order);
            ticket.setTicketsAvailable(ticket.getTicketsAvailable()-quantity);
            model.addAttribute("totalPrice", totalPrice);
            return "payment";	          	        
	    }
	

	 
	 @GetMapping("/errorOrderQuantity")
	 public String showOrder(Model model, HttpSession session) {
		  logger.info("Showing Order with Error for Invalid Quantity");
		  model.addAttribute("error", "invalid_quantity");
		  Ticket ticket = ticketServiceImpl.getTicketByTicketId((int) session.getAttribute("ticketId"));
		  model.addAttribute("ticket", ticket);
		  return "orderDetails";
	 }
	 
	 @GetMapping("/allOrders")
	 public String getMyOrders(HttpSession session,Model model) {
		 logger.info("Fetching All Orders");
		 User user=(User)session.getAttribute("userId");
		 List<Order> ordersList=orderServiceImpl.getAll(user.getUserId());
		 model.addAttribute("ordersList",ordersList);		 
		 return "allOrders";
	 }
}
