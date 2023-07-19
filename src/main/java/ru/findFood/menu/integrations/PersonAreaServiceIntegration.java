package ru.findFood.menu.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.exceprions.InnerRequestException;

@Component
@RequiredArgsConstructor
public class PersonAreaServiceIntegration {
    private final WebClient personAreaClient;

    public GoalDto getGoalByTelegramName(String username) {
        return getGoalDto(username, "/api/v1/persons/person");

    }

    public GoalDto getGoalByName(String username) {
        return getGoalDto(username, "/api/v1/persons/personByTelegramName");
    }

    private GoalDto getGoalDto(String username, String from) {
        return personAreaClient
                .get()
                .uri(from)
                .header("username", username)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(InnerRequestException.class)
                                .map(body -> new InnerRequestException("Bad request from menu service to the personAreaService:" + clientResponse)))
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(InnerRequestException.class)
                                .map(body -> new InnerRequestException("Something wrong on personAreaService side:" + clientResponse)))
                .bodyToMono(GoalDto.class)
                .block();
    }
}
