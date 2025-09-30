package com.pavelg.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String email;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private UserState userState = UserState.ACTIVE;

    private String passwordHash;

    @CreationTimestamp
    @Column(updatable = false)
    @Getter
    private Instant createdDate;

    @UpdateTimestamp
    @Getter
    @Setter
    private Instant lastModifiedDate;

    public User(String fullName, String email, String passwordHash) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public User (String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }
}
