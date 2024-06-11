package com.concentrix.demo.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ExceptionControllerAdviceTest {

	private final ExceptionControllerAdvice exceptionControllerAdvice = new ExceptionControllerAdvice();

    @Test
    void testTicketNotFoundExceptionHandling() throws Exception {
        TicketNotFoundException ex = new TicketNotFoundException("Ticket not found");
        ResponseEntity<Object> responseEntity = exceptionControllerAdvice.ticketNotFoundExceptionHandling(ex);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ticket not found");
    }

    @Test
    void testUserNotFoundExceptionHandling() throws Exception {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ResponseEntity<Object> responseEntity = exceptionControllerAdvice.userNotFoundExceptionHandling(ex);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("User not found");
    }

    @Test
    void testTicketHasOrdersExceptionHandling() throws Exception {
        TicketHasOrdersException ex = new TicketHasOrdersException("Ticket has orders");
        ResponseEntity<Object> responseEntity = exceptionControllerAdvice.ticketHasOrdersExceptionHandling(ex);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ticket has orders");
    }
}
