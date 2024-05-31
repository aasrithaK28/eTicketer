package com.concentrix.demo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.concentrix.demo.exception.TicketHasOrdersException;
import com.concentrix.demo.exception.TicketNotFoundException;
import com.concentrix.demo.model.Order;
import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.model.User;
import com.concentrix.demo.service.OrderServiceImpl;
import com.concentrix.demo.service.TicketServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class TicketController {
    private static final Logger logger = LogManager.getLogger(TicketController.class);

    private final TicketServiceImpl ticketServiceImpl;
    private final OrderServiceImpl orderServiceImpl;

    @Autowired
    public TicketController(TicketServiceImpl ticketServiceImpl, OrderServiceImpl orderServiceImpl) {
        this.ticketServiceImpl = ticketServiceImpl;
        this.orderServiceImpl = orderServiceImpl;
    }

    @GetMapping("/sellTicket")
    public String viewHomePage(Model model, HttpSession session) {
        logger.info("Viewing Sell Ticket Page");
        model.addAttribute("listTickets", ticketServiceImpl.getAll(((User) session.getAttribute("userId")).getUserId()));
        return "sellTicket";
     
        
    }

    @GetMapping("/showNewTicketForm")
    public String showNewTicketForm(Model model, HttpSession session) {
        logger.info("Showing New Ticket Form");
        Ticket ticket = new Ticket();
        ticket.setMyuser(((User) session.getAttribute("userId")));
        model.addAttribute("ticket", ticket);
        return "new_ticket";
    }

    @PostMapping("/saveTicket")
    public String saveTicket(@ModelAttribute("ticket") Ticket ticket) {
        ticketServiceImpl.saveTicket(ticket);
        logger.info("Saved Ticket");
        return "redirect:/sellTicket";
    }

    @GetMapping("/showFormForUpdate/{ticketId}")
    public String showFormForUpdate(@PathVariable(value = "ticketId") int ticketId, Model model, HttpSession session) {
        logger.info("Showing Form for Update");
        Ticket ticket = ticketServiceImpl.getTicketByTicketId(ticketId);
        ticket.setMyuser(((User) session.getAttribute("userId")));
        model.addAttribute("ticket", ticket);
        return "update_ticket";
    }

    @GetMapping("/deleteTicket/{ticketId}")
    public String deleteTicket(@PathVariable(value = "ticketId") int ticketId) throws TicketNotFoundException, TicketHasOrdersException {
        Ticket ticket = ticketServiceImpl.getTicketByTicketId(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket not found with id: " + ticketId);
        }
        if (ticketServiceImpl.hasOrders(ticketId)) {
            throw new TicketHasOrdersException("Ticket with id: " + ticketId + " has orders and cannot be deleted");
        }
        ticketServiceImpl.deleteTicketByTicketId(ticketId);
        return "redirect:/sellTicket";
    }

    @GetMapping("/buy")
    public String showHome(Model model) {
        logger.info("Showing Home Page for Buying Tickets");
        model.addAttribute("listTickets", ticketServiceImpl.getList());
        return "buyTicket";
    }

    @GetMapping("/orderDetails/{ticketId}")
    public String showOrderDetails(@PathVariable("ticketId") int ticketId, Model model, HttpSession session) throws TicketNotFoundException {
        logger.info("Showing Order Details for Ticket with ID: " + ticketId);
        session.setAttribute("ticketId", ticketId);
        Ticket ticket = ticketServiceImpl.getTicketByTicketId(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket not found with id: " + ticketId);
        }
        model.addAttribute("ticket", ticket);
        return "orderDetails";
    }

    @GetMapping("/pay")
    public String showPay() {
        logger.info("Showing Payment Page");
        return "payment";
    }

    

    @PostMapping("/resellTicket")
    public String resellTicket(@RequestParam("orderId") int orderId,@RequestParam("userId") int userId,
                               @RequestParam("resellQuantity") int resellQuantity,
                               HttpSession session, Model model) {
        
        Order order = orderServiceImpl.getOrderById(orderId);
        
        
        if (order == null || resellQuantity <= 0 || resellQuantity > order.getQuantity()) {
            return "redirect:/myOrders"; 
        }
        
        int remainingQuantity = order.getQuantity() - resellQuantity;
        order.setQuantity(remainingQuantity);
        
        orderServiceImpl.saveOrder(order);
        
        Ticket originalTicket = order.getMyTicket();
        User user = (User) session.getAttribute("userId");
        
        Ticket resoldTicket = new Ticket();
     
        resoldTicket.setEventName(originalTicket.getEventName());
        resoldTicket.setDescription(originalTicket.getDescription());
        resoldTicket.setPrice(originalTicket.getPrice());
        resoldTicket.setTicketsAvailable(resellQuantity); 
        resoldTicket.setMyuser(user);
        resoldTicket.setImageUrl(originalTicket.getImageUrl());
        
        ticketServiceImpl.saveTicket(resoldTicket);
        
        
        java.util.List<Ticket> listTickets = (java.util.List<Ticket>) session.getAttribute("listTickets");
        listTickets.add(resoldTicket);
        session.setAttribute("listTickets", listTickets);
        
        
        return "redirect:/sellTicket";
    }



}
