package ru.findFood.menu.converters;

import ru.findFood.menu.dtos.DishDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.entities.Dish;
import ru.findFood.menu.utils.Menu;


public class Converter {
    public static DishDto dishToDto(Dish dish) {
        return new DishDto(
                dish.getId(),
                dish.getTitle(),
                dish.getHealthy(),
                dish.getRestaurant().getTitle(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getCalories(),
                dish.getProteins(),
                dish.getFats(),
                dish.getCarbohydrates(),
                dish.getApproved(),
                dish.getCategory().getTitle());
    }

    public static MenuDto menuToDto(Menu menu) {
        return new MenuDto(
                menu.getDishDtoList().stream()
                        .map(d -> new DishDto(
                                d.id(),
                                d.title(),
                                d.healthy(),
                                d.restaurant(),
                                d.description(),
                                d.price(),
                                d.calories(),
                                d.proteins(),
                                d.fats(),
                                d.carbohydrates(),
                                d.approved(),
                                d.category()
                        ))
                        .toList(),
                menu.getPrice(),
                menu.getCalories(),
                menu.getProteins(),
                menu.getFats(),
                menu.getCarbohydrates()
        );
    }
}
