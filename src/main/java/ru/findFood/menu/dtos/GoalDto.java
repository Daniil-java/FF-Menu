package ru.findFood.menu.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoalDto {
    //Сущность указывающая параметры, к которым должно быть приближенно меню
    //Временное содержание файла. Неободимая для найстройки тестов

    private String title;
    private Integer calories;
    private Integer proteins;
    private Integer fats;
    private Integer carbohydrates;
}