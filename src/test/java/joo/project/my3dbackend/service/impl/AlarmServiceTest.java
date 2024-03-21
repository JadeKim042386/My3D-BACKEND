package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Alarm;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.repository.AlarmRepository;
import joo.project.my3dbackend.repository.EmitterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {
    @InjectMocks private AlarmService alarmService;
    @Mock private EmitterRepository emitterRepository;
    @Mock private AlarmRepository alarmRepository;
    @Mock private ArticleService articleService;

    @DisplayName("알람 전송")
    @Test
    void alarmSend() {
        //given
        Long targetId = 1L, articleId = 1L, receiverId = 2L, senderId = 1L;
        given(articleService.getUserAccountIdOfArticle(anyLong())).willReturn(receiverId);
        given(alarmRepository.save(any(Alarm.class))).willReturn(Fixture.createAlarm());
        given(emitterRepository.get(anyLong())).willReturn(Optional.of(new SseEmitter()));
        //when
        alarmService.send(articleId, targetId, senderId, receiverId);
        //then
    }
}
