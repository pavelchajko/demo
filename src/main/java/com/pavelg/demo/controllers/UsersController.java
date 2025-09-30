package com.pavelg.demo.controllers;

import com.pavelg.demo.dto.UserRequestDto;
import com.pavelg.demo.dto.UserResponseDto;
import com.pavelg.demo.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(UsersController.USERS_BASE_URL)
public class UsersController {
    public static final String USERS_BASE_URL = "/api/v1/users";

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Handles GET requests to retrieve all users.
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> userDtos = usersService.getUsers();
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    /**
     * Handles POST requests to create a new user.
     * @param userRequestDto The DTO containing user details.
     * @return A ResponseEntity containing the created user's details and HTTP status.
     */
    @PostMapping("register")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = usersService.createUser(userRequestDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    /**
     * Handles GET requests to retrieve a user by their ID.
     * @param userId The UUID of the user to retrieve.
     * @return A ResponseEntity containing the user's details and HTTP status.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID userId) {
        UserResponseDto userResponseDto = usersService.getUserById(userId);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

}
