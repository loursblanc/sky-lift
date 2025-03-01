package fr.apsprevoyance.skylift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.SportRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;
import fr.apsprevoyance.skylift.validation.OnCreate;

@Tag(TestTag.SERVICE)
@ExtendWith(MockitoExtension.class)
class SportServiceImplTest {

    private static final String SPORT_NAME = "Ski";
    private static final String SPORT_DESCRIPTION = "Winter sport";
    private static final Long SPORT_ID = 1L;
    private static final String MODEL_NAME = "Sport";
    private static final String NULL_SPORT_DTO_MESSAGE = "SportDTO cannot be null";

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

    @BeforeEach
    void setUp() {
        sportDTO = new SportDTO();
        sportDTO.setName(SPORT_NAME);
        sportDTO.setDescription(SPORT_DESCRIPTION);
        sportDTO.setSeason(Season.WINTER);

        sport = Sport.builder().name(SPORT_NAME).description(SPORT_DESCRIPTION).season(Season.WINTER).build();

        createdSport = Sport.builder().id(SPORT_ID).name(SPORT_NAME).description(SPORT_DESCRIPTION)
                .season(Season.WINTER).build();
    }

    @Test
    void createSport_shouldCreateSport() {
        when(sportMapper.toEntityForCreate(sportDTO)).thenReturn(sport);
        when(sportRepository.create(sport)).thenReturn(createdSport);
        when(sportMapper.toDto(createdSport)).thenReturn(sportDTO);

        SportDTO result = sportService.createSport(sportDTO);

        assertNotNull(result);
        assertEquals(sportDTO, result);
        verify(modelValidationService).checkAndThrowIfInvalid(sport, MODEL_NAME, OnCreate.class);
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
    void createSport_whenValidationFails_shouldPropagateException() {
        when(sportMapper.toEntityForCreate(sportDTO)).thenReturn(sport);

        doThrow(new IllegalArgumentException(ErrorMessageConstants.Errors.NAME_NULL)).when(modelValidationService)
                .checkAndThrowIfInvalid(eq(sport), eq(MODEL_NAME), any());

        assertThrows(IllegalArgumentException.class, () -> {
            sportService.createSport(sportDTO);
        });
    }

    @Test
    void createSport_whenRepositoryFails_shouldPropagateException() {
        when(sportMapper.toEntityForCreate(sportDTO)).thenReturn(sport);
        when(sportRepository.create(sport))
                .thenThrow(new RuntimeException(ErrorMessageConstants.General.INTERNAL_ERROR));

        assertThrows(RuntimeException.class, () -> {
            sportService.createSport(sportDTO);
        });
    }
}