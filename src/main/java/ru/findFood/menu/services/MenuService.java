package ru.findFood.menu.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.entities.Menu;
import ru.findFood.menu.repositories.MenuRepository;

@Service
@RequiredArgsConstructor
public class MenuService implements iMenuService{

    private final MenuRepository menuRepository;

    @Override
    public Menu makeMenu(GoalDto goalDto) {
        Menu menu = new Menu();
        return menu;
    }

    @Override
    //Поиск из уже готовых меню, приближенных к цели клиента
    public Menu getMenu(GoalDto goalDto) {
        return null;
    }

    @Override
    public void clearMenuTable() {
        menuRepository.deleteAll();
    }


}
