package ru.findFood.menu.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.utils.IldarAlgorithm;

@Service
@RequiredArgsConstructor

public class MenuService {

    private final IldarAlgorithm algorithm;

    public MenuDto getMenu(GoalDto goal) {
        return algorithm.getMenu(goal);
    }
}
