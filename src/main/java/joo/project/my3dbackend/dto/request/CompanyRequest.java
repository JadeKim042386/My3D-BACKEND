package joo.project.my3dbackend.dto.request;

import javax.validation.constraints.NotBlank;

public record CompanyRequest(@NotBlank String companyName, String homepage) {}
