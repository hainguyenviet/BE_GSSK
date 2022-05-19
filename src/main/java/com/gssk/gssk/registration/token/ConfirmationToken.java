package com.gssk.gssk.registration.token;


import com.gssk.gssk.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table (name = "tbl_confirmationToken")
public class ConfirmationToken {

    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "token")
    private String token;
    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @NotNull
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;
    @ManyToOne(targetEntity = Account.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, Account account){
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiredAt;
        this.account = account;
    }
}
