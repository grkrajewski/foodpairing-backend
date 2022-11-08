package com.myapp.foodpairingbackend.scheduler;

import com.myapp.foodpairingbackend.config.AdminConfig;
import com.myapp.foodpairingbackend.domain.Mail;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import com.myapp.foodpairingbackend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private static final String SUBJECT = "Foodpairing: Number of compositions information";
    private static final String MESSAGE = "Currently in database you got ";
    private final EmailService emailService;
    private final CompositionRepository compositionRepository;
    private final AdminConfig adminConfig;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendEmailWithNumberOfCompositions() {
        long size = compositionRepository.count();
        final String postfix = size == 1 ? "composition" : "compositions";
        final String message = MESSAGE + size + " " + postfix;
        Mail mail = Mail.builder()
                .mailTo(adminConfig.getAdminMail())
                .subject(SUBJECT)
                .message(message)
                .build();
        emailService.send(mail);
    }
}
