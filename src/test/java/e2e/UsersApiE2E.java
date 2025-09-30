package e2e;

import com.pavelg.demo.dto.UserRequestDto;
import com.pavelg.demo.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UsersApiE2E {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createUser() {
        UserRequestDto userRequestDto = new UserRequestDto("Pavel Gichevski", "pavel.gichevski@gmail.com", "cGFzc3dvcmQ=");
        ResponseEntity<UserResponseDto> response = restTemplate.postForEntity("/api/v1/users/register", userRequestDto, UserResponseDto.class);

        assert(response.getStatusCode().is2xxSuccessful());
        UserResponseDto userResponseDto = response.getBody();
        assert(userResponseDto != null);
        assert(userResponseDto.getFullName().equals("Pavel Gichevski"));
        assert(userResponseDto.getEmail().equals("pavel.gichevski@gmail.com"));
    }
}
