package org.pragma.restaurantplaza.infrastructure.output.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;
    @NotNull
    private int document;
    @NotNull
    @Size(max = 13)
    @Pattern(regexp = "\\+[0-9]{12}")
    private String phone;
    @NotNull
    @Past
    private LocalDate birthdate;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String role;

 /*@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders;
    */
}
