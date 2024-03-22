package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.constants.PackageType;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;

import javax.validation.constraints.NotNull;

public record SubscribeRequest(@NotNull PackageType packageType, @NotNull SubscribeStatus subscribeStatus) {}
