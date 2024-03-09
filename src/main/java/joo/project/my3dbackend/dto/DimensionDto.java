package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Dimension;
import joo.project.my3dbackend.domain.constants.DimUnit;

public record DimensionDto(String name, Double value, DimUnit unit) {

    public static DimensionDto fromEntity(Dimension dimension) {
        return new DimensionDto(dimension.getName(), dimension.getValue(), dimension.getUnit());
    }
}
