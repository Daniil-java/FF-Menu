package ru.findFood.menu.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.services.MenuService;

@RestController
@RequestMapping("/menu/v1/")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    @GetMapping("/get_menu")
    public MenuDto getMenu(@RequestBody GoalDto goalDto) {
        return menuService.getMenu(goalDto);
    }


}
