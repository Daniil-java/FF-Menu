package ru.findFood.menu.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.antlr.v4.runtime.misc.NotNull;
import ru.findFood.menu.dtos.DishDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Menu {


    private Long id;
    private List<DishDto> dishDtoList;
    private BigDecimal price = BigDecimal.ZERO;
    private Integer calories = 0;
    private Integer proteins = 0;
    private Integer fats = 0;
    private Integer carbohydrates = 0;

    //Конструктор Меню. Автоматически высчитывает
    // потребные данные, при передаче Списка Блюд
    public Menu(List<DishDto> dishDtoList) {
        this.dishDtoList = dishDtoList;
        for (DishDto dish : dishDtoList) {
            this.calories += dish.getCalories();
            this.fats += dish.getFats();
            this.carbohydrates += dish.getCarbohydrates();
            this.proteins += dish.getProteins();
            this.price = price.add(dish.getPrice());
        }
    }

    //Метод для добавления Блюда, в Список Меню
    public void addDish(DishDto dishDto) {
        dishDtoList.add(dishDto);
        this.calories += dishDto.getCalories();
        this.fats += dishDto.getFats();
        this.carbohydrates += dishDto.getCarbohydrates();
        this.proteins += dishDto.getProteins();
        this.price = price.add(dishDto.getPrice());
    }

    //Метод для удаления Блюда из Списка по индексу
    public void removeDishIndexOf(int index) {
        this.calories -= dishDtoList.get(index).getCalories();
        this.fats -= dishDtoList.get(index).getFats();
        this.carbohydrates -= dishDtoList.get(index).getCarbohydrates();
        this.proteins -= dishDtoList.get(index).getProteins();
        this.price = price.subtract(dishDtoList.get(index).getPrice());
        dishDtoList.remove(dishDtoList.remove(index));
    }

    //Метод для удаления последнеого Блюда из Списка
    public void removeLastDish() {
        int lastDishIndex = dishDtoList.size()-1;
        this.calories -= dishDtoList.get(lastDishIndex).getCalories();
        this.fats -= dishDtoList.get(lastDishIndex).getFats();
        this.carbohydrates -= dishDtoList.get(lastDishIndex).getCarbohydrates();
        this.proteins -= dishDtoList.get(lastDishIndex).getProteins();
        this.price = price.subtract(dishDtoList.get(lastDishIndex).getPrice());
        dishDtoList.remove(dishDtoList.remove(lastDishIndex));
    }

    //Метод для очистки Списка Блюд
    public void clearDishDtoList() {
        dishDtoList.clear();
        this.calories = 0;
        this.fats = 0;
        this.carbohydrates = 0;
        this.proteins = 0;
        this.price = BigDecimal.ZERO;
    }

}
