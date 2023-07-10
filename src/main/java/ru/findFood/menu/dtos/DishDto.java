package ru.findFood.menu.dtos;

import java.math.BigDecimal;


public record DishDto(
        Long id,
        String title,

        Boolean healthy,

        String restaurant,

        String description,

        BigDecimal price,

        Integer calories,

        Integer proteins,

        Integer fats,

        Integer carbohydrates,

        Boolean approved,

        String category
) {
}