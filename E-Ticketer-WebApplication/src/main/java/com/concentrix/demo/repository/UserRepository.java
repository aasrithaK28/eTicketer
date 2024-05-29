package com.concentrix.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concentrix.demo.model.User;
@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
	User findByUserName(String userName);
}

