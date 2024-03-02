package joo.project.my3dbackend.aop;

import joo.project.my3dbackend.dto.response.ExceptionResponse;
import joo.project.my3dbackend.exception.ValidatedException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.procedure.NoSuchParameterException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Method;

@Slf4j
@Component
@Aspect
public class BindingResultHandlerAspect {

    @Pointcut("@annotation(BindingResultHandler)")
    private void bindingResultPointcut() {}

    @Around("bindingResultPointcut()")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        BindingResult bindingResult = getBindingResult(signature, pjp.getArgs());
        Method method = signature.getMethod();
        BindingResultHandler annotation = method.getAnnotation(BindingResultHandler.class);

        if (bindingResult.hasErrors()) {
            log.error("bindingResult: {}", bindingResult);
            throw new ValidatedException(
                    ErrorCode.INVALID_REQUEST,
                    ExceptionResponse.fromBindingResult(annotation.message(), bindingResult));
        }

        return pjp.proceed();
    }

    private BindingResult getBindingResult(MethodSignature signature, Object[] arguments) {
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals("bindingResult")) {
                return (BindingResult) arguments[i];
            }
        }

        throw new NoSuchParameterException("bindingResult");
    }
}
