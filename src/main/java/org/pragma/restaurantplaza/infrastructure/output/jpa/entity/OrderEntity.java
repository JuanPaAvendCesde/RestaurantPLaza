package org.pragma.restaurantplaza.infrastructure.output.jpa.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pragma.restaurantplaza.domain.model.OrderStatus;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_user")
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @ManyToMany
    @JoinTable(
            name = "order_meal",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private List<MealEntity> meals;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Long assignedEmployeeId;
    private int quantity;
    private String securityPin;
    private LocalDateTime timestamp;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Integer TotalAmount;

}
