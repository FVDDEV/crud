package com.employee.crud.service;

import com.employee.crud.dto.UserDTO;
import com.employee.crud.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService  {

    public List<UserDTO> getUsers();

    UserDTO getUsersById(Integer id);

    void createUser(@Valid UserDTO userDTO);

    UserDTO updateUser(@Valid UserDTO userDTO);

    void deleteUser(Long id);

    Page<User> getUsersPage(PageRequest of);
}
