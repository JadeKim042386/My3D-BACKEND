package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Alarm;
import joo.project.my3dbackend.domain.constants.AlarmType;
import joo.project.my3dbackend.dto.AlarmDto;
import joo.project.my3dbackend.exception.AlarmException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.AlarmRepository;
import joo.project.my3dbackend.repository.EmitterRepository;
import joo.project.my3dbackend.service.AlarmServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmService implements AlarmServiceInterface<SseEmitter> {
    private static final Long SSE_TIMEOUT = 30L * 60 * 1000; // 30분
    private static final String ALARM_NAME = "alarm";
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public List<AlarmDto> getAlarms(Long receiverId) {
        return alarmRepository.findAllByReceiverId(receiverId).stream()
                .map(AlarmDto::fromEntity)
                .sorted(Comparator.comparing(AlarmDto::createdAt).reversed())
                .toList();
    }

    @Transactional
    @Override
    public void send(Long articleId, Long targetId, Long senderId, Long receiverId) {
        Alarm alarm = saveAlarm(targetId, articleId, senderId, receiverId);
        emitterRepository
                .get(receiverId)
                .ifPresentOrElse(
                        sseEmitter -> {
                            try {
                                sseEmitter.send(SseEmitter.event()
                                        .id(String.valueOf(alarm.getId()))
                                        .name(ALARM_NAME)
                                        .data(AlarmDto.fromEntity(alarm)));
                            } catch (IOException e) {
                                emitterRepository.delete(receiverId);
                                throw new AlarmException(ErrorCode.ALARM_CONNECT_ERROR, e);
                            }
                        },
                        () -> log.info("Emitter를 찾을 수 없습니다."));
    }

    @Override
    public SseEmitter connectAlarm(Long userAccountId) {
        SseEmitter sseEmitter = new SseEmitter(SSE_TIMEOUT);
        emitterRepository.save(userAccountId, sseEmitter);
        sseEmitter.onCompletion(() -> emitterRepository.delete(userAccountId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userAccountId));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("connect completed"));
        } catch (IOException e) {
            throw new AlarmException(ErrorCode.ALARM_CONNECT_ERROR, e);
        }

        return sseEmitter;
    }

    @Transactional
    @Override
    public void checkAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.getReferenceById(alarmId);
        alarm.setReadAt(LocalDateTime.now());
    }

    @Transactional
    @Override
    public Alarm saveAlarm(Long targetId, Long articleId, Long senderId, Long receiverId) {
        return alarmRepository.saveAndFlush(Alarm.of(AlarmType.NEW_COMMENT, targetId, articleId, senderId, receiverId));
    }
}
