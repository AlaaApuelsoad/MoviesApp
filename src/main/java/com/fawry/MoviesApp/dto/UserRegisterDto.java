package com.fawry.MoviesApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("firstName")
    @NotBlank(message = "first name cannot be null")
    @Size(min = 3, max = 50,message = "first name minimum length 3, maximum length is 50")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "first name is only in alphabets")
    private String firstName;

    @NotBlank(message = "last name cannot be null")
    @Size(min = 3, max = 50,message = "last name minimum length 5, maximum length is 50")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "last name is only in alphabets")
    private String lastName;

    @NotBlank(message = "username cannot bu null")
    @Size(min = 5,max = 50,message = "username minimum length 5, maximum length is 50")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "username is only in alphabets")
    private String username;

    @NotBlank(message = "email cannot be null")
    @Size(min = 15,max = 100)
    @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "Please enter a valid email" +
            " address in the format: username@example.com.")
    private String email;

    @NotBlank(message = "password cannot be null")
    @Size(min = 8, max = 50,message = "password minimum length 8 and maximum length 50")
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
