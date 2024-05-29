package com.concentrix.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concentrix.demo.model.User;
import com.concentrix.demo.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Service
public class UserServiceImpl implements IUserService{
	
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByUserName(String userName) {
		logger.info("Finding user by username: {}", userName);
        User user = userRepository.findByUserName(userName);
        if (user != null) {
            logger.info("User found with username: {}", userName);
        } else {
            logger.warn("User not found with username: {}", userName);
        }
        return user;
	}

	public void saveUser(User user) {
		logger.info("Saving user: {}", user);
		userRepository.save(user);
		logger.info("User saved successfully");
	}

	
}

