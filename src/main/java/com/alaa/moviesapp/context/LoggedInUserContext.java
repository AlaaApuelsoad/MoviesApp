package com.alaa.MoviesApp.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoggedInUserContext {

    private Long userId;
    private String userName;
    private String email;
    private String role;
    private String type;

}
