package org.pragma.restaurantplaza.domain.model;

import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;

import java.util.List;

public class Order {

    private Long id;

    private User user;

    private Restaurant restaurant;

    private List<MealEntity> meals;

    private OrderStatus orderStatus;

    private int quantity;

    public Order(Long id, User user, Restaurant restaurant, List<MealEntity> meal, OrderStatus orderStatus, int quantity) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.meals = meal;
        this.orderStatus = orderStatus;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<MealEntity> getMeals() {
        return meals;
    }

    public void setMeals(List<MealEntity> meals) {
        this.meals = meals;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
