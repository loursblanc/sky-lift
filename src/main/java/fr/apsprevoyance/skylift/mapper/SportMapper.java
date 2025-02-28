package fr.apsprevoyance.skylift.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.model.Sport;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", defaultValue = "")
    Sport toEntityForCreate(SportDTO dto);

    @Mapping(target = "description", defaultValue = "")
    Sport toEntityForUpdate(SportDTO dto);

    SportDTO toDto(Sport entity);

    default Sport.Builder dtoToBuilderForCreate(SportDTO dto) {
        if (dto == null) {
            return Sport.builder();
        }

        return Sport.builder().name(dto.getName()).description(dto.getDescription()).active(dto.isActive())
                .season(dto.getSeason());
    }

    default Sport.Builder dtoToBuilderForUpdate(SportDTO dto) {
        if (dto == null) {
            return Sport.builder();
        }

        return Sport.builder().id(dto.getId()).name(dto.getName()).description(dto.getDescription())
                .active(dto.isActive()).season(dto.getSeason());
    }
}