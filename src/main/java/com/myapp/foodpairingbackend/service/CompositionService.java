package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.config.AdminConfig;
import com.myapp.foodpairingbackend.domain.Mail;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompositionService {

    private static final String SUBJECT = "Foodpairing: New Composition appeared";
    private static final String MESSAGE_BEGINNING = "New Composition which ID is ";
    private static final String MESSAGE_ENDING = " has been created in \"FOODPAIRING\" application";
    private final EmailService emailService;
    private final AdminConfig adminConfig;
    private final CompositionRepository compositionRepository;

    public List<Composition> getCompositions() {
        return compositionRepository.findAll();
    }

    public Composition getComposition(final Long compositionId) throws ComponentNotFoundException {
        return compositionRepository.findById(compositionId)
                .orElseThrow(() -> new ComponentNotFoundException(ComponentNotFoundException.COMPOSITION));
    }

    public void deleteComposition(final Long compositionId) throws ComponentNotFoundException {
        try {
            compositionRepository.deleteById(compositionId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.COMPOSITION);
        }
    }

    public Composition saveComposition(final Composition composition) throws ComponentExistsException, IdException {
        if (composition.getId() == null) {
            Composition existingComposition = compositionRepository.findByDrinkId(composition.getDrink().getId());
            if (existingComposition == null) {
                Composition savedComposition = compositionRepository.save(composition);
                Mail mail = Mail.builder()
                        .mailTo(adminConfig.getAdminMail())
                        .subject(SUBJECT)
                        .message(MESSAGE_BEGINNING + composition.getId().toString() + MESSAGE_ENDING)
                        .build();
                emailService.send(mail);
                return savedComposition;
            }
            throw new ComponentExistsException(ComponentExistsException.DRINK_EXISTS);
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public Composition updateComposition(final Composition composition) throws ComponentExistsException, IdException,
            ComponentNotFoundException {
        if (composition.getId() != null && getComposition(composition.getId()) != null) {
            Composition existingComposition = compositionRepository.findByDrinkId(composition.getDrink().getId());
            if (existingComposition == null) {
                return compositionRepository.save(composition);
            }
            throw new ComponentExistsException(ComponentExistsException.DRINK_EXISTS);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}
