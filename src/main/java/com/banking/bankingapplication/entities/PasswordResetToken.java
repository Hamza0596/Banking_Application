package com.banking.bankingapplication.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private Users user;

    public PasswordResetToken(String token, LocalDateTime expirationDate, Users user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationDate) ? true : false;
    }
}
