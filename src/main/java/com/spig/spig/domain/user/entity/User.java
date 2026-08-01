package com.spig.spig.domain.user.entity;

import com.spig.spig.domain.user.dto.JoinRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;


    public static User from(JoinRequestDto dto) {
        return User.builder()
                .loginId(dto.getLogin_id())
                .password(dto.getPassword())
                .name(dto.getName())
                .role(
                        UserRole.USER
                ).build();
    }
}