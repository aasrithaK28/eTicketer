package com.concentrix.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.concentrix.demo.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>{
	@Query(value = "select * from orders  where user_id = ?",nativeQuery=true)
	List<Order> getAll(int userId);
	@Query(value="select * from orders where ticket_id=?",nativeQuery=true)
	Order getOrderByTicketId(int ticketId);
	
}
