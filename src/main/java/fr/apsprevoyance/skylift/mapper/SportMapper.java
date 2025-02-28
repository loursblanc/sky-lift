package fr.apsprevoyance.skylift.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.apsprevoyance.skylift.dto.SportCreateDTO;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.model.Sport;

@Mapper(componentModel = "spring")
public interface SportMapper {

    SportDTO toDto(Sport sport);

    Sport toEntity(SportDTO dto);

    @Mapping(target = "id", ignore = true)
    Sport toEntity(SportCreateDTO createDto);
}