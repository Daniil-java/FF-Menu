package ru.findFood.menu.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.exceprions.AppException;
import ru.findFood.menu.utils.SelectAlgorithm;
import ru.findFood.menu.validators.GoalValidator;
import ru.findFood.menu.validators.RequestValidator;

@Service
@RequiredArgsConstructor

public class MenuService {

    private final SelectAlgorithm algorithm;
    private final RequestValidator requestValidator;
    private final GoalValidator goalValidator;

    public MenuDto getMenu(GoalDto goal) throws AppException {
        requestValidator.validate(goal);
        goalValidator.validate(goal);
        return algorithm.getMenu(goal);
    }
}
