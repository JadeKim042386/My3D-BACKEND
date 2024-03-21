package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Alarm;
import joo.project.my3dbackend.domain.constants.AlarmType;

import java.time.LocalDateTime;

public record AlarmDto(
        Long id,
        AlarmType alarmType,
        String fromUserNickname,
        Long articleId,
        LocalDateTime readAt,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt) {
    public static AlarmDto fromEntity(Alarm alarm) {
        return new AlarmDto(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getSender().getNickname(),
                alarm.getArticle().getId(),
                alarm.getReadAt(),
                alarm.getCreatedAt(),
                alarm.getModifiedAt());
    }
}
