package ru.findFood.menu.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.integrations.PersonAreaServiceIntegration;
import ru.findFood.menu.services.MenuService;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final PersonAreaServiceIntegration personAreaServiceIntegration;

//    не вижу необходимости в этом эндпойнте, прошу подтвердить
    @GetMapping("/get_menu")
    public MenuDto getMenu(@RequestBody GoalDto goalDto) {
        return menuService.getMenu(goalDto);
    }

    @GetMapping("/all")
    public MenuDto getMenuByName(
            @RequestParam(required = false) String telegramName,
            @RequestParam(required = false) String username) {
        GoalDto goal = getGoalDto(telegramName, username);
        return menuService.getMenu(goal);

    }

    private GoalDto getGoalDto(String telegramName, String username) {
        GoalDto goal;
        if (telegramName != null) {
            goal = personAreaServiceIntegration.getGoalByTelegramName(telegramName);
        } else {
            goal = personAreaServiceIntegration.getGoalByName(username);
        }
        return goal;
    }
}
