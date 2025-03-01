package fr.apsprevoyance.skylift.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.apsprevoyance.skylift.constants.SportLabels;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.service.SkiLiftService;
import fr.apsprevoyance.skylift.service.SportService;

@Tag(TestTag.CONTROLLER)
class DispatcherControllerTest {

    private static final class TestConstants {

        static final Long SPORT_VALID_ID = 1L;
        static final Long SPORT_DIFFERENT_ID = 2L;
        static final String SPORT_NAME = "Ski Alpin";
        static final String SPORT_ENTITY_NAME = "Sport";
        static final String SPORT_ID_MISMATCH_ERROR = "The URL ID does not match the ID of the sport in the request body";

        static final Long SKI_LIFT_VALID_ID = 789L;
        static final Long SKI_LIFT_DIFFERENT_ID = 101112L;
        static final String SKI_LIFT_NAME = "Télésiège des Marmottes";
        static final String SKI_LIFT_ENTITY_NAME = "SkiLift";
        static final String SKI_LIFT_ID_MISMATCH_ERROR = "The URL ID does not match the upstream ID in the request body";
    }

    private SportService sportService;
    private SkiLiftService skiLiftService;
    private DispatcherController dispatcherController;

    @BeforeEach
    void setUp() {
        sportService = mock(SportService.class);
        skiLiftService = mock(SkiLiftService.class);
        dispatcherController = new DispatcherController(sportService, skiLiftService);
    }

    private SportDTO createValidSportDTO() {
        SportDTO dto = new SportDTO();
        dto.setId(TestConstants.SPORT_VALID_ID);
        dto.setName(TestConstants.SPORT_NAME);
        dto.setDescription("Description du sport");
        dto.setSeason(Season.WINTER);
        dto.setActive(true);
        return dto;
    }

    @Test
    void createSport_shouldDelegateToServiceAndReturnCreatedSport() {
        SportDTO inputDto = createValidSportDTO();
        when(sportService.createSport(any(SportDTO.class))).thenReturn(inputDto);

        ResponseEntity<SportDTO> response = dispatcherController.createSport(inputDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(inputDto, response.getBody());
        verify(sportService).createSport(inputDto);
    }

    @Test
    void findAllSports_shouldDelegateToServiceAndReturnSportsList() {

        List<SportDTO> sports = Collections.singletonList(createValidSportDTO());
        when(sportService.findAllSports()).thenReturn(sports);

        ResponseEntity<List<SportDTO>> response = dispatcherController.getAllSports();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(sportService).findAllSports();
    }

    @Test
    void findSportById_shouldDelegateToServiceAndReturnSport() {

        SportDTO sport = createValidSportDTO();
        when(sportService.findSportById(TestConstants.SPORT_VALID_ID)).thenReturn(sport);

        ResponseEntity<SportDTO> response = dispatcherController.getSportById(TestConstants.SPORT_VALID_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sport, response.getBody());
        verify(sportService).findSportById(TestConstants.SPORT_VALID_ID);
    }

    @Test
    void updateSport_withMatchingId_shouldDelegateToService() {

        SportDTO sportDto = createValidSportDTO();
        when(sportService.updateSport(any(SportDTO.class))).thenReturn(sportDto);

        ResponseEntity<SportDTO> response = dispatcherController.updateSport(TestConstants.SPORT_VALID_ID, sportDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sportDto, response.getBody());
        verify(sportService).updateSport(sportDto);
    }

    @Test
    void updateSport_withMismatchedId_shouldThrowValidationException() {

        SportDTO sportDto = createValidSportDTO();
        sportDto.setId(TestConstants.SPORT_DIFFERENT_ID);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            dispatcherController.updateSport(TestConstants.SPORT_VALID_ID, sportDto);
        });

        assertEquals(TestConstants.SPORT_ENTITY_NAME, exception.getModelName());
        assertEquals(ValidationContextType.REQUEST, exception.getContextType());
        assertEquals(1, exception.getValidationErrors().size());
        assertEquals(TestConstants.SPORT_ID_MISMATCH_ERROR, exception.getValidationErrors().get(0));
    }

    @Test
    void deleteSport_shouldDelegateToService() {

        ResponseEntity<Void> response = dispatcherController.deleteSport(TestConstants.SPORT_VALID_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(sportService).deleteSport(TestConstants.SPORT_VALID_ID);
    }

    // SKI LIFT TESTS
    private SkiLiftDTO createValidSkiLiftDTO() {
        SkiLiftDTO dto = new SkiLiftDTO();
        dto.setId(TestConstants.SKI_LIFT_VALID_ID);
        dto.setName(TestConstants.SKI_LIFT_NAME);
        dto.setType(SkiLiftType.TELESIEGE);
        dto.setStatus(SkiLiftStatus.OPEN);
        dto.setComment("Test Comment");
        dto.setAvailableSports(Set.of(SportLabels.SKI));
        dto.setCommissioningDate(LocalDate.now());
        return dto;
    }

    @Test
    void createSkiLift_shouldDelegateToServiceAndReturnCreatedSkiLift() {

        SkiLiftDTO inputDto = createValidSkiLiftDTO();
        when(skiLiftService.createSkiLift(any(SkiLiftDTO.class))).thenReturn(inputDto);

        ResponseEntity<SkiLiftDTO> response = dispatcherController.createSkiLift(inputDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(inputDto, response.getBody());
        verify(skiLiftService).createSkiLift(inputDto);
    }

    @Test
    void findAllSkiLifts_shouldDelegateToServiceAndReturnSkiLiftsList() {

        List<SkiLiftDTO> skiLifts = Collections.singletonList(createValidSkiLiftDTO());
        when(skiLiftService.findAllSkiLifts()).thenReturn(skiLifts);

        ResponseEntity<List<SkiLiftDTO>> response = dispatcherController.getAllSkiLifts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(skiLiftService).findAllSkiLifts();
    }

    @Test
    void findSkiLiftById_shouldDelegateToServiceAndReturnSkiLift() {

        SkiLiftDTO skiLift = createValidSkiLiftDTO();
        when(skiLiftService.findSkiLiftById(TestConstants.SKI_LIFT_VALID_ID)).thenReturn(skiLift);

        ResponseEntity<SkiLiftDTO> response = dispatcherController.getSkiLiftById(TestConstants.SKI_LIFT_VALID_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(skiLift, response.getBody());
        verify(skiLiftService).findSkiLiftById(TestConstants.SKI_LIFT_VALID_ID);
    }

    @Test
    void updateSkiLift_withMatchingId_shouldDelegateToService() {

        SkiLiftDTO skiLiftDto = createValidSkiLiftDTO();
        when(skiLiftService.updateSkiLift(any(SkiLiftDTO.class))).thenReturn(skiLiftDto);

        ResponseEntity<SkiLiftDTO> response = dispatcherController.updateSkiLift(TestConstants.SKI_LIFT_VALID_ID,
                skiLiftDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(skiLiftDto, response.getBody());
        verify(skiLiftService).updateSkiLift(skiLiftDto);
    }

    @Test
    void updateSkiLift_withMismatchedId_shouldThrowValidationException() {

        SkiLiftDTO skiLiftDto = createValidSkiLiftDTO();
        skiLiftDto.setId(TestConstants.SKI_LIFT_DIFFERENT_ID);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            dispatcherController.updateSkiLift(TestConstants.SKI_LIFT_VALID_ID, skiLiftDto);
        });

        assertEquals(TestConstants.SKI_LIFT_ENTITY_NAME, exception.getModelName());
        assertEquals(ValidationContextType.REQUEST, exception.getContextType());
        assertEquals(1, exception.getValidationErrors().size());
        assertEquals(TestConstants.SKI_LIFT_ID_MISMATCH_ERROR, exception.getValidationErrors().get(0));
    }

    @Test
    void deleteSkiLift_shouldDelegateToService() {

        ResponseEntity<Void> response = dispatcherController.deleteSkiLift(TestConstants.SKI_LIFT_VALID_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(skiLiftService).deleteSkiLift(TestConstants.SKI_LIFT_VALID_ID);
    }
}