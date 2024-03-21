package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.Alarm;
import joo.project.my3dbackend.dto.AlarmDto;

import java.util.List;

public interface AlarmServiceInterface<T> {
    /**
     * 특정 유저의 모든 알람 조회
     */
    List<AlarmDto> getAlarms(Long receiverId);

    Alarm getAlarm(Long alarmId);

    /**
     * 알람 전송
     */
    void send(Long articleId, Long targetId, Long senderId, Long receiverId);

    /**
     * 특정 유저와의 연결 시도
     */
    T connectAlarm(Long userAccountId);

    /**
     * 알람 확인
     */
    void readAlarm(Long alarmId);

    /**
     * 알람 저장
     */
    Alarm saveAlarm(Long targetId, Long articleId, Long senderId, Long receiverId);
}
