package org.pragma.restaurantplaza.domain.model;


public class Meal {

    private Long id;
    private String name;
    private final int price;
    private final String description;
    private final String urlImage;
    private final String category;
    private final Restaurant restaurantId;
    public Meal(Long id, String name, int price, String description, String urlImage, String category, Restaurant restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.urlImage = urlImage;
        this.category = category;
        this.restaurantId = restaurantId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getCategory() {
        return category;
    }

    public Restaurant getRestaurantId() {
        return restaurantId;
    }
}
