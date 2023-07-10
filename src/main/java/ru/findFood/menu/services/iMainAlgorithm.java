package ru.findFood.menu.services;

import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;

public interface iMainAlgorithm {
    MenuDto getMenu(GoalDto goal);
}
