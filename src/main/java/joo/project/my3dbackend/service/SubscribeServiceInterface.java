package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.Subscribe;
import joo.project.my3dbackend.domain.constants.PackageType;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;

public interface SubscribeServiceInterface {

    Subscribe getSubscribe(Long userAccountId);

    /**
     * 구독 수정
     */
    void updateSubscribe(PackageType packageType, SubscribeStatus subscribeStatus, Long userAccountId);

    /**
     * 구독 저장
     */
    void saveSubscribe(PackageType packageType, SubscribeStatus subscribeStatus, Long userAccountId);
}
