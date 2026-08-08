package com.spig.spig.global.init;

import com.spig.spig.domain.user.entity.User;
import com.spig.spig.domain.user.entity.UserRole;
import com.spig.spig.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        User user1 = User.builder()
                .loginId("user1@1234")
                .name("유저1")
                .password(passwordEncoder.encode("1234"))
                .role(UserRole.USER)
                .build();

        User user2 = User.builder()
                .loginId("user2@1234")
                .name("유저2")
                .password(passwordEncoder.encode("1234"))
                .role(UserRole.USER)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
    }
}
