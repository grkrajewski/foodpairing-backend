package com.myapp.foodpairingbackend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SpoonacularConfig {

    @Value("${spoonacular.api.endpoint.prod}")
    private String spoonacularEndpoint;

    @Value("${spoonacular.api.key}")
    private String spoonacularKey;
}
