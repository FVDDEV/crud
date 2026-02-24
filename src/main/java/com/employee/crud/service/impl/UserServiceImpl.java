package com.employee.crud.service.impl;

import com.employee.crud.dto.UserDTO;
import com.employee.crud.entity.Employee;
import com.employee.crud.entity.User;
import com.employee.crud.exception.DuplicateUserException;
import com.employee.crud.exception.UserNotFoundException;
import com.employee.crud.mapper.UserMapper;
import com.employee.crud.repository.UserRepository;
import com.employee.crud.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty())
        {
            log.error("No user is found :: using logger");
            throw new UserNotFoundException("No users found.");
        }
        return users.stream().map(userMapper::mapUserEntityToUserDTO).toList();
    }

    @Override
    public UserDTO getUsersById(Integer id) {

        User existingUser = userRepository.findById(Long.valueOf(id)).orElseThrow(()->new UserNotFoundException("User not found."));
        return userMapper.mapUserEntityToUserDTO(existingUser);
    }

    public void createUser(UserDTO userDTO)
    {
        if(userRepository.existsByUname(userDTO.getUname()))
        {
            throw new DuplicateUserException("Username already exist.");
        }
        userRepository.save(userMapper.mapUserDTOToUserEntity(userDTO,passwordEncoder));
    }

    public UserDTO updateUser(UserDTO userDTO)
    {
        User existingUser = userRepository.findById(Long.valueOf(userDTO.getUid()))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userMapper.updateEntityFromDto(userDTO, existingUser);

        Employee employee = new Employee();
        employee.setEid(userDTO.getEmpid());
        existingUser.setEmployee(employee);

        userRepository.save(existingUser);
            return userDTO;
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> getUsersPage(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

}
