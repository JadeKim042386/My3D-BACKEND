package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Subscribe;
import joo.project.my3dbackend.domain.constants.PackageType;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;
import joo.project.my3dbackend.exception.SubscribeException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.SubscribeRepository;
import joo.project.my3dbackend.service.SubscribeServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscribeService implements SubscribeServiceInterface {
    private final SubscribeRepository subscribeRepository;

    @Transactional(readOnly = true)
    @Override
    public Subscribe getSubscribe(Long userAccountId) {
        return subscribeRepository
                .findByUserAccount_Id(userAccountId)
                .orElseThrow(() -> new SubscribeException(ErrorCode.NOT_FOUND_SUBSCRIBE));
    }

    @Transactional
    @Override
    public void updateSubscribe(PackageType packageType, SubscribeStatus subscribeStatus, Long userAccountId) {
        Subscribe subscribe = getSubscribe(userAccountId);
        // 구독 요청일 경우 startedAt, packageType 수정
        if (subscribeStatus == SubscribeStatus.SUBSCRIBE) {
            subscribe.setPackageType(packageType);
            subscribe.setStartedAt(LocalDateTime.now());
        }
        subscribe.setSubscribeStatus(subscribeStatus);
    }

    @Override
    public void saveSubscribe(PackageType packageType, SubscribeStatus subscribeStatus, Long userAccountId) {
        subscribeRepository.save(Subscribe.of(packageType, subscribeStatus, LocalDateTime.now(), userAccountId));
    }
}
