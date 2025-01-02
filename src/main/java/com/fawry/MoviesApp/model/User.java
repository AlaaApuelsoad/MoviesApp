package com.fawry.MoviesApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user")
public class User extends AuditEntity implements UserDetails {

    @Id
    @SequenceGenerator(allocationSize = 1,name = "user_sequence",sequenceName = "user_sequence",initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    @Column(name = "user_id", updatable = false, nullable = false)
    private long id;

    @Column(nullable = false,length = 50)
    private String firstName;

    @Column(nullable = false,length = 50)
    private String lastName;

    @Column(nullable = false,length = 50,unique = true)
    private String username;

    @Column(nullable = false,length = 100,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String imagePath;

    @Column(nullable = true,length = 50)
    private String saltPassword;

    @Column(length = 50)
    private String verificationCode;

    private boolean isVerified = false;

    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference("userRoleReference")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()-> role.getRoleName());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", saltPassword='" + saltPassword + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", isVerified=" + isVerified +
                ", isDeleted=" + isDeleted +
                ", role=" + role +
                '}';
    }
}
