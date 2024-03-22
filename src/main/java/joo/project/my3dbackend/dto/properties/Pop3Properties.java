package joo.project.my3dbackend.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "pop3.mail")
public record Pop3Properties(
        @NotBlank String host,
        @NotBlank String port,
        @NotBlank String protocol,
        @NotBlank String folder,
        @NotBlank @Email String username,
        @NotBlank String password,
        @NotNull Long untilTime) {}
