package fr.apsprevoyance.skylift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.apsprevoyance.skylift.constants.SportLabels;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.mapper.SkiLiftMapper;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.repository.SkiLiftRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;
import fr.apsprevoyance.skylift.validation.OnCreate;

@Tag(TestTag.SERVICE)
@ExtendWith(MockitoExtension.class)
class SkiLiftServiceImplTest {
    @Mock
    private SkiLiftRepository skiLiftRepository;

    @Mock
    private SkiLiftMapper skiLiftMapper;

    @Mock
    private ModelValidationService modelValidationService;

    @InjectMocks
    private SkiLiftServiceImpl skiLiftService;

    @Test
    void should_throw_exception_when_ski_lift_dto_is_null() {
        assertThrows(NullPointerException.class, () -> skiLiftService.createSkiLift(null),
                "Creating ski lift with null DTO should throw NullPointerException");
    }

    @Test
    void should_create_ski_lift_successfully() {
        // Prepare
        SkiLiftDTO inputDto = createValidSkiLiftDTO();
        SkiLift mappedSkiLift = createValidSkiLift();
        SkiLift createdSkiLift = createValidSkiLiftWithId();
        SkiLiftDTO expectedDto = createValidSkiLiftDTO();
        expectedDto.setId(1L);

        // Stub
        when(skiLiftMapper.toEntityForCreate(inputDto)).thenReturn(mappedSkiLift);
        when(skiLiftRepository.create(mappedSkiLift)).thenReturn(createdSkiLift);
        when(skiLiftMapper.toDto(createdSkiLift)).thenReturn(expectedDto);

        // Execute
        SkiLiftDTO result = skiLiftService.createSkiLift(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(modelValidationService).checkAndThrowIfInvalid(mappedSkiLift, "SkiLift", OnCreate.class);
        verify(skiLiftRepository).create(mappedSkiLift);
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

    private SkiLift createValidSkiLift() {
        return SkiLift.builder().name("Test Ski Lift").type(SkiLiftType.TELESIEGE).status(SkiLiftStatus.OPEN)
                .comment("Test Comment").availableSports(Set.of(SportLabels.SKI)).commissioningDate(LocalDate.now())
                .build();
    }

    private SkiLift createValidSkiLiftWithId() {
        return SkiLift.builder().id(1L).name("Test Ski Lift").type(SkiLiftType.TELESIEGE).status(SkiLiftStatus.OPEN)
                .comment("Test Comment").availableSports(Set.of(SportLabels.SKI)).commissioningDate(LocalDate.now())
                .build();
    }
}