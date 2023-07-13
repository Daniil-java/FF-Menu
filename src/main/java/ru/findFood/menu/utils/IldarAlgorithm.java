package ru.findFood.menu.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.dtos.MenuDto;
import ru.findFood.menu.entities.Dish;
import ru.findFood.menu.services.DishesService;
import ru.findFood.menu.services.iMainAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import static ru.findFood.menu.utils.Converter.dishToDto;
import static ru.findFood.menu.utils.Converter.menuToDto;

@Component
@RequiredArgsConstructor

public class IldarAlgorithm implements iMainAlgorithm {
    private final DishesService dishesService;
    private final static int FIVE_MEALS = 5;
    private final static int FOUR_MEALS = 4;
    private final static int THREE_MEALS = 3;

    private final static int REFRESH_INTERVAL = 10_000;
    private final static int STEP_BETWEEN_CALORIES = 100;
    private final static int QUEUE_SIZE = 50;

    private ConcurrentHashMap<Integer, ArrayBlockingQueue<MenuDto>> threes;
    private ConcurrentHashMap<Integer, ArrayBlockingQueue<MenuDto>> fours;
    private ConcurrentHashMap<Integer, ArrayBlockingQueue<MenuDto>> fives;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        threes = new ConcurrentHashMap<>();
        fours = new ConcurrentHashMap<>();
        fives = new ConcurrentHashMap<>();

        Thread t = new Thread(() -> {
            while (true) {
                List<Dish> breakfasts = dishesService.findBreakfasts();
                List<Dish> lunches = dishesService.findLunches();
                List<Dish> dinners = dishesService.findDinners();
                List<Dish> snacks = dishesService.findSnack();

                Menu menu = new Menu();
                fillMenuMaps(List.of(breakfasts, lunches, dinners, snacks, snacks), 0, menu);
                waitForAWhile();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    //Использовал рекурсию и бэктрекинг
    private void fillMenuMaps(List<List<Dish>> menuList, int index, Menu menu) {
        if (index == menuList.size()) return;

        List<Dish> dishByCategory = menuList.get(index);
        for (Dish dish : dishByCategory) {
            menu.addDish(dishToDto(dish));
            switch (index + 1) {
                case THREE_MEALS -> addMenuToMap(threes, menu);
                case FOUR_MEALS -> addMenuToMap(fours, menu);
                case FIVE_MEALS -> addMenuToMap(fives, menu);
            }
            fillMenuMaps(menuList, index + 1, menu);
            menu.removeLastDish();
        }
    }


    private void addMenuToMap(Map<Integer, ArrayBlockingQueue<MenuDto>> map, Menu menu) {
        int calories = menu.getCalories() / STEP_BETWEEN_CALORIES * STEP_BETWEEN_CALORIES;
        if (!map.containsKey(calories)) {
            map.put(calories, new ArrayBlockingQueue<>(QUEUE_SIZE, true));
        }
        ArrayBlockingQueue<MenuDto> menuQueue = map.get(calories);
        if (menuQueue.size() == QUEUE_SIZE) {
            menuQueue.poll();
        }
        menuQueue.add(menuToDto(menu));
        map.put(calories, menuQueue);
    }

    private void waitForAWhile() {
        try {
            Thread.sleep(REFRESH_INTERVAL);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MenuDto getMenu(GoalDto goal) {
        Map<Integer, ArrayBlockingQueue<MenuDto>> map = chooseCurrentMap(goal.timesToEat());
        List<MenuDto> menus = getClosestByCaloriesList(map, goal.calories());
        MenuDto bestMenu = findClosestByPFC(menus, goal);
        markUsedDishes(bestMenu);
        return bestMenu;
    }


    private void markUsedDishes(MenuDto bestMenu) {

        dishesService.markDishesAsUsed(bestMenu.dishDtoList());

    }

    private MenuDto findClosestByPFC(List<MenuDto> menus, GoalDto goal) {
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

    private List<MenuDto> getClosestByCaloriesList(Map<Integer, ArrayBlockingQueue<MenuDto>> map, Integer calories) {
        int bestKey = Integer.MAX_VALUE;
        int minDiff = Integer.MAX_VALUE;
        for (Integer key : map.keySet()) {
            int tempDiff = Math.abs(key - calories);
            if (tempDiff < minDiff) {
                minDiff = tempDiff;
                bestKey = key;
            }
        }
        return new ArrayList<>(map.get(bestKey));
    }

    private Map<Integer, ArrayBlockingQueue<MenuDto>> chooseCurrentMap(Integer timesToEat) {
        return switch (timesToEat) {
            case THREE_MEALS -> threes;
            case FOUR_MEALS -> fours;
            case FIVE_MEALS -> fives;
            default ->
                    throw new RuntimeException(String.format("The %d-times-a-day eating scheme is not supported", timesToEat));
        };
    }
}
