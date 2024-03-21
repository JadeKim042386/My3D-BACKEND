package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.AlarmDto;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.service.AlarmServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AlarmApi {
    private final AlarmServiceInterface<SseEmitter> alarmService;

    /**
     * 현재 로그인한 사용자에게 온 알람 조회
     */
    @GetMapping
    public ResponseEntity<List<AlarmDto>> getAlarms(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(alarmService.getAlarms(userPrincipal.id()));
    }

    /**
     * SSE 연결 요청
     */
    @GetMapping("/subscribe")
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(alarmService.connectAlarm(userPrincipal.id()));
    }
}
