package ru.findFood.menu.exceprions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppException extends Throwable{

    private Integer status;
    private String message;
}
