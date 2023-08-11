package ru.findFood.menu.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.utils.SelectAlgorithm;

@Service
@RequiredArgsConstructor

public class MenuService {

    private final SelectAlgorithm algorithm;

    public MenuDto getMenu(GoalDto goal) {
        return algorithm.getMenu(goal);
    }
}
