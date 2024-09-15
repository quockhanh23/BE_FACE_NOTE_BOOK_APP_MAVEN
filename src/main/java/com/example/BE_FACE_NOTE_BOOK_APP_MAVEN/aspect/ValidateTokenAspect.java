package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.aspect;


import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption.TokenInvalidException;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class ValidateTokenAspect {

    private final UserService userService;

    @Autowired
    public ValidateTokenAspect(UserService userService) {
        this.userService = userService;
    }

    private Map<String, Object> getMethodSignature(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> parameterMap = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            parameterMap.put(parameterNames[i], args[i]);
        }
        String methodName = methodSignature.getMethod().getName();
        log.info("Calling method: " + methodName + " with parameters: " + parameterMap);
        return parameterMap;
    }

    private void parameterIdAndAuthorization(JoinPoint joinPoint, String idType) {
        Map<String, Object> parameterMap = getMethodSignature(joinPoint);
        if (parameterMap.containsKey("authorization") && parameterMap.containsKey(idType)) {
            Object authorizationValue = parameterMap.get("authorization");
            Object id = parameterMap.get(idType);
            if (Objects.isNull(authorizationValue) || Objects.isNull(id)) return;
            if (!userService.errorToken(authorizationValue.toString(), Long.valueOf(id.toString()))) {
                throw new TokenInvalidException(Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase());
            }
        }
    }

    @Before("execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.AdminRestController.*(..))")
    public void beforeAdminRestControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token AdminRestController Methods");
        parameterIdAndAuthorization(joinPoint, Constants.IdCheck.ID_ADMIN);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token AdminRestController Methods");
    }

    @Before("execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.UserController.*(..)) && !execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.UserController.passwordRetrieval(..))")
    public void beforeUserControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token UserController Methods");
        parameterIdAndAuthorization(joinPoint, Constants.IdCheck.ID_USER);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token UserController Methods");
    }

    @Before("execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.ImageRestController.*(..)) && !execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.ImageRestController.findAllImages(..))")
    public void beforeImageRestControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token ImageRestController Methods");
        parameterIdAndAuthorization(joinPoint, Constants.IdCheck.ID_USER);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token ImageRestController Methods");
    }

    @Before("execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.PostRestController.*(..)) " +
            "&& !execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.PostRestController.allPostPublic(..)) " +
            "&& !execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.PostRestController.findOnePostById(..))")
    public void beforePostRestControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token PostRestController Methods");
        parameterIdAndAuthorization(joinPoint, Constants.IdCheck.ID_USER);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token PostRestController Methods");
    }

    @Before("execution(* com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller.AnswerCommentRestController.*(..))")
    public void beforeAnswerCommentRestControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token AnswerCommentRestController Methods");
        parameterIdAndAuthorization(joinPoint, Constants.IdCheck.ID_USER);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token AnswerCommentRestController Methods");
    }
}
