package fr.apsprevoyance.skylift.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.apsprevoyance.skylift.constants.SportLabels;
import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.service.SkiLiftService;
import fr.apsprevoyance.skylift.service.SportService;

@ExtendWith(MockitoExtension.class)
class DispatcherControllerTest {

    @Mock
    private SportService sportService;

    @Mock
    private SkiLiftService skiLiftService;

    @InjectMocks
    private DispatcherController dispatcherController;

    @Test
    void should_create_sport_successfully() {
        // Prepare
        SportDTO inputDto = createValidSportDTO();
        SportDTO expectedDto = createValidSportDTO();
        expectedDto.setId(1L);

        // Stub
        when(sportService.createSport(inputDto)).thenReturn(expectedDto);

        // Execute
        ResponseEntity<SportDTO> response = dispatcherController.createSport(inputDto);

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        SportDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getId());
        assertEquals(inputDto.getName(), responseBody.getName());
    }

    @Test
    void should_create_ski_lift_successfully() {
        // Prepare
        SkiLiftDTO inputDto = createValidSkiLiftDTO();
        SkiLiftDTO expectedDto = createValidSkiLiftDTO();
        expectedDto.setId(1L);

        // Stub
        when(skiLiftService.createSkiLift(inputDto)).thenReturn(expectedDto);

        // Execute
        ResponseEntity<SkiLiftDTO> response = dispatcherController.createSkiLift(inputDto);

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        SkiLiftDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getId());
        assertEquals(inputDto.getName(), responseBody.getName());
    }

    private SportDTO createValidSportDTO() {
        SportDTO sportDTO = new SportDTO();
        sportDTO.setName("Test Sport");
        sportDTO.setSeason(Season.WINTER);
        sportDTO.setActive(true);
        sportDTO.setDescription("Test Description");
        return sportDTO;
    }

    private SkiLiftDTO createValidSkiLiftDTO() {
        SkiLiftDTO skiLiftDTO = new SkiLiftDTO();
        skiLiftDTO.setName("Test Ski Lift");
        skiLiftDTO.setType(SkiLiftType.TELESIEGE);
        skiLiftDTO.setStatus(SkiLiftStatus.OPEN);
        skiLiftDTO.setComment("Test Comment");
        skiLiftDTO.setAvailableSports(Set.of(SportLabels.SKI));
        skiLiftDTO.setCommissioningDate(LocalDate.now());
        return skiLiftDTO;
    }
}