package org.pragma.restaurantplaza.infrastructure.output.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "meal")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private String description;

    private String urlImage;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantId")
    private RestaurantEntity restaurantId;
    private boolean active = true;
}
