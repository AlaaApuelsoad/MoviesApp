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
public class Role extends AuditEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false)
    private Long id;

    @Column(length = 50,unique = true,nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference("userRoleReference")
    private Set<User> users;

    @Getter
    public enum RoleEnum {
        ADMIN("ADMIN",1L),
        MEMBER("MEMBER",2L);

        private final String name;
        private Long id;

        RoleEnum(String name,Long id) {
            this.name = name;
            this.id = id;
        }
    }

}
