package com.spig.spig.domain.user.service;

import com.spig.spig.domain.user.dto.JoinRequestDto;
import com.spig.spig.domain.user.dto.LoginRequestDto;
import com.spig.spig.domain.user.dto.LoginResponseDto;
import com.spig.spig.domain.user.entity.User;
import com.spig.spig.domain.user.repository.UserRepository;
import com.spig.spig.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.spig.spig.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(JoinRequestDto dto) {
        userRepository.findByLoginId(dto.getLogin_id())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 유저입니다.");
                });

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        //회원 저장
        userRepository.save(User.from(dto));
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        User user = userRepository.findByLoginId(dto.getLogin_id())
                .orElseThrow(() -> new UsernameNotFoundException(dto.getLogin_id() + " 유저가 존재하지 않습니다."));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new CustomException(USER_NOT_FOUND, "비밀번호가 틀렸습니다.");
        }

        return LoginResponseDto.to(user);
    }
}
