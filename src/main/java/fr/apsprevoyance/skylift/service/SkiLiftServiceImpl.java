package fr.apsprevoyance.skylift.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.mapper.SkiLiftMapper;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.repository.SkiLiftRepository;
import fr.apsprevoyance.skylift.validation.ModelValidationService;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;

@Service
public class SkiLiftServiceImpl implements SkiLiftService {

    private static final String ENTITY_NAME = "SkiLift";
    private static final String NULL_SKILIFT_DTO_MESSAGE = "skiLiftDTO cannot be null";
    private static final String NULL_SKILIFT_ID_MESSAGE = "SkiLift ID cannot be null";
    private static final String NULL_SKILIFT_ID_FOR_UPDATE_MESSAGE = "SkiLift ID cannot be null for update";

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
        Objects.requireNonNull(skiLiftDTO, NULL_SKILIFT_DTO_MESSAGE);

        SkiLift skiLift = skiLiftMapper.toEntityForCreate(skiLiftDTO);
        modelValidationService.checkAndThrowIfInvalid(skiLift, ENTITY_NAME, OnCreate.class);

        SkiLift createdSkiLift = skiLiftRepository.create(skiLift);

        return skiLiftMapper.toDto(createdSkiLift);
    }

    @Override
    public List<SkiLiftDTO> findAllSkiLifts() {
        List<SkiLift> skiLifts = skiLiftRepository.findAll();
        return skiLifts.stream().map(skiLiftMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SkiLiftDTO findSkiLiftById(Long id) {
        Objects.requireNonNull(id, NULL_SKILIFT_ID_MESSAGE);

        SkiLift skiLift = skiLiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id.toString()));

        return skiLiftMapper.toDto(skiLift);
    }

    @Override
    public SkiLiftDTO updateSkiLift(SkiLiftDTO skiLiftDTO) {
        Objects.requireNonNull(skiLiftDTO, NULL_SKILIFT_DTO_MESSAGE);
        Objects.requireNonNull(skiLiftDTO.getId(), NULL_SKILIFT_ID_FOR_UPDATE_MESSAGE);

        if (!skiLiftRepository.existsById(skiLiftDTO.getId())) {
            throw new EntityNotFoundException(ENTITY_NAME, skiLiftDTO.getId().toString());
        }

        SkiLift skiLift = skiLiftMapper.toEntityForUpdate(skiLiftDTO);
        modelValidationService.checkAndThrowIfInvalid(skiLift, ENTITY_NAME, OnUpdate.class);

        SkiLift updatedSkiLift = skiLiftRepository.update(skiLift);

        return skiLiftMapper.toDto(updatedSkiLift);
    }

    @Override
    public void deleteSkiLift(Long id) {
        Objects.requireNonNull(id, NULL_SKILIFT_ID_MESSAGE);
        skiLiftRepository.delete(id);
    }

    @Override
    public boolean skiLiftExists(Long id) {
        Objects.requireNonNull(id, NULL_SKILIFT_ID_MESSAGE);
        return skiLiftRepository.existsById(id);
    }
}
