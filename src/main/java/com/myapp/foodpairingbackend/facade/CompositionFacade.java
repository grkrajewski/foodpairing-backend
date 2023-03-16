package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.config.AdminConfig;
import com.myapp.foodpairingbackend.domain.Mail;
import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.exception.*;
import com.myapp.foodpairingbackend.mapper.CompositionMapper;
import com.myapp.foodpairingbackend.service.CompositionService;
import com.myapp.foodpairingbackend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositionFacade {

    private static final String SUBJECT = "Foodpairing: New Composition appeared";
    private static final String MESSAGE_BEGINNING = "New Composition which ID is ";
    private static final String MESSAGE_ENDING = " has been created in \"FOODPAIRING\" application";
    private final CompositionService compositionService;
    private final CompositionMapper compositionMapper;
    private final EmailService emailService;
    private final AdminConfig adminConfig;

    public List<CompositionDto> getCompositions() {
        List<Composition> compositionList = compositionService.getCompositions();
        return compositionMapper.mapToCompositionDtoList(compositionList);
    }

    public CompositionDto getComposition(Long compositionId) throws ComponentNotFoundException {
        Composition composition = compositionService.getComposition(compositionId);
        return compositionMapper.mapToCompositionDto(composition);
    }

    public void deleteComposition(Long compositionId) throws ComponentNotFoundException {
        try {
            compositionService.deleteComposition(compositionId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.COMPOSITION);
        }
    }

    public CompositionDto saveComposition(CompositionDto compositionDto) throws ComponentNotFoundException, IdException,
            ComponentExistsException {
        if (compositionDto.getId() == null) {
            Composition composition = compositionMapper.mapToComposition(compositionDto);
            Composition savedComposition = compositionService.saveComposition(composition);
            Mail mail = Mail.builder()
                    .mailTo(adminConfig.getAdminMail())
                    .subject(SUBJECT)
                    .message(MESSAGE_BEGINNING + composition.getId().toString() + MESSAGE_ENDING)
                    .build();
            emailService.send(mail);
            return compositionMapper.mapToCompositionDto(savedComposition);
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public CompositionDto updateComposition(CompositionDto compositionDto) throws ComponentNotFoundException, IdException,
            ComponentExistsException {
        if (compositionDto.getId() != null && compositionService.getComposition(compositionDto.getId()) != null) {
            Composition composition = compositionMapper.mapToComposition(compositionDto);
            Composition savedComposition = compositionService.saveComposition(composition);
            return compositionMapper.mapToCompositionDto(savedComposition);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}