package ru.findFood.menu.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
