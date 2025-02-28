package fr.apsprevoyance.skylift.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.enums.Season;

@Tag("model")
public class SportTest {
    @Test
    public void builder_creates_sport_with_default_active_true() {
        Sport sport = Sport.builder().id(1L).name("Test Sport").season(Season.WINTER).build();

        assertTrue(sport.isActive());
    }

    @Test
    public void builder_sets_description_to_empty_string_when_null() {
        Sport sport = Sport.builder().id(1L).name("Test Sport").description(null).season(Season.WINTER).build();

        assertEquals("", sport.getDescription());
    }

    @Test
    public void equals_returns_true_for_same_properties() {
        Sport sport1 = Sport.builder().id(1L).name("Test Sport").season(Season.WINTER).build();

        Sport sport2 = Sport.builder().id(1L).name("Test Sport").season(Season.WINTER).build();

        assertEquals(sport1, sport2);
        assertEquals(sport1.hashCode(), sport2.hashCode());
    }

    @Test
    public void equals_returns_false_for_different_properties() {
        Sport sport1 = Sport.builder().id(1L).name("Test Sport").season(Season.WINTER).build();

        Sport sport2 = Sport.builder().id(2L).name("Different Sport").season(Season.SUMMER).build();

        assertNotEquals(sport1, sport2);
        assertNotEquals(sport1.hashCode(), sport2.hashCode());
    }

    @Test
    public void hashCode_consistent_for_same_object() {
        Sport sport = Sport.builder().id(1L).name("Test Sport").season(Season.WINTER).build();

        int initialHashCode = sport.hashCode();
        assertEquals(initialHashCode, sport.hashCode());
    }

    @Test
    public void hashCode_different_for_different_objects() {
        Sport sport1 = Sport.builder().id(1L).name("Test Sport").season(Season.WINTER).build();

        Sport sport2 = Sport.builder().id(2L).name("Different Sport").season(Season.SUMMER).build();

        assertNotEquals(sport1.hashCode(), sport2.hashCode());
    }

    @Test
    public void equals_considers_all_fields() {
        Sport sport1 = Sport.builder().id(1L).name("Test Sport").description("Test Description").active(true)
                .season(Season.WINTER).build();

        Sport sport2 = Sport.builder().id(1L).name("Test Sport").description("Test Description").active(true)
                .season(Season.WINTER).build();

        Sport sport3 = Sport.builder().id(1L).name("Different Name").description("Test Description").active(true)
                .season(Season.WINTER).build();

        assertEquals(sport1, sport2);
        assertNotEquals(sport1, sport3);
    }

    @Test
    public void equals_handles_null_comparison() {
        Sport sport = Sport.builder().id(1L).name("Test Sport").season(Season.WINTER).build();

        assertNotEquals(sport, null);
        assertNotEquals(sport, new Object());
    }

    @Test
    public void builder_handles_empty_description() {
        Sport sport = Sport.builder().id(1L).name("Test Sport").description("").season(Season.WINTER).build();

        assertEquals("", sport.getDescription());
    }

    @Test
    public void builder_allows_setting_active_state() {
        Sport activeSport = Sport.builder().id(1L).name("Active Sport").active(false).season(Season.WINTER).build();

        Sport defaultSport = Sport.builder().id(1L).name("Default Sport").season(Season.WINTER).build();

        assertFalse(activeSport.isActive());
        assertTrue(defaultSport.isActive());
    }

    @Test
    public void getters_return_correct_values() {
        Sport sport = Sport.builder().id(1L).name("Test Sport").description("Test Description").active(false)
                .season(Season.SUMMER).build();

        assertEquals(1L, sport.getId());
        assertEquals("Test Sport", sport.getName());
        assertEquals("Test Description", sport.getDescription());
        assertFalse(sport.isActive());
        assertEquals(Season.SUMMER, sport.getSeason());
    }

    @Test
    public void equals_handles_edge_cases() {
        Sport sport1 = Sport.builder().id(Long.MAX_VALUE).name("Max Sport").season(Season.WINTER).build();
        Sport sport2 = Sport.builder().id(Long.MAX_VALUE).name("Max Sport").season(Season.WINTER).build();

        assertEquals(sport1, sport2);
    }

}
