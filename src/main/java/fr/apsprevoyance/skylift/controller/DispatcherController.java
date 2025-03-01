package fr.apsprevoyance.skylift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.service.SkiLiftService;
import fr.apsprevoyance.skylift.service.SportService;
import fr.apsprevoyance.skylift.validation.OnCreate;

@RestController
@RequestMapping("/api")
@Validated
public class DispatcherController {

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

    @PostMapping("/ski-lifts")
    public ResponseEntity<SkiLiftDTO> createSkiLift(@Validated(OnCreate.class) @RequestBody SkiLiftDTO skiLiftDTO) {
        System.out.println("je suis dans mon controlleur");
        SkiLiftDTO createdSkiLif = skiLiftService.createSkiLift(skiLiftDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkiLif);
    }

}