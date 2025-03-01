package fr.apsprevoyance.skylift.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.service.SkiLiftService;
import fr.apsprevoyance.skylift.service.SportService;

@Tag(TestTag.CONTROLLER)
@ExtendWith(MockitoExtension.class)
class DispatcherControllerTest {

    private static final Long SPORT_ID = 1L;
    private static final Long DIFFERENT_ID = 2L;
    private static final String SPORT_NAME = "Test Sport";
    private static final String SPORT_DESCRIPTION = "Test Description";
    private static final Season SPORT_SEASON = Season.WINTER;
    private static final String ENTITY_NAME = "Sport";
    private static final String ID_MISMATCH_ERROR = "L'ID de l'URL ne correspond pas à l'ID du sport dans le corps de la requête";

    @Mock
    private SportService sportService;

    @Mock
    private SkiLiftService skiLiftService;

    @InjectMocks
    private DispatcherController dispatcherController;

    private SportDTO validSportDTO;
    private SportDTO createdSportDTO;
    private SportDTO updatedSportDTO;

    @BeforeEach
    void setUp() {
        validSportDTO = new SportDTO();
        validSportDTO.setName(SPORT_NAME);
        validSportDTO.setDescription(SPORT_DESCRIPTION);
        validSportDTO.setSeason(SPORT_SEASON);
        validSportDTO.setActive(true);

        createdSportDTO = new SportDTO();
        createdSportDTO.setId(SPORT_ID);
        createdSportDTO.setName(SPORT_NAME);
        createdSportDTO.setDescription(SPORT_DESCRIPTION);
        createdSportDTO.setSeason(SPORT_SEASON);
        createdSportDTO.setActive(true);

        updatedSportDTO = new SportDTO();
        updatedSportDTO.setId(SPORT_ID);
        updatedSportDTO.setName(SPORT_NAME + " Updated");
        updatedSportDTO.setDescription(SPORT_DESCRIPTION + " Updated");
        updatedSportDTO.setSeason(SPORT_SEASON);
        updatedSportDTO.setActive(false);
    }

    @Test
    void createSport_shouldReturnCreatedSportWithStatus201() {
        when(sportService.createSport(eq(validSportDTO))).thenReturn(createdSportDTO);

        ResponseEntity<SportDTO> response = dispatcherController.createSport(validSportDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdSportDTO, response.getBody());
        verify(sportService).createSport(validSportDTO);
    }

    @Test
    void getAllSports_shouldReturnAllSportsWithStatus200() {
        List<SportDTO> sports = Arrays.asList(createdSportDTO, updatedSportDTO);
        when(sportService.findAllSports()).thenReturn(sports);

        ResponseEntity<List<SportDTO>> response = dispatcherController.getAllSports();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sports, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(sportService).findAllSports();
    }

    @Test
    void getAllSports_withEmptyList_shouldReturnEmptyListWithStatus200() {
        when(sportService.findAllSports()).thenReturn(Collections.emptyList());

        ResponseEntity<List<SportDTO>> response = dispatcherController.getAllSports();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(sportService).findAllSports();
    }

    @Test
    void getSportById_shouldReturnSportWithStatus200() {
        when(sportService.findSportById(SPORT_ID)).thenReturn(createdSportDTO);

        ResponseEntity<SportDTO> response = dispatcherController.getSportById(SPORT_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdSportDTO, response.getBody());
        verify(sportService).findSportById(SPORT_ID);
    }

    @Test
    void getSportById_withNonexistentId_shouldPropagateFindException() {
        when(sportService.findSportById(SPORT_ID))
                .thenThrow(new EntityNotFoundException(ENTITY_NAME, SPORT_ID.toString()));

        assertThrows(EntityNotFoundException.class, () -> {
            dispatcherController.getSportById(SPORT_ID);
        });

        verify(sportService).findSportById(SPORT_ID);
    }

    @Test
    void updateSport_shouldReturnUpdatedSportWithStatus200() {
        when(sportService.updateSport(eq(updatedSportDTO))).thenReturn(updatedSportDTO);

        ResponseEntity<SportDTO> response = dispatcherController.updateSport(SPORT_ID, updatedSportDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSportDTO, response.getBody());
        verify(sportService).updateSport(updatedSportDTO);
    }

    @Test
    void updateSport_withIdMismatch_shouldThrowValidationException() {
        SportDTO mismatchDTO = new SportDTO();
        mismatchDTO.setId(DIFFERENT_ID);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            dispatcherController.updateSport(SPORT_ID, mismatchDTO);
        });

        assertEquals(ENTITY_NAME, exception.getModelName());
        assertEquals(ValidationContextType.REQUEST, exception.getContextType());
        assertEquals(1, exception.getValidationErrors().size());
        assertEquals(ID_MISMATCH_ERROR, exception.getValidationErrors().get(0));
    }

    @Test
    void updateSport_withNonexistentId_shouldPropagateUpdateException() {
        when(sportService.updateSport(eq(updatedSportDTO)))
                .thenThrow(new EntityNotFoundException(ENTITY_NAME, SPORT_ID.toString()));

        assertThrows(EntityNotFoundException.class, () -> {
            dispatcherController.updateSport(SPORT_ID, updatedSportDTO);
        });

        verify(sportService).updateSport(updatedSportDTO);
    }

    @Test
    void deleteSport_shouldReturnNoContentStatus204() {
        doNothing().when(sportService).deleteSport(SPORT_ID);

        ResponseEntity<Void> response = dispatcherController.deleteSport(SPORT_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(sportService).deleteSport(SPORT_ID);
    }

    @Test
    void deleteSport_withNonexistentId_shouldPropagateDeleteException() {
        doThrow(new EntityNotFoundException(ENTITY_NAME, SPORT_ID.toString())).when(sportService).deleteSport(SPORT_ID);

        assertThrows(EntityNotFoundException.class, () -> {
            dispatcherController.deleteSport(SPORT_ID);
        });

        verify(sportService).deleteSport(SPORT_ID);
    }
}