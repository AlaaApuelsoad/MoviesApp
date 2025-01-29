package com.fawry.MoviesApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    @NotBlank(message = "{firstName.validation.message}")
    @Size(min = 3, max = 50,message = "{firstName.validation.message}")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "{firstName.validation.message}")
    private String firstName;

    @NotBlank(message = "{lastName.validation.message}")
    @Size(min = 3, max = 50,message = "{lastName.validation.message}")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "{lastName.validation.message}")
    private String lastName;

    @NotBlank(message = "{username.validation.message}")
    @Size(min = 5,max = 50,message = "{username.validation.message}")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "{username.validation.message}")
    private String username;

    @NotBlank(message = "{email.validation.message}")
    @Size(min = 10,max = 100,message = "{email.validation.message}")
    @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "{email.validation.message}")
    private String email;

    @NotBlank(message = "{password.validation.message}")
    @Size(min = 8, max = 50,message = "{password.validation.message}")
    private String password;

    @Override
    public String toString() {
        return "UserRegisterDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
