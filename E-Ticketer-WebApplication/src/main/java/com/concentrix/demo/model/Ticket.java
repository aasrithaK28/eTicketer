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
@Table(name = "ticket")
public class Ticket {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "ticketId")
	private int ticketId;
	@Column(name = "eventName")
	private String eventName;
	@Column(name = "description")
	private String description;
	@Column(name = "price")
	private double price;
	@Column(name = "ticketsAvailable")
	private int ticketsAvailable;
	
	@Column(name = "imageUrl")
	private String imageUrl;
	
	@ManyToOne
	@JoinColumn(name="userId",referencedColumnName="userId")
	private User myuser;


	public Ticket() {
		super();
	}


	


	public String getImageUrl() {
		return imageUrl;
	}





	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public Ticket(int ticketId, String eventName, String description, double price, int ticketsAvailable,
			String imageUrl, User myuser) {
		super();
		this.ticketId = ticketId;
		this.eventName = eventName;
		this.description = description;
		this.price = price;
		this.ticketsAvailable = ticketsAvailable;
		this.imageUrl = imageUrl;
		this.myuser = myuser;
	}





	public int getTicketId() {
		return ticketId;
	}


	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}


	public String getEventName() {
		return eventName;
	}


	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getTicketsAvailable() {
		return ticketsAvailable;
	}


	public void setTicketsAvailable(int ticketsAvailable) {
		this.ticketsAvailable = ticketsAvailable;
	}


	public User getMyuser() {
		return myuser;
	}


	public void setMyuser(User myuser) {
		this.myuser = myuser;
	}


	@Override
	public String toString() {
		return "ticketId:" + ticketId + "  ,eventName:" + eventName + "   ,description:" + description ;
	}
	
	
}
