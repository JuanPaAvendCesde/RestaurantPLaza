package org.pragma.restaurantplaza.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;

import java.util.List;

@Getter
@AllArgsConstructor
public class Restaurant {

    private Long id;
    private String name;
    private Integer nit;
    private String address;
    private String phone;
    private String urlLogo;
    private User userId;
    private List<MealEntity> meals;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

}
