package com.concentrix.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.model.User;
import com.concentrix.demo.service.TicketServiceImpl;
import com.concentrix.demo.service.UserServiceImpl;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private TicketServiceImpl ticketService;

    @Test
    public void testGetIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    
    @Test
    public void testRegisterUser_Success() throws Exception {

    	User user = new User();
        user.setUserName("testUser");
        user.setPassword("ValidPassword@123");
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", user.getUserName())
                .param("password", user.getPassword()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

        verify(userService, times(1)).saveUser(any(User.class));
        
    }



    @Test
    public void testRegisterUser_InvalidPassword() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders.post("/register")               
                .param("userName", "testUser")
                .param("password", "pass"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"));
    }

    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("testPassword");

        when(userService.findByUserName(user.getUserName())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", user.getUserName())
                .param("password", user.getPassword()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void testLoginUser_Failure() throws Exception {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("testPassword");

        when(userService.findByUserName(user.getUserName())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", user.getUserName())
                .param("password", user.getPassword()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testShowHome1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testShowHome() throws Exception {
        User user = new User();
        user.setUserId(1);

        List<Ticket> tickets = new ArrayList<>();
        when(ticketService.getList()).thenReturn(tickets);

        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                .sessionAttr("userId", user))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testShowHelp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/help"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testShowAboutUs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/about"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }


}
