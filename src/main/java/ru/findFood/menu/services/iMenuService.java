package ru.findFood.menu.services;

import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.entities.Menu;

public interface iMenuService {
    public Menu makeMenu(GoalDto goalDto);  //Создание меню, на основе целей клента
    public Menu getMenu(GoalDto goalDto);   //Поиск из уже готовых меню, приближенных к цели клиента
    public void clearMenuTable();
}
