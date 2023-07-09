package ru.findFood.menu.dtos;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DishDto {      //Временное содержание файла. Неободимая для найстройки тестов

    private Long id;

    private String title;

    private Boolean healthy;

    private RestaurantDto restaurantDto;

    private String description;

    private BigDecimal price;

    private byte[] image;

    private Integer calories;

    private Integer proteins;

    private Integer fats;

    private Integer carbohydrates;

    private Boolean approved;

//    private String groupDishDto.getTitle;
    private GroupDishDto groupDishDto;

    private CategoryDto categoryDto;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}