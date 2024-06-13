package com.concentrix.demo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import com.concentrix.demo.model.Order;
import com.concentrix.demo.model.Ticket;
//import com.concentrix.demo.model.User;
//import com.concentrix.demo.service.TicketServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	
	private static final Logger logger = LogManager.getLogger(PaymentController.class);
	
    @PostMapping("/processPayment")
    public String processPayment(Model model,HttpSession session) {
    	Order order1=(Order)session.getAttribute("order");
    	Ticket ticket=(Ticket)session.getAttribute("ticket");
    	logger.info("Processing Payment for Order: " + order1.getOrderId() + ", Ticket: " + ticket.getTicketId());
        model.addAttribute("ticket",ticket);
        model.addAttribute("order",order1);
        return "processPayment";
    }
	
	

    


}
