package fr.apsprevoyance.skylift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.mapper.SkiLiftMapper;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.repository.SkiLiftRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;

@Tag(TestTag.SERVICE)
@ExtendWith(MockitoExtension.class)
class SkiLiftServiceImplTest {

    private static final class TestConstants {
        static final String ENTITY_NAME = "SkiLift";
        static final Long VALID_SKI_LIFT_ID = 1L;
        static final Long NONEXISTENT_ID = 999L;
        static final String VALID_SKI_LIFT_NAME = "Télésiège des Alpes";
        static final String UPDATED_SKI_LIFT_NAME = "Télésiège Modifié";
        static final String NULL_SKI_LIFT_DTO_MESSAGE = "skiLiftDTO cannot be null";
        static final String NULL_SKI_LIFT_ID_MESSAGE = "SkiLift ID cannot be null";
        static final String NULL_SKI_LIFT_ID_FOR_UPDATE_MESSAGE = "SkiLift ID cannot be null for update";
    }

    @Mock
    private SkiLiftRepository skiLiftRepository;

    @Mock
    private SkiLiftMapper skiLiftMapper;

    @Mock
    private ModelValidationService modelValidationService;

    @InjectMocks
    private SkiLiftServiceImpl skiLiftService;

    private SkiLiftDTO createValidSkiLiftDTO() {
        SkiLiftDTO skiLiftDTO = new SkiLiftDTO();
        skiLiftDTO.setName(TestConstants.VALID_SKI_LIFT_NAME);
        skiLiftDTO.setType(SkiLiftType.TELESIEGE);
        skiLiftDTO.setStatus(SkiLiftStatus.OPEN);
        skiLiftDTO.setComment("Test Comment");
        skiLiftDTO.setAvailableSports(Set.of());
        skiLiftDTO.setCommissioningDate(LocalDate.now());
        return skiLiftDTO;
    }

    private SkiLift createValidSkiLift() {
        return SkiLift.builder().name(TestConstants.VALID_SKI_LIFT_NAME).type(SkiLiftType.TELESIEGE)
                .status(SkiLiftStatus.OPEN).comment("Test Comment").availableSports(Set.of())
                .commissioningDate(LocalDate.now()).build();
    }

    private SkiLift createValidSkiLiftWithId() {
        return SkiLift.builder().id(TestConstants.VALID_SKI_LIFT_ID).name(TestConstants.VALID_SKI_LIFT_NAME)
                .type(SkiLiftType.TELESIEGE).status(SkiLiftStatus.OPEN).comment("Test Comment")
                .availableSports(Set.of()).commissioningDate(LocalDate.now()).build();
    }

    @Test
    void createSkiLift_shouldCreateSkiLiftSuccessfully() {
        // Prepare
        SkiLiftDTO inputDto = createValidSkiLiftDTO();
        SkiLift mappedSkiLift = createValidSkiLift();
        SkiLift createdSkiLift = createValidSkiLiftWithId();
        SkiLiftDTO expectedDto = createValidSkiLiftDTO();
        expectedDto.setId(TestConstants.VALID_SKI_LIFT_ID);

        // Stub
        when(skiLiftMapper.toEntityForCreate(inputDto)).thenReturn(mappedSkiLift);
        when(skiLiftRepository.create(mappedSkiLift)).thenReturn(createdSkiLift);
        when(skiLiftMapper.toDto(createdSkiLift)).thenReturn(expectedDto);

        // Execute
        SkiLiftDTO result = skiLiftService.createSkiLift(inputDto);

        // Verify
        assertNotNull(result);
        assertEquals(TestConstants.VALID_SKI_LIFT_ID, result.getId());
        verify(modelValidationService).checkAndThrowIfInvalid(mappedSkiLift, TestConstants.ENTITY_NAME, OnCreate.class);
        verify(skiLiftRepository).create(mappedSkiLift);
    }

    @Test
    void createSkiLift_withNullDTO_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            skiLiftService.createSkiLift(null);
        });

        assertEquals(TestConstants.NULL_SKI_LIFT_DTO_MESSAGE, exception.getMessage());
    }

    @Test
    void findAllSkiLifts_shouldReturnAllSkiLifts() {
        List<SkiLift> skiLifts = Arrays.asList(createValidSkiLiftWithId(), SkiLift.builder().id(2L)
                .name("Autre Télésiège").type(SkiLiftType.TELESKI).status(SkiLiftStatus.CLOSED).build());
        List<SkiLiftDTO> expectedDtos = Arrays.asList(createValidSkiLiftDTO(), new SkiLiftDTO());

        when(skiLiftRepository.findAll()).thenReturn(skiLifts);
        when(skiLiftMapper.toDto(skiLifts.get(0))).thenReturn(expectedDtos.get(0));
        when(skiLiftMapper.toDto(skiLifts.get(1))).thenReturn(expectedDtos.get(1));

        List<SkiLiftDTO> result = skiLiftService.findAllSkiLifts();

        assertEquals(2, result.size());
        verify(skiLiftRepository).findAll();
    }

    @Test
    void findSkiLiftById_shouldReturnSkiLiftWhenExists() {
        SkiLift skiLift = createValidSkiLiftWithId();
        SkiLiftDTO expectedDto = createValidSkiLiftDTO();
        expectedDto.setId(TestConstants.VALID_SKI_LIFT_ID);

        when(skiLiftRepository.findById(TestConstants.VALID_SKI_LIFT_ID)).thenReturn(Optional.of(skiLift));
        when(skiLiftMapper.toDto(skiLift)).thenReturn(expectedDto);

        SkiLiftDTO result = skiLiftService.findSkiLiftById(TestConstants.VALID_SKI_LIFT_ID);

        assertNotNull(result);
        assertEquals(TestConstants.VALID_SKI_LIFT_ID, result.getId());
    }

    @Test
    void findSkiLiftById_withNonexistentId_shouldThrowEntityNotFoundException() {
        when(skiLiftRepository.findById(TestConstants.NONEXISTENT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            skiLiftService.findSkiLiftById(TestConstants.NONEXISTENT_ID);
        });
    }

    @Test
    void findSkiLiftById_withNullId_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            skiLiftService.findSkiLiftById(null);
        });

        assertEquals(TestConstants.NULL_SKI_LIFT_ID_MESSAGE, exception.getMessage());
    }

    @Test
    void updateSkiLift_shouldUpdateSkiLiftSuccessfully() {
        SkiLiftDTO inputDto = createValidSkiLiftDTO();
        inputDto.setId(TestConstants.VALID_SKI_LIFT_ID);
        inputDto.setName(TestConstants.UPDATED_SKI_LIFT_NAME);

        SkiLift mappedSkiLift = SkiLift.builder().id(TestConstants.VALID_SKI_LIFT_ID)
                .name(TestConstants.UPDATED_SKI_LIFT_NAME).type(inputDto.getType()).status(inputDto.getStatus())
                .comment(inputDto.getComment()).availableSports(inputDto.getAvailableSports())
                .commissioningDate(inputDto.getCommissioningDate()).build();

        SkiLift updatedSkiLift = SkiLift.builder().id(TestConstants.VALID_SKI_LIFT_ID)
                .name(TestConstants.UPDATED_SKI_LIFT_NAME).type(inputDto.getType()).status(inputDto.getStatus())
                .comment(inputDto.getComment()).availableSports(inputDto.getAvailableSports())
                .commissioningDate(inputDto.getCommissioningDate()).build();

        SkiLiftDTO expectedDto = createValidSkiLiftDTO();
        expectedDto.setId(TestConstants.VALID_SKI_LIFT_ID);
        expectedDto.setName(TestConstants.UPDATED_SKI_LIFT_NAME);

        when(skiLiftRepository.existsById(TestConstants.VALID_SKI_LIFT_ID)).thenReturn(true);
        when(skiLiftMapper.toEntityForUpdate(inputDto)).thenReturn(mappedSkiLift);
        when(skiLiftRepository.update(mappedSkiLift)).thenReturn(updatedSkiLift);
        when(skiLiftMapper.toDto(updatedSkiLift)).thenReturn(expectedDto);

        SkiLiftDTO result = skiLiftService.updateSkiLift(inputDto);

        assertNotNull(result);
        assertEquals(TestConstants.UPDATED_SKI_LIFT_NAME, result.getName());
        verify(modelValidationService).checkAndThrowIfInvalid(mappedSkiLift, TestConstants.ENTITY_NAME, OnUpdate.class);
    }

    @Test
    void updateSkiLift_withNonexistentId_shouldThrowEntityNotFoundException() {
        SkiLiftDTO inputDto = createValidSkiLiftDTO();
        inputDto.setId(TestConstants.NONEXISTENT_ID);

        when(skiLiftRepository.existsById(TestConstants.NONEXISTENT_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            skiLiftService.updateSkiLift(inputDto);
        });
    }

    @Test
    void deleteSkiLift_shouldDeleteSkiLift() {
        skiLiftService.deleteSkiLift(TestConstants.VALID_SKI_LIFT_ID);

        verify(skiLiftRepository).delete(TestConstants.VALID_SKI_LIFT_ID);
    }

    @Test
    void deleteSkiLift_withNullId_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            skiLiftService.deleteSkiLift(null);
        });

        assertEquals(TestConstants.NULL_SKI_LIFT_ID_MESSAGE, exception.getMessage());
    }

    @Test
    void skiLiftExists_shouldReturnTrueWhenExists() {
        when(skiLiftRepository.existsById(TestConstants.VALID_SKI_LIFT_ID)).thenReturn(true);

        boolean result = skiLiftService.skiLiftExists(TestConstants.VALID_SKI_LIFT_ID);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    void skiLiftExists_shouldReturnFalseWhenNotExists() {
        when(skiLiftRepository.existsById(TestConstants.NONEXISTENT_ID)).thenReturn(false);

        boolean result = skiLiftService.skiLiftExists(TestConstants.NONEXISTENT_ID);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    void skiLiftExists_withNullId_shouldThrowNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            skiLiftService.skiLiftExists(null);
        });

        assertEquals(TestConstants.NULL_SKI_LIFT_ID_MESSAGE, exception.getMessage());
    }
}