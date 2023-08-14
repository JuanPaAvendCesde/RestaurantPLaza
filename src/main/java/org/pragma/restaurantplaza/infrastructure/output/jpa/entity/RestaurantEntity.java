package org.pragma.restaurantplaza.infrastructure.output.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @NotBlank
    private String name;
    @NotBlank
    private Integer nit;
    @NotBlank
    private String address;
    @NotBlank
    @Size(max = 13)
    @Pattern(regexp = "[0-9+]")
    private String phone;
    @NotBlank
    private String urlLogo;
    @NotBlank

    @OneToOne
    @JoinColumn(name = "owner_id")  // Cambiado a owner_id
    private OwnerEntity ownerId;
}
