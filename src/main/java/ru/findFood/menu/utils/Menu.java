package ru.findFood.menu.utils;


import lombok.Getter;
import ru.findFood.menu.dtos.DishDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Menu {

    private final List<DishDto> dishDtoList;
    private BigDecimal price;
    private Integer calories;
    private Integer proteins;
    private Integer fats;
    private Integer carbohydrates;

    public Menu() {
        this.dishDtoList = new ArrayList<>();
        this.price = BigDecimal.ZERO;
        this.calories = 0;
        this.proteins = 0;
        this.fats = 0;
        this.carbohydrates = 0;
    }

    public void addDish(DishDto dishDto) {
        dishDtoList.add(dishDto);
        reCalculate();
    }
    public void removeLastDish() {
        int lastIndex = dishDtoList.size() - 1;
        dishDtoList.remove(lastIndex);
        reCalculate();
    }

    private void reCalculate() {
        price = BigDecimal.ZERO;
        calories = 0;
        proteins = 0;
        fats = 0;
        carbohydrates = 0;
        for (DishDto dishDto : dishDtoList) {
            price = price.add(dishDto.price());
            calories += dishDto.calories();
            proteins += dishDto.proteins();
            fats += dishDto.fats();
            carbohydrates += dishDto.carbohydrates();
        }
    }
}

