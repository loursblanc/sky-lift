package fr.apsprevoyance.skylift.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.service.SkiLiftService;
import fr.apsprevoyance.skylift.service.SportService;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;

@RestController
@RequestMapping("/api")
@Validated
public class DispatcherController {

    private static final String SPORT_ENTITY_NAME = "Sport";
    private static final String SPORT_ID_MISMATCH_ERROR = "L'ID de l'URL ne correspond pas à l'ID du sport dans le corps de la requête";
    private static final String SKILIFT_ENTITY_NAME = "SkiLift";
    private static final String SKILIFT_ID_MISMATCH_ERROR = "L'ID de l'URL ne correspond pas à l'ID de la remontée dans le corps de la requête";

    private final SportService sportService;
    private final SkiLiftService skiLiftService;

    public DispatcherController(SportService sportService, SkiLiftService skiLiftService) {
        this.sportService = sportService;
        this.skiLiftService = skiLiftService;
    }

    @PostMapping("/sports")
    public ResponseEntity<SportDTO> createSport(@Validated(OnCreate.class) @RequestBody SportDTO sportDTO) {
        SportDTO createdSport = sportService.createSport(sportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSport);
    }

    @GetMapping("/sports")
    public ResponseEntity<List<SportDTO>> getAllSports() {
        List<SportDTO> sports = sportService.findAllSports();
        return ResponseEntity.ok(sports);
    }

    @GetMapping("/sports/{id}")
    public ResponseEntity<SportDTO> getSportById(@PathVariable Long id) {
        SportDTO sport = sportService.findSportById(id);
        return ResponseEntity.ok(sport);
    }

    @PutMapping("/sports/{id}")
    public ResponseEntity<SportDTO> updateSport(@PathVariable Long id,
            @Validated(OnUpdate.class) @RequestBody SportDTO sportDTO) {
        if (!id.equals(sportDTO.getId())) {
            throw new ValidationException(SPORT_ENTITY_NAME, ValidationContextType.REQUEST, SPORT_ID_MISMATCH_ERROR);
        }

        SportDTO updatedSport = sportService.updateSport(sportDTO);
        return ResponseEntity.ok(updatedSport);
    }

    @DeleteMapping("/sports/{id}")
    public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ski-lifts")
    public ResponseEntity<SkiLiftDTO> createSkiLift(@Validated(OnCreate.class) @RequestBody SkiLiftDTO skiLiftDTO) {
        SkiLiftDTO createdSkiLift = skiLiftService.createSkiLift(skiLiftDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkiLift);
    }

    @GetMapping("/ski-lifts")
    public ResponseEntity<List<SkiLiftDTO>> getAllSkiLifts() {
        List<SkiLiftDTO> skiLifts = skiLiftService.findAllSkiLifts();
        return ResponseEntity.ok(skiLifts);
    }

    @GetMapping("/ski-lifts/{id}")
    public ResponseEntity<SkiLiftDTO> getSkiLiftById(@PathVariable Long id) {
        SkiLiftDTO skiLift = skiLiftService.findSkiLiftById(id);
        return ResponseEntity.ok(skiLift);
    }

    @PutMapping("/ski-lifts/{id}")
    public ResponseEntity<SkiLiftDTO> updateSkiLift(@PathVariable Long id,
            @Validated(OnUpdate.class) @RequestBody SkiLiftDTO skiLiftDTO) {
        if (!id.equals(skiLiftDTO.getId())) {
            throw new ValidationException(SKILIFT_ENTITY_NAME, ValidationContextType.REQUEST,
                    SKILIFT_ID_MISMATCH_ERROR);
        }

        SkiLiftDTO updatedSkiLift = skiLiftService.updateSkiLift(skiLiftDTO);
        return ResponseEntity.ok(updatedSkiLift);
    }

    @DeleteMapping("/ski-lifts/{id}")
    public ResponseEntity<Void> deleteSkiLift(@PathVariable Long id) {
        skiLiftService.deleteSkiLift(id);
        return ResponseEntity.noContent().build();
    }
}