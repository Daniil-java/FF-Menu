package ru.findFood.menu.dtos;

import java.math.BigDecimal;
import java.util.List;

public record MenuDto(List<DishDto> dishDtoList,
                      BigDecimal price,
                      Integer calories,
                      Integer proteins,
                      Integer fats,
                      Integer carbohydrates) {
}
