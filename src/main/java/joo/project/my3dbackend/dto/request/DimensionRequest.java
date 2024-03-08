package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.Dimension;
import joo.project.my3dbackend.domain.constants.DimUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class DimensionRequest {
    @NotBlank
    private String name;

    @Positive
    private Double value;

    private DimUnit unit;

    public Dimension toEntity() {
        return new Dimension(name, value, unit);
    }
}
