package org.pragma.restaurantplaza.infrastructure.output.jpa.entity;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;


@Entity
@Table(name = "owner")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private int document;
    @NotBlank
    @Size(max = 13)
    @Pattern(regexp = "[0-9+]")
    @NotBlank
    private String phone;
    @NotBlank
    @Past
    private LocalDate fechaNacimineto;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String rol;
}
