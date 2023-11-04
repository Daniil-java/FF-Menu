package ru.findFood.menu.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.exceprions.AppError;
import ru.findFood.menu.exceprions.AppException;
import ru.findFood.menu.integrations.PersonAreaServiceIntegration;
import ru.findFood.menu.services.MenuService;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final PersonAreaServiceIntegration personAreaServiceIntegration;

    //    не вижу необходимости в этом эндпойнте, прошу подтвердить
    @PostMapping("/get_menu")
    public ResponseEntity<?> getMenu(@RequestBody GoalDto goalDto) {
        try {
            return ResponseEntity.ok(menuService.getMenu(goalDto));
        }catch (AppException e){
            return new ResponseEntity<> (new AppError(e.getStatus(), e.getMessage()), HttpStatusCode.valueOf(e.getStatus()));
        }
    }

    @GetMapping("/all")
    public  ResponseEntity<?> getMenuByName(
            @RequestParam(required = false) String telegramName,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer timesToEat
    ) {
        GoalDto goalReceived = getGoalDto(telegramName, username);
        GoalDto goal = new GoalDto(goalReceived.title(),
                goalReceived.calories(),
                goalReceived.proteins(),
                goalReceived.fats(),
                goalReceived.carbohydrates(),
                timesToEat);

        try {
            return ResponseEntity.ok(menuService.getMenu(goal));
        }catch (AppException e){
            return new ResponseEntity<> (new AppError(e.getStatus(), e.getMessage()), HttpStatusCode.valueOf(e.getStatus()));
        }

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
