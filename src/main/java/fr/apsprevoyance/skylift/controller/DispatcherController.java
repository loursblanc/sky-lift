package fr.apsprevoyance.skylift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.service.SportService;
import fr.apsprevoyance.skylift.validation.OnCreate;

@RestController
@RequestMapping("/api")
@Validated
public class DispatcherController {

    private final SportService sportService;

    public DispatcherController(SportService sportService) {
        this.sportService = sportService;
    }

    @PostMapping("/sports")
    public ResponseEntity<SportDTO> createSport(@Validated(OnCreate.class) @RequestBody SportDTO sportDTO) {
        SportDTO createdSport = sportService.createSport(sportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSport);
    }
}