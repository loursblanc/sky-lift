package fr.apsprevoyance.skylift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.SportRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;

@Tag("repository")
class SportServiceImplTest {

    private static class SportRepositoryInMemory implements SportRepository {
        private final List<Sport> sports = new ArrayList<>();
        private long nextId = 1L;

        @Override
        public Sport create(Sport sport) {
            Sport sportWithId = Sport.builder().id(nextId++).name(sport.getName()).description(sport.getDescription())
                    .active(sport.isActive()).season(sport.getSeason()).build();
            sports.add(sportWithId);
            return sportWithId;
        }

        private SportMapper sportMapper = new SportMapper() {
            @Override
            public Sport toEntityForCreate(SportDTO dto) {
                return Sport.builder().name(dto.getName())
                        .description(dto.getDescription() != null ? dto.getDescription() : "").active(dto.isActive())
                        .season(dto.getSeason()).build();
            }

            @Override
            public SportDTO toDto(Sport entity) {
                SportDTO dto = new SportDTO();
                dto.setId(entity.getId());
                dto.setName(entity.getName());
                dto.setDescription(entity.getDescription());
                dto.setActive(entity.isActive());
                dto.setSeason(entity.getSeason());
                return dto;
            }

            @Override
            public Sport toEntityForUpdate(SportDTO dto) {
                return Sport.builder().id(dto.getId()).name(dto.getName())
                        .description(dto.getDescription() != null ? dto.getDescription() : "").active(dto.isActive())
                        .season(dto.getSeason()).build();
            }

            @Override
            public Sport.Builder dtoToBuilderForCreate(SportDTO dto) {
                if (dto == null) {
                    return Sport.builder();
                }
                return Sport.builder().name(dto.getName()).description(dto.getDescription()).active(dto.isActive())
                        .season(dto.getSeason());
            }

            @Override
            public Sport.Builder dtoToBuilderForUpdate(SportDTO dto) {
                if (dto == null) {
                    return Sport.builder();
                }
                return Sport.builder().id(dto.getId()).name(dto.getName()).description(dto.getDescription())
                        .active(dto.isActive()).season(dto.getSeason());
            }
        };

        private SportRepository sportRepository;
        private ModelValidationService modelValidationService;
        private SportServiceImpl sportService;

        @BeforeEach
        void setUp() {
            sportRepository = new SportRepositoryInMemory();
            modelValidationService = new ModelValidationService();
            sportService = new SportServiceImpl(sportRepository, sportMapper, modelValidationService);
        }

        @Test
        void createSport_shouldCreateSportSuccessfully() {

            SportDTO sportDTO = new SportDTO();
            sportDTO.setName("Ski");
            sportDTO.setSeason(Season.WINTER);
            sportDTO.setActive(true);

            SportDTO createdSport = sportService.createSport(sportDTO);

            assertNotNull(createdSport);
            assertNotNull(createdSport.getId());
            assertEquals("Ski", createdSport.getName());
            assertEquals(Season.WINTER, createdSport.getSeason());
            assertTrue(createdSport.isActive());
        }

        @Test
        void createSport_shouldThrowExceptionWhenDTOIsNull() {

            assertThrows(NullPointerException.class, () -> {
                sportService.createSport(null);
            });
        }

        @Test
        void createSport_shouldSetDefaultDescriptionWhenNull() {

            SportDTO sportDTO = new SportDTO();
            sportDTO.setName("Snowboard");
            sportDTO.setSeason(Season.WINTER);
            sportDTO.setDescription(null);

            SportDTO createdSport = sportService.createSport(sportDTO);

            assertNotNull(createdSport);
            assertEquals("", createdSport.getDescription());
        }

        @Test
        void createSport_shouldUseDefaultActiveStateWhenNotSpecified() {

            SportDTO sportDTO = new SportDTO();
            sportDTO.setName("Sledge");
            sportDTO.setSeason(Season.WINTER);

            SportDTO createdSport = sportService.createSport(sportDTO);

            assertTrue(createdSport.isActive());
        }
    }
}