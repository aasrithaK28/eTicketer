package com.concentrix.demo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.concentrix.demo.model.Ticket;
import com.concentrix.demo.model.User;
import com.concentrix.demo.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import com.concentrix.demo.service.TicketServiceImpl;

@Controller
public class UserController {
	
	private static final Logger logger = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
    private  TicketServiceImpl ticketServiceImpl;
	
	public UserController(UserServiceImpl userServiceImpl) {
		super();
		this.userServiceImpl = userServiceImpl;
	}


	@GetMapping("/")
	public String getIndex() {
		logger.info("Accessing index page.");
		return "index";
	}
	
	@GetMapping("/register")
    public String showRegistrationForm(Model model) {
		logger.info("Displaying registration form.");
        model.addAttribute("user", new User());
        return "register";
    }


	@PostMapping("/register")
    public String registerUser(User user, Model model) {
		
		logger.info("Registering user.");
        if (!user.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$")) {
            logger.error("Invalid password format.");
            model.addAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.");
            return "register";
        }
        userServiceImpl.saveUser(user);
        logger.info("User registered successfully.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


    
//    @PostMapping("/login")
//    public String loginUser(User user, Model model,HttpSession session) {
//    	logger.info("Logging in user.");
//        User existingUser = userServiceImpl.findByUserName(user.getUserName());
//
//        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
//        	session.setAttribute("userId",existingUser);
//        	logger.info("User logged in successfully.");
//            return "redirect:/home";
//        } else {
//        	logger.error("Failed to login. Invalid credentials.");
//            return "login";
//        }
//    }
    
    
    @PostMapping("/login")
    public String loginUser(User user, Model model, HttpSession session) {
        logger.info("Logging in user.");
        User existingUser = userServiceImpl.findByUserName(user.getUserName());

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            session.setAttribute("userId", existingUser);

            List<Ticket> userTickets = ticketServiceImpl.getAll(existingUser.getUserId());
            session.setAttribute("listTickets", userTickets);

            logger.info("User logged in successfully.");
            return "redirect:/home";
        } else {
            logger.error("Failed to login. Invalid credentials.");
            return "login";
        }
    }

    
    @GetMapping("/home1")
    public String showHome1(Model model) {
    	logger.info("Displaying home1 page.");
    	model.addAttribute("listTickets",ticketServiceImpl.getList());
        return "home1";
    }

    @GetMapping("/home")
    public String showHome(Model model) {
    	logger.info("Displaying home page.");
    	model.addAttribute("listTickets",ticketServiceImpl.getList());
    	return "home";
    }
    
    @GetMapping("/help")
    public String showHelp() {
    	return "help";
    }
    
    @GetMapping("/about")
    public String showAboutUs() {
    	return "aboutUs";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        logger.info("User logged out successfully.");
        return "redirect:/home1"; 
    }
    
    
}

