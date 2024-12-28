package com.fawry.MoviesApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user")
public class User extends AuditEntity{

    @Id
    @SequenceGenerator(allocationSize = 1,name = "user_sequence",sequenceName = "user_sequence",initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    @Column(name = "user_id", updatable = false, nullable = false)
    private long id;

    @Column(nullable = false,length = 50)
    private String firstName;

    @Setter
    @Column(nullable = false,length = 50)
    private String lastName;

    @Column(nullable = false,length = 50,unique = true)
    private String username;

    @Column(nullable = false,length = 100,unique = true)
    private String email;

    @Column(nullable = false,length = 50)
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


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setSaltPassword(String saltPassword) {
        this.saltPassword = saltPassword;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
