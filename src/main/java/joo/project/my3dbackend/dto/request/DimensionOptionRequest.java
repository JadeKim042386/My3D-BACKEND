package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.DimensionOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DimensionOptionRequest {
    @NotBlank
    private String optionName;

    @NotNull
    @Size(min = 1)
    private final List<DimensionRequest> dimensions = new ArrayList<>();

    public DimensionOption toEntity() {
        DimensionOption dimensionOption = DimensionOption.of(optionName);
        dimensionOption
                .getDimensions()
                .addAll(dimensions.stream().map(DimensionRequest::toEntity).toList());
        return dimensionOption;
    }
}
