package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.constants.DimUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dimension {
    private String name; // 치수명
    private Double value; // 치수값
    private DimUnit unit; // 치수 단위
}
