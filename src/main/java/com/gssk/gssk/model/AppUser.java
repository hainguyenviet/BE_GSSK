package com.gssk.gssk.model;

import com.gssk.gssk.security.account.ERole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "users", uniqueConstraints = {@UniqueConstraint(name = "email_register", columnNames = "email")})
public class AppUser implements UserDetails {

    @SequenceGenerator(
           name = "account_sequence",
           sequenceName = "account_sequence",
           allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "full_name")
    private String fullName;
    @NotNull
    @Column(name = "email")
    private String email;
    @NotNull
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column
    private ERole role;

    @Column(name = "is_locked")
    private Boolean locked = false;
    @Column(name = "is_enabled")
    private Boolean enabled = true;

    public AppUser(String email, String password, ERole role, Boolean locked, Boolean enabled){
        this.email = email;
        this.password = password;
        this.role = role;
        this.locked = locked;
        this.enabled = enabled;
    }



    public AppUser(String fullName, String email, String password, ERole role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
