package fr.apsprevoyance.skylift.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.Season;

class SportDTOTest {

    private SportDTO sportDTO;

    @BeforeEach
    void setUp() {
        sportDTO = new SportDTO();
    }

    @Test
    void sport_dto_creation_with_valid_data() {

        sportDTO.setId(TestConstants.Sport.VALID_ID);
        sportDTO.setName(TestConstants.Sport.VALID_NAME);
        sportDTO.setDescription(TestConstants.Sport.VALID_DESCRIPTION);
        sportDTO.setSeason(TestConstants.Sport.VALID_SEASON);
        sportDTO.setActive(TestConstants.Sport.VALID_ACTIVE);

        assertEquals(TestConstants.Sport.VALID_ID, sportDTO.getId());
        assertEquals(TestConstants.Sport.VALID_NAME, sportDTO.getName());
        assertEquals(TestConstants.Sport.VALID_DESCRIPTION, sportDTO.getDescription());
        assertEquals(TestConstants.Sport.VALID_SEASON, sportDTO.getSeason());
        assertEquals(TestConstants.Sport.VALID_ACTIVE, sportDTO.isActive());
    }

    @Test
    void sport_dto_description_is_never_null() {

        sportDTO.setDescription(null);

        assertNotNull(sportDTO.getDescription(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_NEVER_NULL);
        assertEquals("", sportDTO.getDescription(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_REPLACE_BLANK);
    }

    @Test
    void sport_dto_default_active_state() {

        assertTrue(sportDTO.isActive(), TestConstants.ValidationTestMessages.DEFAULT_ACTIVE);
    }

    @Test
    void sport_dto_name_characteristics() {

        sportDTO.setName(TestConstants.Sport.VALID_NAME);

        assertTrue(sportDTO.getName().length() >= ValidationConstants.NAME_MIN_LENGTH,
                ErrorMessageConstants.Errors.NAME_TOO_SHORT);
        assertTrue(sportDTO.getName().length() <= ValidationConstants.NAME_MAX_LENGTH,
                ErrorMessageConstants.Errors.NAME_TOO_LONG);
    }

    @Test
    void sport_dto_handles_different_seasons() {

        for (Season season : Season.values()) {
            sportDTO.setSeason(season);
            assertEquals(season, sportDTO.getSeason());
        }
    }

    @Test
    void sport_dto_can_change_active_state() {

        sportDTO.setActive(false);

        assertFalse(sportDTO.isActive());

        sportDTO.setActive(true);
        assertTrue(sportDTO.isActive());
    }

    @Test
    void sport_dto_id_setter_and_getter() {

        Long testId = TestConstants.Sport.VALID_ID;
        sportDTO.setId(testId);

        assertEquals(testId, sportDTO.getId());
    }
}