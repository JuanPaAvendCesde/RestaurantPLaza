package org.pragma.restaurantplaza.domain.model;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class Restaurant {

    private Long id;
    private String name;
    private Integer nit;
    private String address;
    private String phone;
    private String urlLogo;
    private User ownerId;
   /* private List<MealEntity> meals;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNit() {
        return nit;
    }

    public void setNit(Integer nit) {
        this.nit = nit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }



    public User getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }
/*
    public List<MealEntity> getMeals() {
        return meals;
    }

    public void setMeals(List<MealEntity> meals) {
        this.meals = meals;
    }*/
}
