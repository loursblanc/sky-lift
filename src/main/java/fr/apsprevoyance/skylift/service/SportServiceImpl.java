package fr.apsprevoyance.skylift.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.SportRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;

@Service
public class SportServiceImpl implements SportService {

    private static final Logger log = LoggerFactory.getLogger(SportServiceImpl.class);
    private static final String ENTITY_NAME = "Sport";
    private static final String NULL_SPORT_DTO_MESSAGE = "SportDTO cannot be null";
    private static final String NULL_SPORT_ID_MESSAGE = "Sport ID cannot be null";
    private static final String NULL_SPORT_ID_FOR_UPDATE_MESSAGE = "Sport ID cannot be null for update";

    private final SportRepository sportRepository;
    private final SportMapper sportMapper;
    private final ModelValidationService modelValidationService;

    public SportServiceImpl(SportRepository sportRepository, SportMapper sportMapper,
            ModelValidationService modelValidationService) {
        this.sportRepository = sportRepository;
        this.sportMapper = sportMapper;
        this.modelValidationService = modelValidationService;
    }

    @Override
    public SportDTO createSport(SportDTO sportDTO) {
        Objects.requireNonNull(sportDTO, NULL_SPORT_DTO_MESSAGE);

        Sport sport = sportMapper.toEntityForCreate(sportDTO);
        modelValidationService.checkAndThrowIfInvalid(sport, ENTITY_NAME, OnCreate.class);

        Sport createdSport = sportRepository.create(sport);

        return sportMapper.toDto(createdSport);
    }

    @Override
    public List<SportDTO> findAllSports() {
        List<Sport> sports = sportRepository.findAll();
        return sports.stream().map(sportMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SportDTO findSportById(Long id) {
        Objects.requireNonNull(id, NULL_SPORT_ID_MESSAGE);

        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id.toString()));

        return sportMapper.toDto(sport);
    }

    @Override
    public SportDTO updateSport(SportDTO sportDTO) {
        Objects.requireNonNull(sportDTO, NULL_SPORT_DTO_MESSAGE);
        Objects.requireNonNull(sportDTO.getId(), NULL_SPORT_ID_FOR_UPDATE_MESSAGE);

        if (!sportRepository.existsById(sportDTO.getId())) {
            throw new EntityNotFoundException(ENTITY_NAME, sportDTO.getId().toString());
        }

        Sport sport = sportMapper.toEntityForUpdate(sportDTO);
        modelValidationService.checkAndThrowIfInvalid(sport, ENTITY_NAME, OnUpdate.class);

        Sport updatedSport = sportRepository.update(sport);

        return sportMapper.toDto(updatedSport);
    }

    @Override
    public void deleteSport(Long id) {
        Objects.requireNonNull(id, NULL_SPORT_ID_MESSAGE);
        sportRepository.delete(id);
    }

    @Override
    public boolean sportExists(Long id) {
        Objects.requireNonNull(id, NULL_SPORT_ID_MESSAGE);
        return sportRepository.existsById(id);
    }

    @Override
    public boolean areSportsValid(List<String> sportNames) {
        if (sportNames == null || sportNames.isEmpty()) {
            return false;
        }

        Set<String> existingSportNames = sportRepository.findAll().stream().map(Sport::getName)
                .collect(Collectors.toSet());

        return sportNames.stream().allMatch(existingSportNames::contains);
    }
}