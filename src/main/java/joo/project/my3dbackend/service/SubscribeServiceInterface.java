package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.constants.PackageType;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;

public interface SubscribeServiceInterface {

    /**
     * 구독 저장
     */
    void saveSubscribe(PackageType packageType, SubscribeStatus subscribeStatus, Long userAccountId);
}
