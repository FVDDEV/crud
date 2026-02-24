//package com.employee.crud.controller;
//
//import com.employee.crud.model.User;
//import com.employee.crud.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @Test
//    void shouldReturnUser() {
//        User user = new User(1L, "John");
//        when(userService.getUserById(1L)).thenReturn(user);
//
//        User result = userController.getUserById(1L);
//
//        assertEquals("John", result.getName());
//    }
//}
