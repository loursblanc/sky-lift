package fr.apsprevoyance.skylift.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.apsprevoyance.skylift.constants.SportLabels;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.model.SkiLift;

@ExtendWith(MockitoExtension.class)
class SkiLiftRepositoryInMemoryTest {

    @Spy
    private AtomicLong idCounter = new AtomicLong(1);

    @InjectMocks
    private SkiLiftRepositoryInMemory repository;

    @Test
    void should_generate_new_id_when_creating_ski_lift() {
        SkiLift skiLift = createValidSkiLift();

        SkiLift createdSkiLift = repository.create(skiLift);

        assertNotNull(createdSkiLift.getId());
        assertEquals(1, createdSkiLift.getId());
    }

    @Test
    void should_throw_exception_when_ski_lift_has_predefined_id() {
        SkiLift originalSkiLift = createValidSkiLift();
        SkiLift skiLiftWithPreDefinedId = SkiLift.builder().id(1L).name(originalSkiLift.getName())
                .type(originalSkiLift.getType()).status(originalSkiLift.getStatus())
                .availableSports(originalSkiLift.getAvailableSports())
                .commissioningDate(originalSkiLift.getCommissioningDate()).build();

        assertThrows(ValidationException.class, () -> repository.create(skiLiftWithPreDefinedId));
    }

    @Test
    void should_throw_null_pointer_exception_when_ski_lift_is_null() {
        assertThrows(NullPointerException.class, () -> repository.create(null));
    }

    private SkiLift createValidSkiLift() {
        Set<String> sports = new HashSet<>();
        sports.add(SportLabels.SKI);

        return SkiLift.builder().name("Test Lift").type(SkiLiftType.TELESIEGE).status(SkiLiftStatus.OPEN)
                .availableSports(sports).commissioningDate(LocalDate.now()).build();
    }
}