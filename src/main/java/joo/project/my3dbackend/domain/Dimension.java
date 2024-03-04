package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.constants.DimUnit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Dimension {
    private final String name; // 치수명
    private final Double value; // 치수값
    private final DimUnit unit; // 치수 단위
}
