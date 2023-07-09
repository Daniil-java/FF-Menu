package ru.findFood.menu.converters;

import org.springframework.stereotype.Component;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.entities.Menu;

@Component
public class MenuDtoConverter {
    public MenuDto entityToDto(Menu menu) {
        return new MenuDto(menu.getId(), menu.getDishDtoList(), menu.getPrice(),
                menu.getCalories(), menu.getProteins(), menu.getFats(), menu.getCarbohydrates());
    }

}
