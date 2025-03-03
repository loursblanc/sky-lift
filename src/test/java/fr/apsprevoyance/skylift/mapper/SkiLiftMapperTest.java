package fr.apsprevoyance.skylift.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/*
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SkiLiftEntity;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;
*/

@ExtendWith(MockitoExtension.class)
class SkiLiftMapperTest {
    /*
     * @Mock private SkiLiftEntity mockSkiLiftEntity;
     * 
     * @Mock private SportMapper sportMapper;
     * 
     * @InjectMocks private SkiLiftMapper skiLiftMapper;
     * 
     * @Mock private SportEntity mockSportEntity;
     * 
     * private static final Long SKILIFT_ID = 1L; private static final String
     * SKILIFT_NAME = "Télésiège des Marmottes"; private static final SkiLiftType
     * SKILIFT_TYPE = SkiLiftType.TELESIEGE; private static final SkiLiftStatus
     * SKILIFT_STATUS = SkiLiftStatus.OPEN; private static final String COMMENT =
     * "Test Comment"; private static final LocalDate COMMISSIONING_DATE =
     * LocalDate.of(2023, 12, 15);
     * 
     * private static final String SPORT_NAME_SKI = "SKI"; private static final
     * String SPORT_NAME_SNOWBOARD = "SNOWBOARD"; private static final Season
     * SPORT_SEASON = Season.WINTER; private static final Long SPORT_ID_SKI = 1L;
     * private static final Long SPORT_ID_SNOWBOARD = 2L;
     * 
     * @BeforeEach void setUp() {
     * 
     * Sport mockSport =
     * Sport.builder().id(SPORT_ID_SKI).name(SPORT_NAME_SKI).season(SPORT_SEASON).
     * build();
     * when(sportMapper.toDomain(any(SportEntity.class))).thenReturn(mockSport);
     * 
     * SportEntity mockSportEntity = mock(SportEntity.class);
     * when(sportMapper.toEntityForCreate(any(Sport.class))).thenReturn(
     * mockSportEntity);
     * when(sportMapper.toEntityForUpdate(any(Sport.class))).thenReturn(
     * mockSportEntity); }
     * 
     * private SkiLiftDTO createValidSkiLiftDTO() { SkiLiftDTO dto = new
     * SkiLiftDTO(); dto.setId(SKILIFT_ID); dto.setName(SKILIFT_NAME);
     * dto.setType(SKILIFT_TYPE); dto.setStatus(SKILIFT_STATUS);
     * dto.setComment(COMMENT);
     * 
     * Sport skiSport =
     * Sport.builder().id(SPORT_ID_SKI).name(SPORT_NAME_SKI).season(SPORT_SEASON).
     * build();
     * 
     * dto.setAvailableSports(Set.of(skiSport));
     * dto.setCommissioningDate(COMMISSIONING_DATE); return dto; }
     * 
     * private SkiLift createValidSkiLift() { Sport skiSport =
     * Sport.builder().id(SPORT_ID_SKI).name(SPORT_NAME_SKI).season(SPORT_SEASON).
     * build();
     * 
     * return
     * SkiLift.builder().id(SKILIFT_ID).name(SKILIFT_NAME).type(SKILIFT_TYPE).status
     * (SKILIFT_STATUS)
     * .comment(COMMENT).availableSports(Set.of(skiSport)).commissioningDate(
     * COMMISSIONING_DATE).build(); }
     * 
     * private SkiLiftEntity createValidSkiLiftEntity() {
     * 
     * SkiLiftEntity mockEntity = mock(SkiLiftEntity.class);
     * 
     * Set<SportEntity> sports = new HashSet<>(); SportEntity sportEntity =
     * createSportEntity(SPORT_ID_SKI, SPORT_NAME_SKI, SPORT_SEASON, true);
     * sports.add(sportEntity);
     * 
     * when(mockEntity.getId()).thenReturn(SKILIFT_ID);
     * when(mockEntity.getName()).thenReturn(SKILIFT_NAME);
     * when(mockEntity.getType()).thenReturn(SKILIFT_TYPE);
     * when(mockEntity.getStatus()).thenReturn(SKILIFT_STATUS);
     * when(mockEntity.getComment()).thenReturn(COMMENT);
     * when(mockEntity.getAvailableSports()).thenReturn(sports);
     * when(mockEntity.getCommissioningDate()).thenReturn(COMMISSIONING_DATE);
     * 
     * return mockEntity; }
     * 
     * private SportEntity createSportEntity(Long id, String name, Season season,
     * boolean active) {
     * 
     * SportEntity mockSport = mock(SportEntity.class);
     * when(mockSport.getId()).thenReturn(id);
     * when(mockSport.getName()).thenReturn(name);
     * when(mockSport.getSeason()).thenReturn(season);
     * when(mockSport.isActive()).thenReturn(active);
     * 
     * return mockSport; }
     * 
     * @Nested
     * 
     * @DisplayName("Tests for DTO to Model mappings") class DtoToModelMappingTests
     * {
     * 
     * @Test
     * 
     * @DisplayName("toEntityForCreate should map DTO to Model correctly while ignoring ID"
     * ) void testToEntityForCreate_shouldMapDtoToEntityCorrectly() {
     * 
     * SkiLiftDTO dto = createValidSkiLiftDTO();
     * 
     * SkiLift skiLift = skiLiftMapper.toEntityForCreate(dto);
     * 
     * assertNotNull(skiLift); assertNull(skiLift.getId());
     * assertEquals(dto.getName(), skiLift.getName()); assertEquals(dto.getType(),
     * skiLift.getType()); assertEquals(dto.getStatus(), skiLift.getStatus());
     * assertEquals(dto.getComment(), skiLift.getComment());
     * assertEquals(dto.getCommissioningDate(), skiLift.getCommissioningDate());
     * 
     * assertNotNull(skiLift.getAvailableSports()); assertEquals(1,
     * skiLift.getAvailableSports().size());
     * 
     * Sport mappedSport = skiLift.getAvailableSports().iterator().next();
     * assertEquals(dto.getAvailableSports().iterator().next().getName(),
     * mappedSport.getName()); }
     * 
     * @Test
     * 
     * @DisplayName("toEntityForUpdate should map DTO to Model correctly with ID")
     * void testToEntityForUpdate_shouldMapDtoToEntityCorrectly() {
     * 
     * SkiLiftDTO dto = createValidSkiLiftDTO();
     * 
     * SkiLift skiLift = skiLiftMapper.toEntityForUpdate(dto);
     * 
     * assertNotNull(skiLift); assertEquals(dto.getId(), skiLift.getId());
     * assertEquals(dto.getName(), skiLift.getName()); assertEquals(dto.getType(),
     * skiLift.getType()); assertEquals(dto.getStatus(), skiLift.getStatus());
     * assertEquals(dto.getComment(), skiLift.getComment());
     * assertEquals(dto.getCommissioningDate(), skiLift.getCommissioningDate());
     * 
     * assertNotNull(skiLift.getAvailableSports()); assertEquals(1,
     * skiLift.getAvailableSports().size());
     * 
     * Sport mappedSport = skiLift.getAvailableSports().iterator().next();
     * assertEquals(dto.getAvailableSports().iterator().next().getName(),
     * mappedSport.getName()); }
     * 
     * @Test
     * 
     * @DisplayName("dtoToBuilderForCreate should create builder correctly without ID"
     * ) void testDtoToBuilderForCreate_shouldCreateBuilderCorrectly() {
     * 
     * SkiLiftDTO dto = createValidSkiLiftDTO();
     * 
     * SkiLift.Builder builder = skiLiftMapper.dtoToBuilderForCreate(dto); SkiLift
     * skiLift = builder.build();
     * 
     * assertNotNull(skiLift); assertNull(skiLift.getId()); // ID should not be set
     * assertEquals(dto.getName(), skiLift.getName()); assertEquals(dto.getType(),
     * skiLift.getType()); assertEquals(dto.getStatus(), skiLift.getStatus());
     * assertEquals(dto.getComment(), skiLift.getComment());
     * assertEquals(dto.getCommissioningDate(), skiLift.getCommissioningDate());
     * 
     * assertNotNull(skiLift.getAvailableSports()); assertEquals(1,
     * skiLift.getAvailableSports().size());
     * 
     * Sport mappedSport = skiLift.getAvailableSports().iterator().next();
     * assertEquals(dto.getAvailableSports().iterator().next().getName(),
     * mappedSport.getName()); }
     * 
     * @Test
     * 
     * @DisplayName("dtoToBuilderForUpdate should create builder correctly with ID")
     * void testDtoToBuilderForUpdate_shouldCreateBuilderCorrectly() {
     * 
     * SkiLiftDTO dto = createValidSkiLiftDTO();
     * 
     * SkiLift.Builder builder = skiLiftMapper.dtoToBuilderForUpdate(dto); SkiLift
     * skiLift = builder.build();
     * 
     * assertNotNull(skiLift); assertEquals(dto.getId(), skiLift.getId());
     * assertEquals(dto.getName(), skiLift.getName()); assertEquals(dto.getType(),
     * skiLift.getType()); assertEquals(dto.getStatus(), skiLift.getStatus());
     * assertEquals(dto.getComment(), skiLift.getComment());
     * assertEquals(dto.getCommissioningDate(), skiLift.getCommissioningDate());
     * 
     * assertNotNull(skiLift.getAvailableSports()); assertEquals(1,
     * skiLift.getAvailableSports().size());
     * 
     * Sport mappedSport = skiLift.getAvailableSports().iterator().next();
     * assertEquals(dto.getAvailableSports().iterator().next().getName(),
     * mappedSport.getName()); }
     * 
     * @Test
     * 
     * @DisplayName("updateEntityFromDto should update builder correctly") void
     * testUpdateEntityFromDto_shouldUpdateBuilderCorrectly() {
     * 
     * SkiLiftDTO dto = createValidSkiLiftDTO(); SkiLift.Builder builder =
     * SkiLift.builder();
     * 
     * skiLiftMapper.updateEntityFromDto(dto, builder); SkiLift skiLift =
     * builder.build();
     * 
     * assertNotNull(skiLift); assertEquals(dto.getId(), skiLift.getId());
     * assertEquals(dto.getName(), skiLift.getName()); assertEquals(dto.getType(),
     * skiLift.getType()); assertEquals(dto.getStatus(), skiLift.getStatus());
     * assertEquals(dto.getComment(), skiLift.getComment());
     * assertEquals(dto.getCommissioningDate(), skiLift.getCommissioningDate());
     * assertEquals(dto.getAvailableSports(), skiLift.getAvailableSports()); }
     * 
     * @Test
     * 
     * @DisplayName("updateEntityFromDto should handle null DTO gracefully") void
     * testUpdateEntityFromDto_shouldHandleNullDtoGracefully() {
     * 
     * SkiLift.Builder builder =
     * SkiLift.builder().name("Original Name").type(SkiLiftType.TELECABLE);
     * 
     * skiLiftMapper.updateEntityFromDto(null, builder); SkiLift skiLift =
     * builder.build();
     * 
     * assertEquals("Original Name", skiLift.getName());
     * assertEquals(SkiLiftType.TELECABLE, skiLift.getType()); } }
     * 
     * @Nested
     * 
     * @DisplayName("Tests for Model to DTO mappings") class ModelToDtoMappingTests
     * {
     * 
     * @Test
     * 
     * @DisplayName("toDto should map Model to DTO correctly") void
     * testToDto_shouldMapEntityToDtoCorrectly() {
     * 
     * SkiLift skiLift = createValidSkiLift();
     * 
     * SkiLiftDTO dto = skiLiftMapper.toDto(skiLift);
     * 
     * assertNotNull(dto); assertEquals(skiLift.getId(), dto.getId());
     * assertEquals(skiLift.getName(), dto.getName());
     * assertEquals(skiLift.getType(), dto.getType());
     * assertEquals(skiLift.getStatus(), dto.getStatus());
     * assertEquals(skiLift.getComment(), dto.getComment());
     * assertEquals(skiLift.getCommissioningDate(), dto.getCommissioningDate());
     * 
     * assertNotNull(dto.getAvailableSports()); assertEquals(1,
     * dto.getAvailableSports().size());
     * 
     * Sport mappedSport = dto.getAvailableSports().iterator().next();
     * assertEquals(skiLift.getAvailableSports().iterator().next().getName(),
     * mappedSport.getName()); } }
     * 
     * @Nested
     * 
     * @DisplayName("Tests for Entity to Model mappings") class
     * EntityToModelMappingTests {
     * 
     * @Test
     * 
     * @DisplayName("toDomain should map Entity to Model correctly") void
     * testToDomain_shouldMapEntityToModelCorrectly() {
     * 
     * SkiLiftEntity entity = createValidSkiLiftEntity();
     * 
     * SkiLift model = skiLiftMapper.toDomain(entity);
     * 
     * assertNotNull(model); assertEquals(entity.getId(), model.getId());
     * assertEquals(entity.getName(), model.getName());
     * assertEquals(entity.getType(), model.getType());
     * assertEquals(entity.getStatus(), model.getStatus());
     * assertEquals(entity.getComment(), model.getComment());
     * assertEquals(entity.getCommissioningDate(), model.getCommissioningDate());
     * 
     * assertNotNull(model.getAvailableSports());
     * assertEquals(entity.getAvailableSports().size(),
     * model.getAvailableSports().size());
     * 
     * Sport mappedSport = model.getAvailableSports().iterator().next(); SportEntity
     * originalSport = entity.getAvailableSports().iterator().next();
     * assertEquals(originalSport.getId(), mappedSport.getId());
     * assertEquals(originalSport.getName(), mappedSport.getName());
     * assertEquals(originalSport.getSeason(), mappedSport.getSeason()); }
     * 
     * @Test
     * 
     * @DisplayName("toDomainList should map Entity list to Model list correctly")
     * void testToDomainList_shouldMapEntityListToModelList() {
     * 
     * SkiLiftEntity entity1 = createValidSkiLiftEntity();
     * 
     * SportEntity snowboardSportEntity = createSportEntity(SPORT_ID_SNOWBOARD,
     * SPORT_NAME_SNOWBOARD, SPORT_SEASON, true);
     * 
     * Set<SportEntity> sports2 = new HashSet<>();
     * sports2.add(snowboardSportEntity);
     * 
     * SkiLiftEntity entity2 = mock(SkiLiftEntity.class);
     * when(entity2.getId()).thenReturn(2L);
     * when(entity2.getName()).thenReturn("Télésiège de l'Ours");
     * when(entity2.getType()).thenReturn(SkiLiftType.TELESKI);
     * when(entity2.getStatus()).thenReturn(SkiLiftStatus.CLOSED);
     * when(entity2.getComment()).thenReturn("Fermé pour maintenance");
     * when(entity2.getAvailableSports()).thenReturn(sports2);
     * when(entity2.getCommissioningDate()).thenReturn(COMMISSIONING_DATE.minusYears
     * (1));
     * 
     * List<SkiLiftEntity> entities = Arrays.asList(entity1, entity2);
     * 
     * List<SkiLift> models = skiLiftMapper.toDomainList(entities);
     * 
     * assertNotNull(models); assertEquals(2, models.size());
     * 
     * SkiLift model1 = models.get(0); assertEquals(entity1.getId(),
     * model1.getId()); assertEquals(entity1.getName(), model1.getName());
     * 
     * SkiLift model2 = models.get(1); assertEquals(entity2.getId(),
     * model2.getId()); assertEquals(entity2.getName(), model2.getName());
     * assertEquals(entity2.getType(), model2.getType());
     * assertEquals(entity2.getStatus(), model2.getStatus());
     * 
     * Sport mappedSport = model2.getAvailableSports().iterator().next();
     * assertEquals(SPORT_NAME_SNOWBOARD, mappedSport.getName()); }
     * 
     * @Test
     * 
     * @DisplayName("toDomainList should handle empty list correctly") void
     * testToDomainList_shouldHandleEmptyList() {
     * 
     * List<SkiLiftEntity> emptyList = Collections.emptyList();
     * 
     * List<SkiLift> result = skiLiftMapper.toDomainList(emptyList);
     * 
     * assertNotNull(result); assertTrue(result.isEmpty()); }
     * 
     * @Test
     * 
     * @DisplayName("toDomainList should handle null list correctly") void
     * testToDomainList_shouldHandleNullList() {
     * 
     * List<SkiLift> result = skiLiftMapper.toDomainList(null);
     * 
     * assertNotNull(result); assertTrue(result.isEmpty()); } }
     * 
     * @Nested
     * 
     * @DisplayName("Tests for Model to Entity mappings") class
     * ModelToEntityMappingTests {
     * 
     * @Test
     * 
     * @DisplayName("toEntityForCreate should map Model to Entity correctly without ID"
     * ) void testToEntityForCreate_shouldMapModelToEntityCorrectly() {
     * 
     * SkiLift model = createValidSkiLift();
     * 
     * SkiLiftEntity entity = skiLiftMapper.toEntityForCreate(model);
     * 
     * assertNotNull(entity); assertNull(entity.getId()); // ID should be ignored
     * assertEquals(model.getName(), entity.getName());
     * assertEquals(model.getType(), entity.getType());
     * assertEquals(model.getStatus(), entity.getStatus());
     * assertEquals(model.getComment(), entity.getComment());
     * assertEquals(model.getCommissioningDate(), entity.getCommissioningDate());
     * 
     * assertNotNull(entity.getAvailableSports());
     * assertEquals(model.getAvailableSports().size(),
     * entity.getAvailableSports().size());
     * 
     * SportEntity mappedSport = entity.getAvailableSports().iterator().next();
     * Sport originalSport = model.getAvailableSports().iterator().next();
     * assertEquals(originalSport.getId(), mappedSport.getId());
     * assertEquals(originalSport.getName(), mappedSport.getName());
     * assertEquals(originalSport.getSeason(), mappedSport.getSeason()); }
     * 
     * @Test
     * 
     * @DisplayName("toEntityForUpdate should map Model to Entity correctly with ID"
     * ) void testToEntityForUpdate_shouldMapModelToEntityCorrectly() {
     * 
     * SkiLift model = createValidSkiLift();
     * 
     * SkiLiftEntity entity = skiLiftMapper.toEntityForUpdate(model);
     * 
     * assertNotNull(entity); assertEquals(model.getId(), entity.getId());
     * assertEquals(model.getName(), entity.getName());
     * assertEquals(model.getType(), entity.getType());
     * assertEquals(model.getStatus(), entity.getStatus());
     * assertEquals(model.getComment(), entity.getComment());
     * assertEquals(model.getCommissioningDate(), entity.getCommissioningDate());
     * 
     * assertNotNull(entity.getAvailableSports());
     * assertEquals(model.getAvailableSports().size(),
     * entity.getAvailableSports().size());
     * 
     * SportEntity mappedSport = entity.getAvailableSports().iterator().next();
     * Sport originalSport = model.getAvailableSports().iterator().next();
     * assertEquals(originalSport.getId(), mappedSport.getId());
     * assertEquals(originalSport.getName(), mappedSport.getName());
     * assertEquals(originalSport.getSeason(), mappedSport.getSeason()); } }
     */
}