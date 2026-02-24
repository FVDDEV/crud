//package com.employee.crud.service.impl;
//
//
//import com.employee.crud.dto.UserDTO;
//import com.employee.crud.entity.Employee;
//import com.employee.crud.entity.User;
//import com.employee.crud.exception.DuplicateUserException;
//import com.employee.crud.exception.UserNotFoundException;
//import com.employee.crud.mapper.UserMapper;
//import com.employee.crud.repository.UserRepository;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.BDDMockito.verify;
//import static org.mockito.BDDMockito.given;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.List;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserMapper userMapper;
//
//    @InjectMocks
//    private UserServiceImpl userServiceImpl;
//
//    @Test
//    void getUsersTest()
//    {
//        User person1 = new User();
//        person1.setUid(1);
//        person1.setUname("falguni_d");
//        person1.setPwd("fd_123");
//        person1.setEmployee(null);
//
//        User person2 = User.builder().uid(person1.getUid()).uname(person1.getUname()).pwd(person1.getPwd()).employee(new Employee()).build();
//
//        given(userRepository.findAll()).willReturn(List.of(person1, person2));
//
//        given(userMapper.mapUserEntityToUserDTO(person1))
//                .willReturn(new UserDTO(1, "falguni_d","fd123",1));
//
//        given(userMapper.mapUserEntityToUserDTO(person2))
//                .willReturn(new UserDTO(2, "vinee_c","vc123",1));
//
//        List<UserDTO> userList = userServiceImpl.getUsers();
//
//        assertThat(userList).isNotNull().hasSize(2);
//
//        verify(userRepository).findAll();
//    }
//
//    @Test
//    void getUsersThrowException_whenNoUsersFound() {
//        given(userRepository.findAll()).willReturn(List.of());
//
//        assertThatThrownBy(() -> userServiceImpl.getUsers()).isInstanceOf(UserNotFoundException.class)
//                .hasMessage("No users found.");
//
//        verify(userRepository).findAll();
//
//        verifyNoInteractions(userMapper);
//    }
//
//    @Test
//    void getUsersByIdTest()
//    {
//        Integer id = 1;
//        User user = new User(1, "falguni_d", "fd123", null);
//        UserDTO userDTO = new UserDTO(1, "falguni_d", "fd123",1);
//
//        given(userRepository.findById(1L)).willReturn(Optional.of(user));
//
//        given(userMapper.mapUserEntityToUserDTO(user)).willReturn(userDTO);
//
//        UserDTO result = userServiceImpl.getUsersById(id);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getUname()).isEqualTo("falguni_d");
//
//        verify(userRepository).findById(1L);
//        verify(userMapper).mapUserEntityToUserDTO(user);
//    }
//
//    @Test
//    void getUserByIDThrowException_whenNotFound()
//    {
//        given(userRepository.findById(1L)).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> userServiceImpl.getUsersById(1))
//                .isInstanceOf(UserNotFoundException.class).hasMessage("User not found.");
//
//
//    }
//
//    @Test
//    void createUserTest()
//    {
//
//        UserDTO dto = new UserDTO(1, "falguni_d", "fd123",1);
//        User mappedUser = new User(1, "falguni_d", "fd123", null);
//
//        given(userRepository.existsByUname("falguni_d")).willReturn(false);
//        given(userMapper.mapUserDTOToUserEntity(dto)).willReturn(mappedUser);
//
//        userServiceImpl.createUser(dto);
//
//        verify(userRepository).existsByUname("falguni_d");
//        verify(userMapper).mapUserDTOToUserEntity(dto);
//        verify(userRepository).save(mappedUser);
//    }
//    @Test
//    void createUserThrowException_whenUsernameExists()
//    {
//
//        UserDTO dto = new UserDTO(1, "falguni", "fd123",1);
//
//        given(userRepository.existsByUname("falguni")).willReturn(true);
//
//        assertThatThrownBy(() -> userServiceImpl.createUser(dto)).isInstanceOf(DuplicateUserException.class)
//                .hasMessage("Username already exist.");
//
//        verify(userRepository).existsByUname("falguni");
//        verifyNoMoreInteractions(userRepository);
//        verifyNoInteractions(userMapper);
//    }
//    @Test
//    void updateUserTest() {
//
//        UserDTO dto = new UserDTO(1, "falguni", "fd123",null);
//        dto.setEmpid(100);
//
//        User existingUser = new User(1, "old", "old", null);
//
//        given(userRepository.findById(1L))
//                .willReturn(Optional.of(existingUser));
//
//        userServiceImpl.updateUser(dto);
//
//        verify(userRepository).findById(1L);
//        verify(userMapper).updateEntityFromDto(dto, existingUser);
//        verify(userRepository).save(existingUser);
//
//        assertThat(existingUser.getEmployee()).isNotNull();
//        assertThat(existingUser.getEmployee().getEid()).isEqualTo(100);
//    }
//    @Test
//    void updateUserThrowException_whenUserNotFound() {
//
//        UserDTO dto = new UserDTO(1, "fvd", "fd123",1);
//
//        given(userRepository.findById(1L))
//                .willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> userServiceImpl.updateUser(dto))
//                .isInstanceOf(UserNotFoundException.class)
//                .hasMessage("User not found");
//
//        verify(userRepository).findById(1L);
//        verifyNoInteractions(userMapper);
//    }
//    @Test
//    void deleteUserTest() {
//
//        given(userRepository.existsById(1L)).willReturn(true);
//
//        userServiceImpl.deleteUser(1L);
//
//        verify(userRepository).deleteById(1L);
//    }
//    @Test
//    void deleteUserThrowException_whenUserNotFound() {
//
//        given(userRepository.existsById(1L)).willReturn(false);
//
//        assertThatThrownBy(() -> userServiceImpl.deleteUser(1L))
//                .isInstanceOf(UserNotFoundException.class).hasMessage("User not found");
//
//        verify(userRepository).existsById(1L);
//        verify(userRepository, never()).deleteById(any());
//    }
//    @Test
//    void getUsersPageTest() {
//
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        Page<User> page = new PageImpl<>(List.of(
//                new User(1, "falguni", "fd123", null)
//        ));
//
//        given(userRepository.findAll(pageRequest)).willReturn(page);
//
//        Page<User> result = userServiceImpl.getUsersPage(pageRequest);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).hasSize(1);
//
//        verify(userRepository).findAll(pageRequest);
//    }
//
//
//}
//
