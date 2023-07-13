package ru.findFood.menu.dtos.notMvp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDishDto {

    Long id;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
