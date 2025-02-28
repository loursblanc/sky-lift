package fr.apsprevoyance.skylift.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.dto.SportCreateDTO;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.model.Sport;

class SportMapperTest {

    private final SportMapper sportMapper = new SportMapper() {
        @Override
        public SportDTO toDto(Sport sport) {
            if (sport == null) {
                return null;
            }

            SportDTO dto = new SportDTO();
            dto.setId(sport.getId());
            dto.setName(sport.getName());
            dto.setDescription(sport.getDescription());
            dto.setActive(sport.isActive());
            dto.setSeason(sport.getSeason());
            return dto;
        }

        @Override
        public Sport toEntity(SportDTO dto) {
            if (dto == null) {
                return null;
            }

            return Sport.builder().id(dto.getId()).name(dto.getName()).description(dto.getDescription())
                    .active(dto.isActive()).season(dto.getSeason()).build();
        }

        @Override
        public Sport toEntity(SportCreateDTO createDto) {
            if (createDto == null) {
                return null;
            }

            return Sport.builder().name(createDto.getName()).description(createDto.getDescription())
                    .active(createDto.isActive()).season(createDto.getSeason()).build();
        }
    };

    @Test
    void toDto_shouldMapAllFields() {
        Sport sport = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).active(TestConstants.Sport.VALID_ACTIVE)
                .season(TestConstants.Sport.VALID_SEASON).build();

        SportDTO dto = sportMapper.toDto(sport);

        assertEquals(TestConstants.Sport.VALID_ID, dto.getId());
        assertEquals(TestConstants.Sport.VALID_NAME, dto.getName());
        assertEquals(TestConstants.Sport.VALID_DESCRIPTION, dto.getDescription());
        assertEquals(TestConstants.Sport.VALID_ACTIVE, dto.isActive());
        assertEquals(TestConstants.Sport.VALID_SEASON, dto.getSeason());
    }

    @Test
    void toEntity_fromDto_shouldMapAllFields() {
        SportDTO dto = new SportDTO();
        dto.setId(TestConstants.Sport.VALID_ID);
        dto.setName(TestConstants.Sport.VALID_NAME);
        dto.setDescription(TestConstants.Sport.VALID_DESCRIPTION);
        dto.setActive(TestConstants.Sport.VALID_ACTIVE);
        dto.setSeason(TestConstants.Sport.VALID_SEASON);

        Sport entity = sportMapper.toEntity(dto);

        assertEquals(TestConstants.Sport.VALID_ID, entity.getId());
        assertEquals(TestConstants.Sport.VALID_NAME, entity.getName());
        assertEquals(TestConstants.Sport.VALID_DESCRIPTION, entity.getDescription());
        assertEquals(TestConstants.Sport.VALID_ACTIVE, entity.isActive());
        assertEquals(TestConstants.Sport.VALID_SEASON, entity.getSeason());
    }

    @Test
    void toEntity_fromCreateDto_shouldMapAllFieldsExceptId() {
        SportCreateDTO createDto = new SportCreateDTO();
        createDto.setName(TestConstants.Sport.VALID_NAME);
        createDto.setDescription(TestConstants.Sport.VALID_DESCRIPTION);
        createDto.setActive(TestConstants.Sport.VALID_ACTIVE);
        createDto.setSeason(TestConstants.Sport.VALID_SEASON);

        Sport entity = sportMapper.toEntity(createDto);

        assertNotNull(entity);
        assertEquals(TestConstants.Sport.VALID_NAME, entity.getName());
        assertEquals(TestConstants.Sport.VALID_DESCRIPTION, entity.getDescription());
        assertEquals(TestConstants.Sport.VALID_ACTIVE, entity.isActive());
        assertEquals(TestConstants.Sport.VALID_SEASON, entity.getSeason());
    }
}