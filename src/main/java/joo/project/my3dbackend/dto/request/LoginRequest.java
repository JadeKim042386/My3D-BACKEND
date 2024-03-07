package joo.project.my3dbackend.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
