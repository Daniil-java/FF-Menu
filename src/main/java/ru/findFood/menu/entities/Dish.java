package ru.findFood.menu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "dishes")
@RequiredArgsConstructor
@Data
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "image_data")
    private byte[] image;

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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "group_dish_id")
    private GroupDish groupDish;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    //минимальный конструктор на всякий случай
//    public Dish(String title) {
//        this.title = title;
//    }
//

    //конструктор с обязательными полями
    public Dish(String title, Integer calories, Integer proteins, Integer fats, Integer carbohydrates, LocalDateTime createdAt) {
        this.title = title;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.createdAt = createdAt;
    }

//    //почти полный конструктор
//    public Dish(String title, Long restaurant_id, String description, BigDecimal price, Integer calories, Integer proteins, Integer fats, Integer carbohydrates, GroupDish groupDish, List<Category> categories, LocalDateTime createdAt) {
//        this.title = title;
//        this.restaurant_id = restaurant_id;
//        this.description = description;
//        this.price = price;
//        this.calories = calories;
//        this.proteins = proteins;
//        this.fats = fats;
//        this.carbohydrates = carbohydrates;
//        this.groupDish = groupDish;
//        this.categories = categories;
//        this.createdAt = createdAt;
//    }
}
