package ru.findFood.menu.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import ru.findFood.menu.dtos.DishDto;
import ru.findFood.menu.dtos.UpdateDishTimeRequest;
import ru.findFood.menu.exceprions.InnerRequestException;
import ru.findFood.menu.exceprions.NotFoundException;
import ru.findFood.menu.properties.ServicesIntegrationProperties;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableConfigurationProperties(
        {ServicesIntegrationProperties.class}
)
@RequiredArgsConstructor
public class RestaurantsServiceIntegration {

    private final ServicesIntegrationProperties sip;
    private WebClient webClient;

    @Value("${integrations.restaurants-service.url}")
    private String restaurantServiceUrl;

    public List<DishDto> findByCategory(String category, String querySize) {
        Object[] response = getWebClient()
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/api/v1/dishes/limitedAndByCategory")
                                .queryParam("category", category)
                                .queryParam("querySize", querySize)
                                .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(InnerRequestException.class)
                                .map(body -> new InnerRequestException("Bad request from menu service to the restaurantsService:" + clientResponse)))
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(InnerRequestException.class)
                                .map(body -> new InnerRequestException("Something wrong on RestaurantsService side:" + clientResponse)))
                .bodyToMono(Object[].class)
                .block();


        if (response != null) {
            ObjectMapper mapper = new ObjectMapper();
            return Arrays.stream(response)
                    .map(o -> mapper.convertValue(o, DishDto.class))
                    .toList();

        }
        throw new NotFoundException("Couldn't find dishes!");
    }

    public void saveAll(UpdateDishTimeRequest request) {
        getWebClient()
                .post()
                .uri("/api/v1/dishes/updateUsedLastTime")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UpdateDishTimeRequest.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }

    private WebClient getWebClient() {
        if (webClient == null) {
            HttpClient httpClient = HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, sip.getConnectTimeout())
                    .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(sip.getReadTimeout(), TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(sip.getWriteTimeout(), TimeUnit.MILLISECONDS)));

            webClient = WebClient.builder()
                    .baseUrl(restaurantServiceUrl)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
        }
        return webClient;
    }
}
