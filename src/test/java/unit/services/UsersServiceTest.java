package unit.services;

import com.pavelg.demo.domain.User;
import com.pavelg.demo.dto.UserRequestDto;
import com.pavelg.demo.dto.UserResponseDto;
import com.pavelg.demo.exceptions.InvalidUserRequestException;
import com.pavelg.demo.repositories.UsersRepository;
import com.pavelg.demo.services.UsersService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class UsersServiceTest {
    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    class GetUsers {
        @Test
        void shouldReturnListOfUsers() {
            when(usersRepository.findAll()).thenReturn(List.of(
                    new User("Pavel GIchevski", "pavel.gichevski@gmail.com"),
                    new User("John Doe", "johndoe@gmail.com")));

            List<UserResponseDto> users = usersService.getUsers();
            assertEquals(2, users.size());
            assertEquals("Pavel GIchevski", users.getFirst().getFullName());
            assertEquals("John Doe", users.getLast().getFullName());
        }
    }

    @Nested
    class CreateUser {
        @Test
        void shouldCreateUser() {
            // "password" in Base64
            UserRequestDto userRequestDto = new UserRequestDto("Pavel Gichevski", "pavel.gichevski@gmail.com", "cGFzc3dvcmQ=");
            when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

            UserResponseDto userResponseDto = usersService.createUser(userRequestDto);
            assertEquals("Pavel Gichevski", userResponseDto.getFullName());
            verify(usersRepository).save(argThat(user ->
                    user.getFullName().equals("Pavel Gichevski") &&
                            user.getEmail().equals("pavel.gichevski@gmail.com") &&
                            user.getPasswordHash().equals("hashedPassword")
            ));
        }

        @Test
        void shouldThrowExceptionForInvalidBase64Password() {
            UserRequestDto userRequestDto = new UserRequestDto("Pavel Gichevski", "pavel.gichevski@gmail.com", "invalid_base64");

            InvalidUserRequestException exception = assertThrows(InvalidUserRequestException.class, () -> {
                usersService.createUser(userRequestDto);
            });
            assertEquals("Password must be Base64 encoded", exception.getReason());
        }

        @Test
        void shouldThrowInvalidUserRequestExceptionWhenEmailIsNotUnique() {
            // "password" in Base64
            UserRequestDto userRequestDto = new UserRequestDto("Pavel Gichevski", "pavel.gichevski@gmail.com", "cGFzc3dvcmQ=");

            when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
            when(usersRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                    .thenThrow(new org.springframework.dao.DataIntegrityViolationException("Unique index or primary key violation"));

            InvalidUserRequestException exception = assertThrows(InvalidUserRequestException.class, () -> {
                usersService.createUser(userRequestDto);
            });
            assertEquals("Unable to create user with provided data", exception.getReason());
        }
    }

    @Nested
    class GetUserById {
        @Test
        void shouldReturnUserById() {
            UUID userId = UUID.randomUUID();
            User user = new User("Pavel Gichevski", "pavel.gichevski@gmail.com");
            when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

            UserResponseDto userResponseDto = usersService.getUserById(userId);

            assertEquals("Pavel Gichevski", userResponseDto.getFullName());
            assertEquals("pavel.gichevski@gmail.com", userResponseDto.getEmail());
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            UUID userId = UUID.randomUUID();
            when(usersRepository.findById(userId)).thenReturn(Optional.empty());

            InvalidUserRequestException exception = assertThrows(InvalidUserRequestException.class, () -> {
                usersService.getUserById(userId);
            });
            assertEquals("User with id " + userId + " not found", exception.getReason());
        }
    }
}
