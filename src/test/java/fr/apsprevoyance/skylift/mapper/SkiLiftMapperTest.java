package fr.apsprevoyance.skylift.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.model.Sport;

class SkiLiftMapperTest {

    private SkiLiftMapper skiLiftMapper;

    @BeforeEach
    void setUp() {
        skiLiftMapper = Mappers.getMapper(SkiLiftMapper.class);
    }

    private SkiLiftDTO createValidSkiLiftDTO() {
        SkiLiftDTO dto = new SkiLiftDTO();
        dto.setId(1L);
        dto.setName("Télésiège des Marmottes");
        dto.setType(SkiLiftType.TELESIEGE);
        dto.setStatus(SkiLiftStatus.OPEN);
        dto.setComment("Test Comment");

        Sport skiSport = Sport.builder().name("SKI").season(Season.WINTER).build();

        dto.setAvailableSports(Set.of(skiSport));
        dto.setCommissioningDate(LocalDate.now());
        return dto;
    }

    @Test
    void testToEntityForCreate_shouldMapDtoToEntityCorrectly() {
        // Arrange
        SkiLiftDTO dto = createValidSkiLiftDTO();

        // Act
        SkiLift skiLift = skiLiftMapper.toEntityForCreate(dto);

        // Assert
        assertNotNull(skiLift);
        assertEquals(dto.getName(), skiLift.getName());
        assertEquals(dto.getType(), skiLift.getType());
        assertEquals(dto.getStatus(), skiLift.getStatus());
        assertEquals(dto.getComment(), skiLift.getComment());
        assertEquals(dto.getCommissioningDate(), skiLift.getCommissioningDate());

        // Vérifier les sports
        assertNotNull(skiLift.getAvailableSports());
        assertEquals(1, skiLift.getAvailableSports().size());

        Sport mappedSport = skiLift.getAvailableSports().iterator().next();
        assertEquals(dto.getAvailableSports().iterator().next().getName(), mappedSport.getName());
    }

    @Test
    void testToDto_shouldMapEntityToDtoCorrectly() {
        // Arrange
        Sport skiSport = Sport.builder().name("SKI").season(Season.WINTER).build();

        SkiLift skiLift = SkiLift.builder().id(1L).name("Télésiège des Marmottes").type(SkiLiftType.TELESIEGE)
                .status(SkiLiftStatus.OPEN).comment("Test Comment").availableSports(Set.of(skiSport))
                .commissioningDate(LocalDate.now()).build();

        // Act
        SkiLiftDTO dto = skiLiftMapper.toDto(skiLift);

        // Assert
        assertNotNull(dto);
        assertEquals(skiLift.getId(), dto.getId());
        assertEquals(skiLift.getName(), dto.getName());
        assertEquals(skiLift.getType(), dto.getType());
        assertEquals(skiLift.getStatus(), dto.getStatus());
        assertEquals(skiLift.getComment(), dto.getComment());
        assertEquals(skiLift.getCommissioningDate(), dto.getCommissioningDate());

        // Vérifier les sports
        assertNotNull(dto.getAvailableSports());
        assertEquals(1, dto.getAvailableSports().size());

        Sport mappedSport = dto.getAvailableSports().iterator().next();
        assertEquals(skiLift.getAvailableSports().iterator().next().getName(), mappedSport.getName());
    }

    @Test
    void testDtoToBuilderForCreate_shouldCreateBuilderCorrectly() {
        // Arrange
        SkiLiftDTO dto = createValidSkiLiftDTO();

        // Act
        SkiLift.Builder builder = skiLiftMapper.dtoToBuilderForCreate(dto);
        SkiLift skiLift = builder.build();

        // Assert
        assertNotNull(skiLift);
        assertEquals(dto.getName(), skiLift.getName());
        assertEquals(dto.getType(), skiLift.getType());
        assertEquals(dto.getStatus(), skiLift.getStatus());
        assertEquals(dto.getComment(), skiLift.getComment());
        assertEquals(dto.getCommissioningDate(), skiLift.getCommissioningDate());

        // Vérifier les sports
        assertNotNull(skiLift.getAvailableSports());
        assertEquals(1, skiLift.getAvailableSports().size());

        Sport mappedSport = skiLift.getAvailableSports().iterator().next();
        assertEquals(dto.getAvailableSports().iterator().next().getName(), mappedSport.getName());
    }
}