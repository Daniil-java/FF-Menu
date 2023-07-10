package ru.findFood.menu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "dishes")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Dish {

    private final static int CALORIES_IN_PROTEIN = 4;
    private final static int CALORIES_IN_FAT = 9;
    private final static int CALORIES_IN_CARBOHYDRATE = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "calories", nullable = false)
    private Integer calories;

    @Column(name = "proteins", nullable = false)
    private Integer proteins;

    @Column(name = "fats", nullable = false)
    private Integer fats;

    @Column(name = "carbohydrates", nullable = false)
    private Integer carbohydrates;

    @Column(name = "approved")
    private Boolean approved;

    @Column(name = "healthy")
    private Boolean healthy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @Column(name = "used_last_time")
    private LocalDateTime usedLastTime;


    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    //конструктор с обязательными полями
    public Dish(@NotNull String title, Integer proteins, Integer fats, Integer carbohydrates, Category category) {
        this.title = title;
        this.calories = proteins * CALORIES_IN_PROTEIN + fats * CALORIES_IN_FAT + carbohydrates * CALORIES_IN_CARBOHYDRATE;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Dish dish = (Dish) o;
        return getId() != null && Objects.equals(getId(), dish.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
