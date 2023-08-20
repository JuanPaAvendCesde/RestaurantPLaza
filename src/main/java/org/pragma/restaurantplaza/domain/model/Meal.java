package org.pragma.restaurantplaza.domain.model;

public class Meal {

    //Nombre del plato, precio del plato (en números enteros positivos y mayores a 0), Descripción, UrlImagen y la categoria.


    private Long id;

    private String name;

    private int price;

    private String description;

    private String urlImage;

    private String category;

    public Meal(Long id, String name, int price, String description, String urlImage, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.urlImage = urlImage;
        this.category = category;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
