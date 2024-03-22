package joo.project.my3dbackend.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record EmailRequest(@NotBlank @Email String email, String code) {}
