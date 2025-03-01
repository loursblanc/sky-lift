package fr.apsprevoyance.skylift.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.mapper.SkiLiftMapper;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.repository.SkiLiftRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;
import fr.apsprevoyance.skylift.validation.OnCreate;

@Service
public class SkiLiftServiceImpl implements SkiLiftService {

    private final SkiLiftRepository skiLiftRepository;
    private final SkiLiftMapper skiLiftMapper;
    private final ModelValidationService modelValidationService;

    public SkiLiftServiceImpl(SkiLiftRepository skiLiftRepository, SkiLiftMapper skiLiftMapper,
            ModelValidationService modelValidationService) {
        this.skiLiftRepository = skiLiftRepository;
        this.skiLiftMapper = skiLiftMapper;
        this.modelValidationService = modelValidationService;
    }

    @Override
    public SkiLiftDTO createSkiLift(SkiLiftDTO skiLiftDTO) {
        Objects.requireNonNull(skiLiftDTO, "skiLiftDTO cannot be null");

        SkiLift skiLift = skiLiftMapper.toEntityForCreate(skiLiftDTO);
        modelValidationService.checkAndThrowIfInvalid(skiLift, "SkiLift", OnCreate.class);

        SkiLift createdSkiLift = skiLiftRepository.create(skiLift);

        return skiLiftMapper.toDto(createdSkiLift);
    }

}
