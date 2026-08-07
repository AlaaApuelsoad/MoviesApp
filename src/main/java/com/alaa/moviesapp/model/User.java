package com.alaa.moviesapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends AuditEntity implements UserDetails, Serializable {


    @Id
    @SequenceGenerator(allocationSize = 1,name = "USER_PK_SEQUENCE",sequenceName = "USER_PK_SEQUENCE",initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "USER_PK_SEQUENCE")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

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

    @Column(nullable = false,length = 50)
    private String type;


    @Column(nullable = false, length = 50)
    private String saltPassword;

    @Column(length = 50)
    private String verificationCode;

    private Instant verificationCodeExpiryDate ;

    private Boolean isVerified = false;

    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonBackReference("userRoleReference")
    private Role role;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonManagedReference("userRatingReference")
    private List<MemberRating> memberRatings;

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority(role.getRoleName())
        );
    }
}
