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
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        if (userRepository.findByLoginId("admin").isPresent()) {
            return;
        }

        User admin = User.builder()
                .loginId("admin")
                .name("관리자")
                .password(passwordEncoder.encode("admin1234"))
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(admin);
    }
}
