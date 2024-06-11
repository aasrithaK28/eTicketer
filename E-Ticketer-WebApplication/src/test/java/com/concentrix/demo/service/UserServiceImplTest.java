package com.concentrix.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.concentrix.demo.exception.UserNotFoundException;
import com.concentrix.demo.model.User;
import com.concentrix.demo.repository.UserRepository;
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	
	
	@Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void testFindByUserName_UserExists() throws UserNotFoundException {
        String userName = "testUser";
        User expectedUser = new User();
        expectedUser.setUserName(userName);
        when(userRepository.findByUserName(userName)).thenReturn(expectedUser);
        User actualUser = userService.findByUserName(userName);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testFindByUserName_UserDoesNotExist() {
        String userName = "nonExistingUser";
        when(userRepository.findByUserName(userName)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> {
            userService.findByUserName(userName);
        });
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUserName("testUser");
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

}
