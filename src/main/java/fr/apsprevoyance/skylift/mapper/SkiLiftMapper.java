package fr.apsprevoyance.skylift.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.repository.entity.SkiLiftEntity;

@Component
@Mapper(componentModel = "spring", uses = {
        SportMapper.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SkiLiftMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comment", defaultValue = "")
    SkiLift toEntityForCreate(SkiLiftDTO dto);

    @Mapping(target = "comment", defaultValue = "")
    SkiLift toEntityForUpdate(SkiLiftDTO dto);

    SkiLiftDTO toDto(SkiLift entity);

    default SkiLift.Builder dtoToBuilderForCreate(SkiLiftDTO dto) {
        if (dto == null) {
            return SkiLift.builder();
        }

        return SkiLift.builder().name(dto.getName()).type(dto.getType()).status(dto.getStatus())
                .comment(dto.getComment()).availableSports(dto.getAvailableSports())
                .commissioningDate(dto.getCommissioningDate());
    }

    default SkiLift.Builder dtoToBuilderForUpdate(SkiLiftDTO dto) {
        if (dto == null) {
            return SkiLift.builder();
        }

        return SkiLift.builder().id(dto.getId()).name(dto.getName()).type(dto.getType()).status(dto.getStatus())
                .comment(dto.getComment()).availableSports(dto.getAvailableSports())
                .commissioningDate(dto.getCommissioningDate());
    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "availableSports", source = "availableSports")
    @Mapping(target = "commissioningDate", source = "commissioningDate")
    public abstract SkiLift toDomain(SkiLiftEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "availableSports", source = "availableSports", qualifiedByName = "sportToEntityForCreate")
    @Mapping(target = "commissioningDate", source = "commissioningDate")
    public abstract SkiLiftEntity toEntityForCreate(SkiLift skiLift);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "availableSports", source = "availableSports", qualifiedByName = "sportToEntityForUpdate")
    @Mapping(target = "commissioningDate", source = "commissioningDate")
    public abstract SkiLiftEntity toEntityForUpdate(SkiLift skiLift);

    default void updateEntityFromDto(SkiLiftDTO dto, @MappingTarget SkiLift.Builder builder) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            builder.id(dto.getId());
        }

        if (dto.getName() != null) {
            builder.name(dto.getName());
        }

        if (dto.getType() != null) {
            builder.type(dto.getType());
        }

        if (dto.getStatus() != null) {
            builder.status(dto.getStatus());
        }

        if (dto.getComment() != null) {
            builder.comment(dto.getComment());
        }

        if (dto.getAvailableSports() != null) {
            builder.availableSports(dto.getAvailableSports());
        }

        if (dto.getCommissioningDate() != null) {
            builder.commissioningDate(dto.getCommissioningDate());
        }
    }

    default List<SkiLift> toDomainList(List<SkiLiftEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }
}