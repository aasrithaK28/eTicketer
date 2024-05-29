package com.concentrix.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.concentrix.demo.model.Ticket;
@Repository
public interface TicketRepository extends JpaRepository<Ticket,Integer>{
	@Query(value = "select * from ticket  where user_id = ?",nativeQuery=true)
	List<Ticket> getAll(int userId);
	
	@Query(value = "select * from ticket where tickets_available > 0",nativeQuery=true)
	List<Ticket> getList();
	
	
	@Query(value="select count(*) from orders where ticket_id=?",nativeQuery=true)
	long countOrdersByTicketId(int ticketId);
}

