package com.example.project_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String email;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private String nickname;  // getter/setter 포함

    // 회원가입 시 닉네임이 username으로 기본 세팅
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.nickname == null || this.nickname.isEmpty()) {
            this.nickname = this.username;
        }
    }


}
