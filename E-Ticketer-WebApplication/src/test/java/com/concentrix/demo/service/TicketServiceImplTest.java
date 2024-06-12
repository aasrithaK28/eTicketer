package com.concentrix.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
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

import com.concentrix.demo.exception.TicketHasOrdersException;
import com.concentrix.demo.exception.TicketNotFoundException;
import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.repository.TicketRepository;
@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {
	
	@Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void testGetAllTickets() {
        List<Ticket> expectedTickets = new ArrayList<>();
        when(ticketRepository.findAll()).thenReturn(expectedTickets);

        List<Ticket> actualTickets = ticketService.getAllTickets();

        assertEquals(expectedTickets, actualTickets);
    }

    @Test
    public void testSaveTicket() {
        Ticket ticket = new Ticket();

        ticketService.saveTicket(ticket);

        verify(ticketRepository, times(1)).save(ticket);
    }
    
    @Test
    public void testGetTicketByTicketId_TicketExists() throws TicketNotFoundException {
        int ticketId = 1;
        Ticket expectedTicket = new Ticket();
        expectedTicket.setTicketId(ticketId);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(expectedTicket));

        Ticket actualTicket = ticketService.getTicketByTicketId(ticketId);

        assertNotNull(actualTicket);
        assertEquals(expectedTicket, actualTicket);
        verify(ticketRepository, times(1)).findById(ticketId);
    }
    
    @Test
    public void testGetTicketByTicketId_TicketNotFound(){
        int ticketId = 1;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        TicketNotFoundException exception = assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketByTicketId(ticketId));
        assertEquals("Ticket not found with id: " + ticketId, exception.getMessage());
        verify(ticketRepository, never()).deleteById(ticketId);
    
    }
    
    @Test
    public void testDeleteTicketByTicketId_TicketIsNull() {
        
        int ticketId = 1;
        when(ticketRepository.findById(ticketId)).thenReturn(null);

        TicketNotFoundException exception = assertThrows(TicketNotFoundException.class, () -> ticketService.deleteTicketByTicketId(ticketId));
        assertEquals("Ticket not found with id: " + ticketId, exception.getMessage());
        verify(ticketRepository, never()).deleteById(ticketId);
    }

    @Test
    public void testDeleteTicketByTicketId_NoOrders_TicketExists() throws TicketNotFoundException, TicketHasOrdersException {
        int ticketId = 1;
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.countOrdersByTicketId(ticketId)).thenReturn(0L);

        assertDoesNotThrow(() -> ticketService.deleteTicketByTicketId(ticketId));

        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @Test
    public void testDeleteTicketByTicketId_WithOrders() {
        int ticketId = 1;
        when(ticketRepository.countOrdersByTicketId(ticketId)).thenReturn(1L);

        assertThrows(TicketHasOrdersException.class, () -> ticketService.deleteTicketByTicketId(ticketId));

    }

    @Test
    public void testGetAll_UserExists() {
        int userId = 1;
        List<Ticket> expectedTickets = new ArrayList<>();
        when(ticketRepository.getAll(userId)).thenReturn(expectedTickets);

        List<Ticket> actualTickets = ticketService.getAll(userId);

        assertEquals(expectedTickets, actualTickets);
    }

    @Test
    public void testGetList() {
        List<Ticket> expectedTickets = new ArrayList<>();
        when(ticketRepository.getList()).thenReturn(expectedTickets);

        List<Ticket> actualTickets = ticketService.getList();

        assertEquals(expectedTickets, actualTickets);
    }

    @Test
    public void testHasOrders() {
        int ticketId = 1;
        when(ticketRepository.countOrdersByTicketId(ticketId)).thenReturn(1L);

        assertTrue(ticketService.hasOrders(ticketId));
    }

}
