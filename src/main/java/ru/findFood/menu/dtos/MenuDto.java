package ru.findFood.menu.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MenuDto {
    private Long id;
    private List<DishDto> dishDtoList;
    private BigDecimal price = BigDecimal.ZERO;
    private Integer calories = 0;
    private Integer proteins = 0;
    private Integer fats = 0;
    private Integer carbohydrates = 0;
}
