package com.myapp.foodpairingbackend.client;

import com.myapp.foodpairingbackend.config.SpoonacularConfig;
import com.myapp.foodpairingbackend.domain.dto.SpoonacularResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class SpoonacularClient {

    private final RestTemplate restTemplate;
    private final SpoonacularConfig spoonacularConfig;

    public SpoonacularResultDto getDishesFromExternalApiDb(String nameFragment) {
        URI url = UriComponentsBuilder
                .fromHttpUrl(spoonacularConfig.getSpoonacularEndpoint() + "/recipes/search")
                .queryParam("apiKey", spoonacularConfig.getSpoonacularKey())
                .queryParam("query", nameFragment)
                .build()
                .encode()
                .toUri();

        SpoonacularResultDto apiResponse = restTemplate.getForObject(url, SpoonacularResultDto.class);
        return apiResponse;
    }
}
