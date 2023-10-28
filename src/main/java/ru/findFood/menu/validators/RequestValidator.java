package ru.findFood.menu.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.exceprions.AppException;

@Component
@RequiredArgsConstructor
public class RequestValidator {
    public void validate(GoalDto goalDto) throws AppException {
        if (goalDto.timesToEat() == null || goalDto.timesToEat() == 0){
            throw new AppException(HttpStatus.BAD_REQUEST.value(),
                    "Невозможно произвести подбор меню при отсутствии параметра 'Количество приемов пищи в день'!!!");
        }
    }
}
