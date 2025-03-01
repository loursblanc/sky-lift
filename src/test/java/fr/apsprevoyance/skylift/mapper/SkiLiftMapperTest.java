package fr.apsprevoyance.skylift.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import fr.apsprevoyance.skylift.constants.SportLabels;
import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.model.SkiLift;

@Tag("mapper")
public class SkiLiftMapperTest {

    private SkiLiftMapper mapper;

    private static final Long VALID_ID = 1L;
    private static final String VALID_NAME = "Télésiège du Lac";
    private static final SkiLiftType VALID_TYPE = SkiLiftType.TELESIEGE;
    private static final SkiLiftStatus VALID_STATUS = SkiLiftStatus.OPEN;
    private static final String VALID_COMMENT = "Valid comment";
    private static final Set<String> VALID_SPORTS = new HashSet<>(Set.of(SportLabels.SKI, SportLabels.SNOWBOARD));
    private static final LocalDate VALID_DATE = LocalDate.now();

    private SkiLiftDTO dto;
    private SkiLift entity;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(SkiLiftMapper.class);

        dto = new SkiLiftDTO();
        dto.setId(VALID_ID);
        dto.setName(VALID_NAME);
        dto.setType(VALID_TYPE);
        dto.setStatus(VALID_STATUS);
        dto.setComment(VALID_COMMENT);
        dto.setAvailableSports(VALID_SPORTS);
        dto.setCommissioningDate(VALID_DATE);

        entity = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();
    }

    @Test
    public void toEntityForCreate_should_map_dto_to_entity_without_id() {
        SkiLift result = mapper.toEntityForCreate(dto);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(VALID_NAME, result.getName());
        assertEquals(VALID_TYPE, result.getType());
        assertEquals(VALID_STATUS, result.getStatus());
        assertEquals(VALID_COMMENT, result.getComment());
        assertEquals(VALID_SPORTS, result.getAvailableSports());
        assertEquals(VALID_DATE, result.getCommissioningDate());
    }

    @Test
    public void toEntityForCreate_with_null_dto_should_return_null() {
        SkiLift result = mapper.toEntityForCreate(null);

        assertNull(result);
    }

    @Test
    public void toEntityForCreate_with_null_comment_should_use_empty_string() {
        dto.setComment(null);

        SkiLift result = mapper.toEntityForCreate(dto);

        assertNotNull(result);
        assertEquals("", result.getComment());
    }

    @Test
    public void toEntityForUpdate_should_map_dto_to_entity_with_id() {
        SkiLift result = mapper.toEntityForUpdate(dto);

        assertNotNull(result);
        assertEquals(VALID_ID, result.getId());
        assertEquals(VALID_NAME, result.getName());
        assertEquals(VALID_TYPE, result.getType());
        assertEquals(VALID_STATUS, result.getStatus());
        assertEquals(VALID_COMMENT, result.getComment());
        assertEquals(VALID_SPORTS, result.getAvailableSports());
        assertEquals(VALID_DATE, result.getCommissioningDate());
    }

    @Test
    public void toEntityForUpdate_with_null_dto_should_return_null() {
        SkiLift result = mapper.toEntityForUpdate(null);

        assertNull(result);
    }

    @Test
    public void toDto_should_map_entity_to_dto() {
        SkiLiftDTO result = mapper.toDto(entity);

        assertNotNull(result);
        assertEquals(VALID_ID, result.getId());
        assertEquals(VALID_NAME, result.getName());
        assertEquals(VALID_TYPE, result.getType());
        assertEquals(VALID_STATUS, result.getStatus());
        assertEquals(VALID_COMMENT, result.getComment());
        assertEquals(VALID_SPORTS, result.getAvailableSports());
        assertEquals(VALID_DATE, result.getCommissioningDate());
    }

    @Test
    public void toDto_with_null_entity_should_return_null() {
        SkiLiftDTO result = mapper.toDto(null);

        assertNull(result);
    }

    @Test
    public void dtoToBuilderForCreate_should_return_builder_without_id() {
        SkiLift.Builder builder = mapper.dtoToBuilderForCreate(dto);
        SkiLift result = builder.build();

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(VALID_NAME, result.getName());
        assertEquals(VALID_TYPE, result.getType());
        assertEquals(VALID_STATUS, result.getStatus());
        assertEquals(VALID_COMMENT, result.getComment());
        assertEquals(VALID_SPORTS, result.getAvailableSports());
        assertEquals(VALID_DATE, result.getCommissioningDate());
    }

    @Test
    public void dtoToBuilderForCreate_with_null_dto_should_return_empty_builder() {
        SkiLift.Builder builder = mapper.dtoToBuilderForCreate(null);

        assertNotNull(builder);
    }

    @Test
    public void dtoToBuilderForUpdate_should_return_builder_with_id() {
        SkiLift.Builder builder = mapper.dtoToBuilderForUpdate(dto);
        SkiLift result = builder.build();

        assertNotNull(result);
        assertEquals(VALID_ID, result.getId());
        assertEquals(VALID_NAME, result.getName());
        assertEquals(VALID_TYPE, result.getType());
        assertEquals(VALID_STATUS, result.getStatus());
        assertEquals(VALID_COMMENT, result.getComment());
        assertEquals(VALID_SPORTS, result.getAvailableSports());
        assertEquals(VALID_DATE, result.getCommissioningDate());
    }

    @Test
    public void dtoToBuilderForUpdate_with_null_dto_should_return_empty_builder() {
        SkiLift.Builder builder = mapper.dtoToBuilderForUpdate(null);

        assertNotNull(builder);
    }

    @Test
    public void updateEntityFromDto_should_update_builder_fields() {
        SkiLift.Builder builder = SkiLift.builder();

        mapper.updateEntityFromDto(dto, builder);
        SkiLift result = builder.build();

        assertNotNull(result);
        assertEquals(VALID_ID, result.getId());
        assertEquals(VALID_NAME, result.getName());
        assertEquals(VALID_TYPE, result.getType());
        assertEquals(VALID_STATUS, result.getStatus());
        assertEquals(VALID_COMMENT, result.getComment());
        assertEquals(VALID_SPORTS, result.getAvailableSports());
        assertEquals(VALID_DATE, result.getCommissioningDate());
    }

    @Test
    public void updateEntityFromDto_with_null_dto_should_not_modify_builder() {
        SkiLift.Builder builder = SkiLift.builder().name("Original Name").type(SkiLiftType.TELESKI);

        mapper.updateEntityFromDto(null, builder);
        SkiLift result = builder.build();

        assertEquals("Original Name", result.getName());
        assertEquals(SkiLiftType.TELESKI, result.getType());
    }

    @Test
    public void updateEntityFromDto_with_null_fields_should_not_override_existing_values() {
        SkiLift.Builder builder = SkiLift.builder().name("Original Name").type(SkiLiftType.TELESKI)
                .status(SkiLiftStatus.CLOSED);

        SkiLiftDTO partialDto = new SkiLiftDTO();
        partialDto.setStatus(SkiLiftStatus.MAINTENANCE);

        mapper.updateEntityFromDto(partialDto, builder);
        SkiLift result = builder.build();

        assertEquals("Original Name", result.getName());
        assertEquals(SkiLiftType.TELESKI, result.getType());
        assertEquals(SkiLiftStatus.MAINTENANCE, result.getStatus());
    }

    @Test
    public void toDto_should_handle_null_collections() {
        SkiLift entityWithNullCollections = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE)
                .status(VALID_STATUS).comment(VALID_COMMENT).availableSports(null).commissioningDate(VALID_DATE)
                .build();

        SkiLiftDTO result = mapper.toDto(entityWithNullCollections);

        assertNotNull(result);
        assertNotNull(result.getAvailableSports());
        assertTrue(result.getAvailableSports().isEmpty());
    }
}