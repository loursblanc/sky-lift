package fr.apsprevoyance.skylift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.SportRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;

@Tag(TestTag.SERVICE)
@ExtendWith(MockitoExtension.class)
class SportServiceImplTest {

    private static final String ENTITY_NAME = "Sport";
    private static final String NULL_SPORT_DTO_MESSAGE = "SportDTO cannot be null";
    private static final String NULL_SPORT_ID_MESSAGE = "Sport ID cannot be null";
    private static final Long SPORT_ID = 1L;
    private static final Long NONEXISTENT_ID = 999L;
    private static final String SPORT_NAME = "Ski Alpin";
    private static final String UPDATED_SPORT_NAME = "Ski Alpin Modifié";
    private static final String SPORT_DESCRIPTION = "Description du ski alpin";
    private static final Season SPORT_SEASON = Season.WINTER;
    private static final String SPORT_1 = "Ski";
    private static final String SPORT_2 = "Snowboard";
    private static final String SPORT_3 = "Invalid Sport";

    @Mock
    private SportRepository sportRepository;

    @Mock
    private SportMapper sportMapper;

    @Mock
    private ModelValidationService modelValidationService;

    @InjectMocks
    private SportServiceImpl sportService;

    private SportDTO sportDTO;
    private Sport sport;
    private Sport createdSport;
    private SportDTO mappedSportDTO;

    @BeforeEach
    void setUp() {
        sportDTO = new SportDTO();
        sportDTO.setName(SPORT_NAME);
        sportDTO.setDescription(SPORT_DESCRIPTION);
        sportDTO.setSeason(SPORT_SEASON);
        sportDTO.setActive(true);

        sport = Sport.builder().name(SPORT_NAME).description(SPORT_DESCRIPTION).season(SPORT_SEASON).active(true)
                .build();

        createdSport = Sport.builder().id(SPORT_ID).name(SPORT_NAME).description(SPORT_DESCRIPTION).season(SPORT_SEASON)
                .active(true).build();

        mappedSportDTO = new SportDTO();
        mappedSportDTO.setId(SPORT_ID);
        mappedSportDTO.setName(SPORT_NAME);
        mappedSportDTO.setDescription(SPORT_DESCRIPTION);
        mappedSportDTO.setSeason(SPORT_SEASON);
        mappedSportDTO.setActive(true);
    }

    @Test
    void createSport_shouldCreateSport() {
        when(sportMapper.toEntityForCreate(sportDTO)).thenReturn(sport);
        when(sportRepository.create(sport)).thenReturn(createdSport);
        when(sportMapper.toDto(createdSport)).thenReturn(mappedSportDTO);

        SportDTO result = sportService.createSport(sportDTO);

        assertNotNull(result);
        assertEquals(mappedSportDTO, result);
        verify(modelValidationService).checkAndThrowIfInvalid(sport, ENTITY_NAME, OnCreate.class);
        verify(sportRepository).create(sport);
        verify(sportMapper).toDto(createdSport);
    }

    @Test
    void createSport_withNullDTO_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            sportService.createSport(null);
        });

        assertEquals(NULL_SPORT_DTO_MESSAGE, exception.getMessage());
    }

    @Test
    void findAllSports_shouldReturnAllSports() {
        List<Sport> sports = Arrays.asList(Sport.builder().id(1L).name("Sport 1").build(),
                Sport.builder().id(2L).name("Sport 2").build());
        List<SportDTO> sportDTOs = Arrays.asList(new SportDTO(), new SportDTO());

        when(sportRepository.findAll()).thenReturn(sports);
        when(sportMapper.toDto(sports.get(0))).thenReturn(sportDTOs.get(0));
        when(sportMapper.toDto(sports.get(1))).thenReturn(sportDTOs.get(1));

        List<SportDTO> result = sportService.findAllSports();

        assertEquals(2, result.size());
        verify(sportRepository).findAll();
        verify(sportMapper, times(2)).toDto(any(Sport.class));
    }

    @Test
    void findAllSports_withEmptyRepository_shouldReturnEmptyList() {
        when(sportRepository.findAll()).thenReturn(Collections.emptyList());

        List<SportDTO> result = sportService.findAllSports();

        assertTrue(result.isEmpty());
        verify(sportRepository).findAll();
        verify(sportMapper, never()).toDto(any(Sport.class));
    }

    @Test
    void findSportById_shouldReturnSport() {
        when(sportRepository.findById(SPORT_ID)).thenReturn(Optional.of(createdSport));
        when(sportMapper.toDto(createdSport)).thenReturn(mappedSportDTO);

        SportDTO result = sportService.findSportById(SPORT_ID);

        assertNotNull(result);
        assertEquals(mappedSportDTO, result);
        verify(sportRepository).findById(SPORT_ID);
        verify(sportMapper).toDto(createdSport);
    }

    @Test
    void findSportById_withNonexistentId_shouldThrowEntityNotFoundException() {
        when(sportRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sportService.findSportById(NONEXISTENT_ID);
        });

        assertEquals(ENTITY_NAME, exception.getEntityName());
        assertEquals(NONEXISTENT_ID.toString(), exception.getIdentifier());
        verify(sportRepository).findById(NONEXISTENT_ID);
        verify(sportMapper, never()).toDto(any(Sport.class));
    }

    @Test
    void findSportById_withNullId_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            sportService.findSportById(null);
        });

        assertEquals(NULL_SPORT_ID_MESSAGE, exception.getMessage());
        verify(sportRepository, never()).findById(anyLong());
    }

    @Test
    void updateSport_shouldUpdateSport() {
        SportDTO updateDTO = new SportDTO();
        updateDTO.setId(SPORT_ID);
        updateDTO.setName(UPDATED_SPORT_NAME);
        updateDTO.setDescription(SPORT_DESCRIPTION);
        updateDTO.setSeason(SPORT_SEASON);

        Sport mappedSport = Sport.builder().id(SPORT_ID).name(UPDATED_SPORT_NAME).description(SPORT_DESCRIPTION)
                .season(SPORT_SEASON).build();

        Sport updatedSport = Sport.builder().id(SPORT_ID).name(UPDATED_SPORT_NAME).description(SPORT_DESCRIPTION)
                .season(SPORT_SEASON).build();

        SportDTO resultDTO = new SportDTO();
        resultDTO.setId(SPORT_ID);
        resultDTO.setName(UPDATED_SPORT_NAME);

        // when(sportRepository.existsById(SPORT_ID)).thenReturn(true);
        when(sportMapper.toEntityForUpdate(updateDTO)).thenReturn(mappedSport);
        when(sportRepository.update(mappedSport)).thenReturn(updatedSport);
        when(sportMapper.toDto(updatedSport)).thenReturn(resultDTO);

        SportDTO result = sportService.updateSport(updateDTO);

        assertNotNull(result);
        assertEquals(resultDTO, result);
        // verify(sportRepository).existsById(SPORT_ID);
        verify(modelValidationService).checkAndThrowIfInvalid(mappedSport, ENTITY_NAME, OnUpdate.class);
        verify(sportRepository).update(mappedSport);
        verify(sportMapper).toDto(updatedSport);
    }

    @Test
    void updateSport_withNullDTO_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            sportService.updateSport(null);
        });

        assertEquals(NULL_SPORT_DTO_MESSAGE, exception.getMessage());
    }

    @Test
    void updateSport_withNullId_shouldThrowNullPointerException() {
        SportDTO updateDTO = new SportDTO();
        updateDTO.setId(null);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            sportService.updateSport(updateDTO);
        });

        assertEquals("Sport ID cannot be null for update", exception.getMessage());
    }

    @Test
    void deleteSport_shouldDeleteSport() {
        sportService.deleteSport(SPORT_ID);

        verify(sportRepository).delete(SPORT_ID);
    }

    @Test
    void deleteSport_withNullId_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            sportService.deleteSport(null);
        });

        assertEquals(NULL_SPORT_ID_MESSAGE, exception.getMessage());
        verify(sportRepository, never()).delete(anyLong());
    }

    @Test
    void sportExists_shouldReturnTrueWhenSportExists() {
        when(sportRepository.existsById(SPORT_ID)).thenReturn(true);

        boolean result = sportService.sportExists(SPORT_ID);

        assertTrue(result);
        verify(sportRepository).existsById(SPORT_ID);
    }

    @Test
    void sportExists_shouldReturnFalseWhenSportDoesNotExist() {
        when(sportRepository.existsById(NONEXISTENT_ID)).thenReturn(false);

        boolean result = sportService.sportExists(NONEXISTENT_ID);

        assertFalse(result);
        verify(sportRepository).existsById(NONEXISTENT_ID);
    }

    @Test
    void sportExists_withNullId_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            sportService.sportExists(null);
        });

        assertEquals(NULL_SPORT_ID_MESSAGE, exception.getMessage());
    }

    @Test
    void areSportsValid_shouldReturnTrueWhenAllSportsAreValid() {
        List<String> sportNames = Arrays.asList(SPORT_1, SPORT_2);
        List<Sport> existingSports = Arrays.asList(Sport.builder().name(SPORT_1).build(),
                Sport.builder().name(SPORT_2).build());

        when(sportRepository.findAll()).thenReturn(existingSports);

        boolean result = sportService.areSportsValid(sportNames);

        assertTrue(result);
        verify(sportRepository).findAll();
    }

    @Test
    void areSportsValid_shouldReturnFalseWhenAnySportIsInvalid() {
        List<String> sportNames = Arrays.asList(SPORT_1, SPORT_3);
        List<Sport> existingSports = Arrays.asList(Sport.builder().name(SPORT_1).build(),
                Sport.builder().name(SPORT_2).build());

        when(sportRepository.findAll()).thenReturn(existingSports);

        boolean result = sportService.areSportsValid(sportNames);

        assertFalse(result);
        verify(sportRepository).findAll();
    }

    @Test
    void areSportsValid_shouldReturnFalseWhenSportListIsNull() {
        boolean result = sportService.areSportsValid(null);

        assertFalse(result);
        verify(sportRepository, never()).findAll();
    }

    @Test
    void areSportsValid_shouldReturnFalseWhenSportListIsEmpty() {
        boolean result = sportService.areSportsValid(new ArrayList<>());

        assertFalse(result);
        verify(sportRepository, never()).findAll();
    }
}