package ru.findFood.menu.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.findFood.menu.dtos.DishDto;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.exceprions.NotFoundException;
import ru.findFood.menu.services.DishesService;
import ru.findFood.menu.services.iMainAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.findFood.menu.converters.Converter.menuToDto;


@Component
@RequiredArgsConstructor
@Slf4j
public class SelectAlgorithm implements iMainAlgorithm {
    private final static int THREE_MEALS = 3;

    private final DishesService dishesService;
    private final static int REFRESH_INTERVAL = 10_000;
    private final static int STEP_BETWEEN_CALORIES = 50;
    private final static int QUEUE_SIZE = 50;

    private final RedisTemplate<String, Object> redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Thread t = new Thread(() -> {
            while (true) {
                List<DishDto> breakfasts = dishesService.findBreakfasts();
                List<DishDto> lunches = dishesService.findLunches();
                List<DishDto> dinners = dishesService.findDinners();
                List<DishDto> snacks = dishesService.findSnack();

                Menu menu = new Menu();
                fillMenuMaps(List.of(breakfasts, lunches, dinners, snacks, snacks), 0, menu);
                waitForAWhile();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void fillMenuMaps(List<List<DishDto>> menuList, int index, Menu menu) {
        if (index == menuList.size()) return;

        List<DishDto> dishByCategory = menuList.get(index);
        for (DishDto dish : dishByCategory) {
            menu.addDish(dish);


            addMenuToMap(index + 1, menu);
            fillMenuMaps(menuList, index + 1, menu);
            menu.removeLastDish();
        }
    }


    private void addMenuToMap(int timesToEat, Menu menu) {
        if (timesToEat >= THREE_MEALS) {
            int calories = menu.getCalories() / STEP_BETWEEN_CALORIES * STEP_BETWEEN_CALORIES;
            String key = timesToEat + "_" + calories;
            if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, new BoundedQueue<MenuDto>(QUEUE_SIZE)))) {

                BoundedQueue<MenuDto> boundedQueue = getMenuDtoQueue(key);

                boundedQueue.add(menuToDto(menu));
                redisTemplate.opsForValue().set(key, boundedQueue);
            }
        }
    }

    private BoundedQueue<MenuDto> getMenuDtoQueue(String key) {
        Optional<Object> object = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        BoundedQueue<MenuDto> boundedQueue = (BoundedQueue<MenuDto>) object.orElseThrow(
                () -> new NotFoundException(key)
        );
        return boundedQueue;
    }

    private void waitForAWhile() {
        try {
            Thread.sleep(REFRESH_INTERVAL);
        } catch (InterruptedException e) {
            log.error(e.toString());
        }
    }


    @Override
    public MenuDto getMenu(GoalDto goal) {
        List<MenuDto> menus = getClosestByCaloriesList(goal.calories(), goal.timesToEat());
        MenuDto bestMenu = getClosestByPFC(menus, goal);
        markUsedDishes(bestMenu);
        return bestMenu;
    }

    private MenuDto getClosestByPFC(List<MenuDto> menus, GoalDto goal) {
        int minDiff = Integer.MAX_VALUE;
        int id = 0;
        for (int i = 0; i < menus.size(); i++) {
            int tempDiff = calculateDistance(goal, menus.get(i));
            if (tempDiff < minDiff) {
                id = i;
            }
        }
        return menus.get(id);
    }

    private int calculateDistance(GoalDto goal, MenuDto menu) {
        return (int) Math.sqrt(
                Math.pow((goal.proteins() - menu.proteins()), 2)
                        +
                        Math.pow((goal.fats() - menu.fats()), 2)
                        +
                        Math.pow((goal.carbohydrates() - menu.carbohydrates()), 2)
        );
    }

    private List<MenuDto> getClosestByCaloriesList(Integer calories, Integer timesToEat) {
        int bestKey = Integer.MAX_VALUE;
        int minDiff = Integer.MAX_VALUE;
        String prefix = timesToEat + "_";
        List<Integer> keys = Optional.ofNullable(redisTemplate.keys(prefix + "*"))
                .orElseThrow(() -> new NotFoundException("RedisTemplate is empty"))
                .stream()
                .map(k -> Integer.valueOf(k.substring(prefix.length())))
                .toList();

        for (Integer key : keys) {
            int tempDiff = Math.abs(key - calories);
            if (tempDiff < minDiff) {
                minDiff = tempDiff;
                bestKey = key;
            }
        }
        String key = prefix + bestKey;
        return new ArrayList<>(getMenuDtoQueue(key));
    }

    private void markUsedDishes(MenuDto bestMenu) {
        dishesService.markDishesAsUsed(bestMenu.dishDtoList());
    }
}
