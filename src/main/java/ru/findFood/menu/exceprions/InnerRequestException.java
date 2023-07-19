package ru.findFood.menu.exceprions;

public class InnerRequestException extends RuntimeException{
    public InnerRequestException(String message) {
        super(message);
    }
}
