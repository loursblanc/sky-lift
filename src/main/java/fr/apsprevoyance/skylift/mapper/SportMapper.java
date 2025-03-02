package fr.apsprevoyance.skylift.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SportMapper {
    // Méthodes DTO -> Sport
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", defaultValue = "")
    public abstract Sport toEntityForCreate(SportDTO dto);

    @Mapping(target = "description", defaultValue = "")
    public abstract Sport toEntityForUpdate(SportDTO dto);

    public abstract SportDTO toDto(Sport entity);

    // Builders
    public Sport.Builder dtoToBuilderForCreate(SportDTO dto) {
        if (dto == null) {
            return Sport.builder();
        }
        return Sport.builder().name(dto.getName()).description(dto.getDescription()).active(dto.isActive())
                .season(dto.getSeason());
    }

    public Sport.Builder dtoToBuilderForUpdate(SportDTO dto) {
        if (dto == null) {
            return Sport.builder();
        }
        return Sport.builder().id(dto.getId()).name(dto.getName()).description(dto.getDescription())
                .active(dto.isActive()).season(dto.getSeason());
    }

    public SportEntity toEntityForCreate(Sport sport) {
        if (sport == null) {
            return null;
        }
        return new SportEntity(sport.getName(), sport.getDescription(), sport.getSeason(), sport.isActive());
    }

    public SportEntity toEntityForUpdate(Sport sport) {
        if (sport == null) {
            return null;
        }
        return new SportEntity(sport.getId(), sport.getName(), sport.getDescription(), sport.getSeason(),
                sport.isActive());
    }

    // Méthode SportEntity -> Sport
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "season", source = "season")
    @Mapping(target = "active", source = "active")
    public abstract Sport toDomain(SportEntity sportEntity);

    public abstract void updateEntityFromDomain(Sport sport, @MappingTarget SportEntity sportEntity);
}