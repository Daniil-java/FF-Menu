package ru.findFood.menu.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record UpdateDishTimeRequest(Map<Long, LocalDateTime> map){}
