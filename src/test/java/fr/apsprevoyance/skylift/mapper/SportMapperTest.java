package fr.apsprevoyance.skylift.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;

@Tag("mapper")
class SportMapperTest {

    private SportMapper mapper;
    private SportDTO validDto;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SportMapper.class);

        validDto = new SportDTO();
        validDto.setId(TestConstants.Sport.VALID_ID);
        validDto.setName(TestConstants.Sport.VALID_NAME);
        validDto.setDescription(TestConstants.Sport.VALID_DESCRIPTION);
        validDto.setActive(TestConstants.Sport.VALID_ACTIVE);
        validDto.setSeason(TestConstants.Sport.VALID_SEASON);
    }

    @Test
    void toEntityForCreate_should_map_dto_to_entity_ignoring_id() {
        Sport result = mapper.toEntityForCreate(validDto);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(validDto.getName(), result.getName());
        assertEquals(validDto.getDescription(), result.getDescription());
        assertEquals(validDto.isActive(), result.isActive());
        assertEquals(validDto.getSeason(), result.getSeason());
    }

    @Test
    void toEntityForCreate_should_handle_null_description() {
        validDto.setDescription(null);

        Sport result = mapper.toEntityForCreate(validDto);

        assertNotNull(result);
        assertEquals("", result.getDescription());
    }

    @Test
    void toEntityForUpdate_should_map_dto_to_entity_with_id() {
        Sport result = mapper.toEntityForUpdate(validDto);

        assertNotNull(result);
        assertEquals(validDto.getId(), result.getId());
        assertEquals(validDto.getName(), result.getName());
        assertEquals(validDto.getDescription(), result.getDescription());
        assertEquals(validDto.isActive(), result.isActive());
        assertEquals(validDto.getSeason(), result.getSeason());
    }

    @Test
    void toDto_should_map_entity_to_dto() {
        Sport entity = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).active(TestConstants.Sport.VALID_ACTIVE)
                .season(TestConstants.Sport.VALID_SEASON).build();

        SportDTO result = mapper.toDto(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.isActive(), result.isActive());
        assertEquals(entity.getSeason(), result.getSeason());
    }

    @Test
    void dtoToBuilderForCreate_should_return_builder_without_id() {
        Sport.Builder builder = mapper.dtoToBuilderForCreate(validDto);

        Sport result = builder.build();

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(validDto.getName(), result.getName());
        assertEquals(validDto.getDescription(), result.getDescription());
        assertEquals(validDto.isActive(), result.isActive());
        assertEquals(validDto.getSeason(), result.getSeason());
    }

    @Test
    void dtoToBuilderForCreate_should_handle_null_dto() {
        Sport.Builder builder = mapper.dtoToBuilderForCreate(null);

        assertNotNull(builder);
    }

    @Test
    void dtoToBuilderForUpdate_should_return_builder_with_id() {
        Sport.Builder builder = mapper.dtoToBuilderForUpdate(validDto);

        Sport result = builder.build();

        assertNotNull(result);
        assertEquals(validDto.getId(), result.getId());
        assertEquals(validDto.getName(), result.getName());
        assertEquals(validDto.getDescription(), result.getDescription());
        assertEquals(validDto.isActive(), result.isActive());
        assertEquals(validDto.getSeason(), result.getSeason());
    }

    @Test
    void dtoToBuilderForUpdate_should_handle_null_dto() {
        Sport.Builder builder = mapper.dtoToBuilderForUpdate(null);

        assertNotNull(builder);
    }

    @Test
    void toEntityForCreate_should_map_sport_to_sportEntity_ignoring_id() {
        Sport sport = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).active(TestConstants.Sport.VALID_ACTIVE)
                .season(TestConstants.Sport.VALID_SEASON).build();

        SportEntity result = mapper.toEntityForCreate(sport);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(sport.getName(), result.getName());
        assertEquals(sport.getDescription(), result.getDescription());
        assertEquals(sport.isActive(), result.isActive());
        assertEquals(sport.getSeason(), result.getSeason());
    }

    @Test
    void toEntityForCreate_sport_should_handle_null_sport() {
        SportEntity result = mapper.toEntityForCreate((Sport) null);

        assertNull(result);
    }

    @Test
    void toEntityForUpdate_should_map_sport_to_sportEntity_with_id() {
        Sport sport = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).active(TestConstants.Sport.VALID_ACTIVE)
                .season(TestConstants.Sport.VALID_SEASON).build();

        SportEntity result = mapper.toEntityForUpdate(sport);

        assertNotNull(result);
        assertEquals(sport.getId(), result.getId());
        assertEquals(sport.getName(), result.getName());
        assertEquals(sport.getDescription(), result.getDescription());
        assertEquals(sport.isActive(), result.isActive());
        assertEquals(sport.getSeason(), result.getSeason());
    }

    @Test
    void toEntityForUpdate_should_handle_null_sport() {
        SportEntity result = mapper.toEntityForUpdate((Sport) null);

        assertNull(result);
    }

    @Test
    void toDomain_should_map_sportEntity_to_sport() {
        SportEntity entity = new SportEntity(TestConstants.Sport.VALID_ID, TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);

        Sport result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.isActive(), result.isActive());
        assertEquals(entity.getSeason(), result.getSeason());
    }

    @Test
    void toDomain_should_handle_null_sportEntity() {
        Sport result = mapper.toDomain(null);

        assertNull(result);
    }

    @Test
    void updateEntityFromDomain_should_update_sportEntity_from_sport() {
        Sport sport = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).active(TestConstants.Sport.VALID_ACTIVE)
                .season(TestConstants.Sport.VALID_SEASON).build();

        SportEntity entity = new SportEntity(null, "Old Name", "Old Description", TestConstants.Sport.VALID_SEASON,
                false);

        mapper.updateEntityFromDomain(sport, entity);

        assertEquals(sport.getName(), entity.getName());
        assertEquals(sport.getDescription(), entity.getDescription());
        assertEquals(sport.isActive(), entity.isActive());
        assertEquals(sport.getSeason(), entity.getSeason());
    }
}