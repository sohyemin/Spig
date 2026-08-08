package com.spig.spig.user;

import com.spig.spig.domain.user.dto.LoginRequestDto;
import com.spig.spig.domain.user.dto.LoginResponseDto;
import com.spig.spig.domain.user.entity.User;
import com.spig.spig.domain.user.entity.UserRole;
import com.spig.spig.domain.user.repository.UserRepository;
import com.spig.spig.domain.user.service.UserServiceImpl;
import com.spig.spig.global.exception.CustomException;
import com.spig.spig.global.security.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static com.spig.spig.global.exception.ErrorCode.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;
    private LoginRequestDto request;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                tokenService,
                passwordEncoder
        );

        request = new LoginRequestDto(
                "spig-user",
                "raw-password"
        );
    }

    @Test
    @DisplayName("아이디와 비밀번호가 일치하면 Access Token 응답을 반환한다")
    void loginSuccess() {
        User user = createUser();
        LoginResponseDto expectedResponse =
                LoginResponseDto.to("access-token", UserRole.USER);

        when(userRepository.findByLoginId("spig-user"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                "raw-password",
                "encoded-password"
        )).thenReturn(true);
        when(tokenService.issueTokens(user))
                .thenReturn(expectedResponse);

        LoginResponseDto response = userService.login(request);

        assertSame(expectedResponse, response);
        verify(tokenService).issueTokens(user);
    }

    @Test
    @DisplayName("존재하지 않는 로그인 ID이면 토큰을 발급하지 않는다")
    void rejectUnknownUser() {
        when(userRepository.findByLoginId("spig-user"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.login(request)
        );

        assertEquals(
                "spig-user 유저가 존재하지 않습니다.",
                exception.getMessage()
        );
        verify(tokenService, never()).issueTokens(
                org.mockito.ArgumentMatchers.any(User.class)
        );
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 토큰을 발급하지 않는다")
    void rejectWrongPassword() {
        User user = createUser();

        when(userRepository.findByLoginId("spig-user"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                "raw-password",
                "encoded-password"
        )).thenReturn(false);

        CustomException exception = assertThrows(
                CustomException.class,
                () -> userService.login(request)
        );

        assertEquals(USER_NOT_FOUND, exception.getErrorCode());
        verify(tokenService, never()).issueTokens(user);
    }

    private User createUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .loginId("spig-user")
                .password("encoded-password")
                .name("테스트 사용자")
                .role(UserRole.USER)
                .build();
    }
}
