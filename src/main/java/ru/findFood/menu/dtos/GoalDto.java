package ru.findFood.menu.dtos;

public record GoalDto(
        String title,
        Integer calories,
        Integer proteins,
        Integer fats,
        Integer carbohydrates,
        Integer timesToEat) {
}