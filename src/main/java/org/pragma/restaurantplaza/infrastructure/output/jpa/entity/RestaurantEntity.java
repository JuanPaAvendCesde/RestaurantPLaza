package org.pragma.restaurantplaza.infrastructure.output.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9]+$")
    private String name;

    private Integer nit;

    private String address;
    @Size(max = 13)
    @Pattern(regexp = "\\+[0-9]{12}")

    private String phone;

    private String urlLogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId") // Asegúrate de que coincida con el nombre de la columna en tu base de datos
    private UserEntity ownerId;
    @OneToMany(mappedBy = "restaurantId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealEntity> meals;



}
