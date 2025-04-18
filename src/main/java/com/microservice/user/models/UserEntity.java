package com.microservice.user.models;

import com.microservice.user.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String username;
    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    @Column(name = "account_No_Expired")
    private boolean accountNoExpired = true;

    @Column(name = "account_No_Locked")
    private boolean accountNoLocked = true;

    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired = true;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
