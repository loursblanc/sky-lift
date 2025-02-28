package fr.apsprevoyance.skylift.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.SportRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;

@Service
public class SportServiceImpl implements SportService {

    private static final Logger log = LoggerFactory.getLogger(SportServiceImpl.class);
    private final SportRepository sportRepository;
    private final SportMapper sportMapper;
    private final ModelValidationService modelValidationService;

    public SportServiceImpl(SportRepository sportRepository, SportMapper sportMapper,
            ModelValidationService modelValidationService) {
        this.sportRepository = sportRepository;
        this.sportMapper = sportMapper;
        this.modelValidationService = modelValidationService;
    }

    public SportDTO createSport(SportDTO sportDTO) {
        Objects.requireNonNull(sportDTO, "SportDTO cannot be null");

        Sport sport = sportMapper.toEntityForCreate(sportDTO);
        modelValidationService.checkAndThrowIfInvalid(sport, "Sport");

        Sport createdSport = sportRepository.create(sport);

        return sportMapper.toDto(createdSport);
    }
}
