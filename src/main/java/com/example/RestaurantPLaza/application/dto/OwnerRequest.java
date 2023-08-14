package com.example.RestaurantPLaza.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OwnerRequest {

    private String name;
    private int document;
    private String phone;
    private LocalDate birthdate;
    private String email;
    private String password;
    private String rol;
}
