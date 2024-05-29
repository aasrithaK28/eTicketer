package com.concentrix.demo.service;

import java.util.List;

import com.concentrix.demo.exception.TicketHasOrdersException;
import com.concentrix.demo.exception.TicketNotFoundException;
import com.concentrix.demo.model.Ticket;



public interface ITicketService {
	List<Ticket> getAllTickets();
	void saveTicket(Ticket ticket);
	Ticket getTicketByTicketId(int ticketId);
	void deleteTicketByTicketId(int ticketId) throws TicketNotFoundException, TicketHasOrdersException;
	List<Ticket> getAll(int userId);
	List<Ticket> getList();
	boolean hasOrders(int ticketId);
}

