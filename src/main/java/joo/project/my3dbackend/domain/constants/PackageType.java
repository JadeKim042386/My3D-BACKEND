package joo.project.my3dbackend.domain.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PackageType {
    FREE(0),
    ONE_MONTH(1),
    THREE_MONTH(3),
    SIX_MONTH(6),
    ONE_YEAR(12);

    private final int month;
}
