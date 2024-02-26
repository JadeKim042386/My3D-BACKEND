package joo.project.my3dbackend.dto.request.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private ValidEnum annotation;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)) return false;
        // validation 대상 값이 annotation에서 지정한 enum에 속하는지 확인
        return Optional.ofNullable(this.annotation.enumClass().getEnumConstants())
                .map(e -> Arrays.stream(e).anyMatch(it -> it.name().equalsIgnoreCase(value)))
                .orElse(false);
    }
}
