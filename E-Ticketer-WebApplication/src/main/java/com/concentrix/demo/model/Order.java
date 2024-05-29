package com.concentrix.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "orderId")
	private int orderId;
	
	@ManyToOne
	@JoinColumn(name="user_id",referencedColumnName="userId")
	private User myUser;
	
	@ManyToOne
	@JoinColumn(name="ticket_id",referencedColumnName="ticketId")
	private Ticket myTicket;
	
	@Column(name="quantity")
	private int quantity;

	public Order() {
		super();
	}

	public Order(int orderId, User myUser, Ticket myTicket, int quantity) {
		super();
		this.orderId = orderId;
		this.myUser = myUser;
		this.myTicket = myTicket;
		this.quantity = quantity;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public User getMyUser() {
		return myUser;
	}

	public void setMyUser(User myUser) {
		this.myUser = myUser;
	}

	public Ticket getMyTicket() {
		return myTicket;
	}

	public void setMyTicket(Ticket myTicket) {
		this.myTicket = myTicket;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", myUser=" + myUser + ", myTicket=" + myTicket + ", quantity=" + quantity
				+ "]";
	}

	
	
}
