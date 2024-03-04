package joo.project.my3dbackend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DimensionOption {
    private String name;
    private Set<Dimension> dimensions = new LinkedHashSet<>();
}
