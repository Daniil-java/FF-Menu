package ru.findFood.menu.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.integrations.PersonAreaServiceIntegration;
import ru.findFood.menu.services.MenuService;

@RestController
@RequestMapping("/menu/v1/")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final PersonAreaServiceIntegration personAreaServiceIntegration;
    @GetMapping("/get_menu")
    public MenuDto getMenu(@RequestBody GoalDto goalDto) {
        return menuService.getMenu(goalDto);
    }

    @GetMapping("/get_menu_by_telegram_name")
    public MenuDto getMenuByTelegramName(@RequestHeader String username) {
        GoalDto goal = personAreaServiceIntegration.getGoalByTelegramName(username);
        return menuService.getMenu(goal);
    }

    @GetMapping("/get_menu_by_name")
    public MenuDto getMenuByName(@RequestHeader String username) {
        GoalDto goal = personAreaServiceIntegration.getGoalByName(username);
        return menuService.getMenu(goal);
    }
}
