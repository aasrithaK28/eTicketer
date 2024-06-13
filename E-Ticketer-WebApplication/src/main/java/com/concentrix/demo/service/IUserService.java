package com.concentrix.demo.service;

import com.concentrix.demo.exception.UserNotFoundException;
import com.concentrix.demo.model.User;

public interface IUserService {
	 void saveUser(User user);
	 User findByUserName(String userName) throws UserNotFoundException;
}

