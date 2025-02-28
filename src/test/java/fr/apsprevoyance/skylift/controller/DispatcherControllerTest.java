package fr.apsprevoyance.skylift.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.service.SportService;

class DispatcherControllerTest {

    private SportService sportService;
    private DispatcherController dispatcherController;

    private static class SportServiceStub implements SportService {
        @Override
        public SportDTO createSport(SportDTO sportDTO) {
            if (sportDTO == null) {
                throw new NullPointerException("Sport DTO cannot be null");
            }

            SportDTO savedDto = new SportDTO();
            savedDto.setId(TestConstants.Sport.VALID_ID);
            savedDto.setName(sportDTO.getName());
            savedDto.setSeason(sportDTO.getSeason());
            savedDto.setActive(sportDTO.isActive());
            savedDto.setDescription(sportDTO.getDescription());

            return savedDto;
        }

    }

    @BeforeEach
    void setUp() {
        sportService = new SportServiceStub();
        dispatcherController = new DispatcherController(sportService);
    }

    @Test
    void createSport_shouldReturnCreatedSport() {

        SportDTO inputDto = new SportDTO();
        inputDto.setName(TestConstants.Sport.VALID_NAME);
        inputDto.setSeason(TestConstants.Sport.VALID_SEASON);
        inputDto.setActive(TestConstants.Sport.VALID_ACTIVE);
        inputDto.setDescription(TestConstants.Sport.VALID_DESCRIPTION);

        ResponseEntity<SportDTO> response = dispatcherController.createSport(inputDto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());

        SportDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(TestConstants.Sport.VALID_ID, responseBody.getId());
        assertEquals(TestConstants.Sport.VALID_NAME, responseBody.getName());
        assertEquals(TestConstants.Sport.VALID_SEASON, responseBody.getSeason());
        assertEquals(TestConstants.Sport.VALID_ACTIVE, responseBody.isActive());
        assertEquals(TestConstants.Sport.VALID_DESCRIPTION, responseBody.getDescription());
    }

    @Test
    void createSport_withNullDto_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            dispatcherController.createSport(null);
        });
    }

    @Test
    void createSport_withMinimalSportDTO_shouldCreateSport() {
        SportDTO minimalDto = new SportDTO();
        minimalDto.setName(TestConstants.Sport.VALID_NAME);
        minimalDto.setSeason(Season.WINTER);

        ResponseEntity<SportDTO> response = dispatcherController.createSport(minimalDto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());

        SportDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(TestConstants.Sport.VALID_ID, responseBody.getId());
        assertEquals(TestConstants.Sport.VALID_NAME, responseBody.getName());
        assertEquals(Season.WINTER, responseBody.getSeason());
    }
}