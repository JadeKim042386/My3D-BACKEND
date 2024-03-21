package joo.project.my3dbackend.repository;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@NoArgsConstructor
public class EmitterRepository {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter save(Long userAccountId, SseEmitter sseEmitter) {
        final String key = getKey(userAccountId);
        emitterMap.put(key, sseEmitter);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(Long userAccountId) {
        final String key = getKey(userAccountId);
        return Optional.ofNullable(emitterMap.get(key));
    }

    public void delete(Long userAccountId) {
        emitterMap.remove(getKey(userAccountId));
    }

    private String getKey(Long userAccountId) {
        return "Emitter:Id:" + userAccountId;
    }
}
