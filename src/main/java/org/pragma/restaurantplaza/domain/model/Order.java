package org.pragma.restaurantplaza.domain.model;

import lombok.AllArgsConstructor;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
public class Order {

    private Long id;

    private User user;

    private Restaurant restaurant;

    private List<MealEntity> meals;

    private OrderStatus orderStatus;
    private Long assignedEmployeeId;
    private int quantity;

    private String securityPin;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;



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

    public List<MealEntity> getMeals(List<Long> mealIds) {
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

    public Long getAssignedEmployeeId() {
        return assignedEmployeeId;
    }

    public void setAssignedEmployeeId(Long assignedEmployeeId) {
        this.assignedEmployeeId = assignedEmployeeId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<MealEntity> getMeals() {
        return meals;
    }

    public String getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }


    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
