package com.concentrix.demo.service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concentrix.demo.exception.TicketNotFoundException;
import com.concentrix.demo.exception.TicketHasOrdersException;
import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.repository.TicketRepository;

@Service
public class TicketServiceImpl implements ITicketService{
	
	private static final Logger logger = LogManager.getLogger(TicketServiceImpl.class);
	@Autowired
	private TicketRepository ticketRepository;
	@Override
	public List<Ticket> getAllTickets() {
		
		logger.info("Retrieving all tickets");
        List<Ticket> tickets = ticketRepository.findAll();
        logger.info("Retrieved {} tickets", tickets.size());
        return tickets;
	}

	@Override
	public void saveTicket(Ticket ticket) {
	     ticketRepository.save(ticket);
	     logger.info("Ticket saved successfully");
	}

	@Override
	public Ticket getTicketByTicketId(int ticketId) {
		logger.info("Retrieving ticket for id: {}", ticketId);
        Optional<Ticket> optional = ticketRepository.findById(ticketId);
        Ticket ticket = null;
        if (optional.isPresent()) {
            ticket = optional.get();
            logger.info("Ticket found for id: {}", ticketId);
        } else {
            logger.warn("Ticket not found for id: {}", ticketId);
        }
        return ticket;
	}

	@Override
	public void deleteTicketByTicketId(int ticketId) throws TicketNotFoundException, TicketHasOrdersException {
		
		Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + ticketId));
		long orderCount = ticketRepository.countOrdersByTicketId(ticketId);
		
		if (orderCount > 0) {
            throw new TicketHasOrdersException("Cannot delete ticket with id " + ticketId +
                    " because it has associated orders.");
        }
		
		
		this.ticketRepository.deleteById(ticketId);
		logger.info("Ticket with id {} deleted successfully", ticketId);
		
	}

	@Override
	public List<Ticket> getAll(int userId) {

		logger.info("Retrieving all tickets for user with ID: {}", userId);
        List<Ticket> tickets = ticketRepository.getAll(userId);
        logger.info("Retrieved {} tickets for user with ID: {}", tickets.size(), userId);
        return tickets;
	}

	@Override
	public List<Ticket> getList() {	

		logger.info("Retrieving list of tickets");
        List<Ticket> tickets = ticketRepository.getList();
        logger.info("Retrieved {} tickets", tickets.size());
        return tickets;
	}

	@Override
	public boolean hasOrders(int ticketId) {
		return ticketRepository.countOrdersByTicketId(ticketId) > 0;
	}

	
	
	
}

