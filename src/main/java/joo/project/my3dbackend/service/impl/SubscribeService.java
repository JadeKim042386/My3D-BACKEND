package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Subscribe;
import joo.project.my3dbackend.domain.constants.PackageType;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;
import joo.project.my3dbackend.repository.SubscribeRepository;
import joo.project.my3dbackend.service.SubscribeServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscribeService implements SubscribeServiceInterface {
    private final SubscribeRepository subscribeRepository;

    @Override
    public void saveSubscribe(PackageType packageType, SubscribeStatus subscribeStatus, Long userAccountId) {
        subscribeRepository.save(Subscribe.of(packageType, subscribeStatus, LocalDateTime.now(), userAccountId));
    }
}
