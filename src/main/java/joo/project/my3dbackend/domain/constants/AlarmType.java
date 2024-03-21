package joo.project.my3dbackend.domain.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
    NEW_COMMENT("새로운 댓글!");

    private final String alarmText;
}
