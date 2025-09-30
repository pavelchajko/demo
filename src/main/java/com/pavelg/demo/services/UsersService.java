package com.pavelg.demo.services;

import com.pavelg.demo.domain.User;
import com.pavelg.demo.dto.UserRequestDto;
import com.pavelg.demo.dto.UserResponseDto;
import com.pavelg.demo.exceptions.InvalidUserRequestException;
import com.pavelg.demo.repositories.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all users from the repository and maps them to UserResponseDto objects.
     *
     * @return A list of UserResponseDto containing full name and email of each user.
     */
    public List<UserResponseDto> getUsers() {
        List<User> users = usersRepository.findAll();
        return users.stream().map(user -> new UserResponseDto(user.getFullName(), user.getEmail())).toList();
    }

    /**
     * Creates a new user with the provided details.
     *
     * @param userRequestDto The DTO containing user details such as full name, email, and Base64 encoded password.
     * @return A UserResponseDto containing the created user's full name and email.
     * @throws InvalidUserRequestException if the password is not properly Base64 encoded or if there is a data integrity violation (e.g., duplicate email).
     */
    public UserResponseDto createUser(@Valid UserRequestDto userRequestDto) {
        String rawPassword;
        try {
            rawPassword = new String(Base64.getDecoder().decode(userRequestDto.getPassword()));
        } catch (IllegalArgumentException e) {
            throw new InvalidUserRequestException("Password must be Base64 encoded", HttpStatus.BAD_REQUEST);
        }

        User user = new User(userRequestDto.getFullName(), userRequestDto.getEmail(), passwordEncoder.encode(rawPassword));
        try{
            usersRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            //we don't want to disclose that the email is already taken
            throw new InvalidUserRequestException("Unable to create user with provided data", HttpStatus.PRECONDITION_FAILED);
        }

        return new UserResponseDto(user.getFullName(),user.getEmail());
    }

    /**
    * Retrieves a user by their unique identifier.
    *
    * @param userId The UUID of the user to retrieve.
    * @return A UserResponseDto containing the user's full name and email.
    * @throws IllegalArgumentException if no user is found with the provided ID.
    */
    public UserResponseDto getUserById(UUID userId) {
        User user = usersRepository.findById(userId).orElseThrow(() -> new InvalidUserRequestException("User with id " + userId + " not found", HttpStatus.NOT_FOUND));
        return new UserResponseDto(user.getFullName(),user.getEmail());
    }
}
