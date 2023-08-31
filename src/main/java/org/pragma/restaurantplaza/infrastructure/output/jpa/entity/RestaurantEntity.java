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
    @NotBlank
    private String name;
    @NotBlank
    private Integer nit;
    @NotBlank
    private String address;
    @Size(max = 13)
    @Pattern(regexp = "[0-9+]")
    @NotBlank
    private String phone;
    @NotBlank
    private String urlLogo;
    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity userId;
    @OneToMany(mappedBy = "restaurantId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealEntity> meals;


}
