package com.fawry.MoviesApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponse {

    private String message = "User registered successfully.";
    private long id;
    private String username;
    private String email;
    private String type;
    private String accountVerify = "Pending Verified";
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registerDate = LocalDateTime.now();
}
