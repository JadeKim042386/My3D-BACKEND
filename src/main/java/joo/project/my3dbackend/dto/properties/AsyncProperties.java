package joo.project.my3dbackend.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Validated
@ConfigurationProperties(prefix = "async.executor")
public record AsyncProperties(
        @Positive Integer corePoolSize,
        @Positive Integer maxPoolSize,
        @Positive Integer queueCapacity,
        @NotBlank String threadNamePrefix) {}
