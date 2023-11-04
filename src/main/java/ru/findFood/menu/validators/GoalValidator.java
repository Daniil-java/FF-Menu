package ru.findFood.menu.validators;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.exceprions.AppException;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GoalValidator {
    public void validate(GoalDto goalDto) throws AppException {
        if (Objects.equals(goalDto.title(), "No data") ||
           (goalDto.calories() == 0 && goalDto.proteins() == 0 && goalDto.fats() == 0 && goalDto.carbohydrates() == 0)){
            throw new AppException(HttpStatus.BAD_REQUEST.value(),
                    "Ваша анкета не заполнена!!!\n" +
                    "Правильный подбор меню не возможен!!!\n" +
                    "Для использования сервиса необходимо заполнить Вашу анкету.");
        }
    }
}
