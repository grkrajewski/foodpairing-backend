package com.myapp.foodpairingbackend.scheduler;

import com.myapp.foodpairingbackend.domain.Mail;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import com.myapp.foodpairingbackend.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TestEmailScheduler {

    @MockBean
    CompositionRepository compositionRepository;

    @SpyBean
    EmailService emailService;

    @Autowired
    EmailScheduler emailScheduler;

    @Test
    void shouldSendEmailWithNumberOfCompositions() {
        //Given
        when(compositionRepository.count()).thenReturn(2L);

        //When
        emailScheduler.sendEmailWithNumberOfCompositions();

        //Then
        verify(compositionRepository, times(1)).count();
        verify(emailService, times(1)).send(any(Mail.class));
    }
}
