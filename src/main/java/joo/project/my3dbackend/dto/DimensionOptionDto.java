package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.DimensionOption;

import java.util.Set;
import java.util.stream.Collectors;

public record DimensionOptionDto(String optionName, Set<DimensionDto> dimensions) {

    public static DimensionOptionDto fromEntity(DimensionOption dimensionOption) {
        return new DimensionOptionDto(
                dimensionOption.getName(),
                dimensionOption.getDimensions().stream()
                        .map(DimensionDto::fromEntity)
                        .collect(Collectors.toUnmodifiableSet()));
    }
}
