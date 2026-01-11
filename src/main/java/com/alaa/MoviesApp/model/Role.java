package com.alaa.MoviesApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id",nullable = false,updatable = false)
    private long id;

    @Column(name = "role_name",length = 50,unique = true,nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference("userRoleReference")
    private Set<User> users;

    public enum RoleEnum {
        ADMIN("ADMIN"),
        MEMBER("MEMBER");

        private String name;

        RoleEnum(String name) {
            this.name = name;
        }
    }

}
