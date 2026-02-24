package com.employee.crud.controller;

import com.employee.crud.dto.UserDTO;
import com.employee.crud.entity.User;
import com.employee.crud.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

  //  @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers()
    {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUsers(@PathVariable("id") Integer id)
    {
        return ResponseEntity.ok(userService.getUsersById(id));
    }

    @GetMapping("/users")
    public Page<User> getUsers(@RequestParam(value = "offset", required = false) Integer offset,
                               @RequestParam(value = "pageSize", required = false) Integer pageSize,
                               @RequestParam(value = "sortBy", required = false) String sortBy) {
        if(null == offset) offset = 0;
        if(null == pageSize) pageSize = 10;
        if(StringUtils.isEmpty(sortBy)) sortBy ="uid";
        return userService.getUsersPage(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
    }


//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO)
//    {
//        userService.createUser(userDTO);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody UserDTO userDTO)
    {
        userService.createUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Integer id, @Valid @RequestBody UserDTO userDTO)
    {
        userDTO.setUid(id);
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
