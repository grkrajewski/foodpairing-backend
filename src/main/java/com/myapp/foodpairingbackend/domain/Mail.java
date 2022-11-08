package com.myapp.foodpairingbackend.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Mail {

    private final String mailTo;
    private final String subject;
    private final String message;
}
