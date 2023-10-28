package ru.findFood.menu.integrations;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.findFood.menu.dtos.GoalDto;
import ru.findFood.menu.exceprions.InnerRequestException;
import ru.findFood.menu.properties.ServicesIntegrationProperties;

import java.util.concurrent.TimeUnit;

@Component
@EnableConfigurationProperties(
        {ServicesIntegrationProperties.class}
)
@RequiredArgsConstructor
public class PersonAreaServiceIntegration {

    private final ServicesIntegrationProperties sip;

    private WebClient webClient;


    @Value("${integrations.person-service.url}")
    private String personServiceUrl;


    public GoalDto getGoalByTelegramName(String username) {
        return getGoalDto(username, "/api/v1/persons/person");

    }

    public GoalDto getGoalByName(String username) {

        return getGoalDto(username, "/api/v1/goals/app/");
    }

    private GoalDto getGoalDto(String username, String from) {
        return getWebClient()
                .get()
                .uri(personServiceUrl + from + username)
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

    private WebClient getWebClient() {
        if (webClient == null) {
            HttpClient httpClient = HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, sip.getConnectTimeout())
                    .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(sip.getReadTimeout(), TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(sip.getWriteTimeout(), TimeUnit.MILLISECONDS)));
            webClient = WebClient.builder()
                    .baseUrl(personServiceUrl)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
        }
        return webClient;
    }
}
