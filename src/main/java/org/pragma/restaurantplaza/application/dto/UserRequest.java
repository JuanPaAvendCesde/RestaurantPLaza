package org.pragma.restaurantplaza.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class UserRequest {
    private Long id;
    private String name;
    private int document;
    private String phone;
    private LocalDate birthdate;
    private String email;
    private String password;
    private String role;


}
