package org.pragma.restaurantplaza.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@Getter
@Setter
public class User {

    private Long id;
    private String name;
    private int document;
    private String phone;
    private LocalDate birthdate;
    private String email;
    private String password;
    private String role;



}