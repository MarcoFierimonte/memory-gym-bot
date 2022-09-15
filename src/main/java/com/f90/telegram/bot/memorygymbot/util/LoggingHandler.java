package com.f90.telegram.bot.memorygymbot.util;

import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import com.google.common.base.Joiner;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Logging handler.
 */
@Aspect
@Component
public class LoggingHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result object
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        this.logRequest(joinPoint);
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        this.logResponse(result, joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName(), timeElapsed);
        return result;
    }

    private void logRequest(JoinPoint joinPoint) throws InternalException {
        Object[] args = joinPoint.getArgs();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Map<String, String> argsMap = new LinkedHashMap<>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        argsMap.put("requestURL", request.getRequestURL().toString());
        argsMap.put("httpMethod", request.getMethod());
        for (int i = 0; i < args.length; i++) {
            String argName = codeSignature.getParameterNames()[i];
            String jsonArg;
            if (this.validateLog(args[i])) {
                jsonArg = JsonUtil.toJson(args[i]);
            } else {
                if (args[i] == null) {
                    jsonArg = null;
                } else {
                    jsonArg = "@" + args[i].getClass().getSimpleName();
                }
            }
            argsMap.put(argName, jsonArg);
        }
        String argsAsString = Joiner.on(", ").withKeyValueSeparator("=").join(argsMap);
        LOGGER.info("{}.{}() - IN: {}", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName(), argsAsString);
    }

    private void logResponse(Object response, String className, String methodName, long timeElapsed) throws InternalException {
        Object toLog;
        if (response instanceof ResponseEntity) {
            toLog = ((ResponseEntity<?>) response).getBody();
        } else {
            toLog = response;
        }

        if (this.validateLog(toLog)) {
            String responseToLog = JsonUtil.toJson(toLog);
            LOGGER.info("{}.{}() - OUT: elapsedTime=[{} ms] - {}", className, methodName, timeElapsed, responseToLog);
        } else {
            LOGGER.info("{}.{}() - OUT: elapsedTime=[{} ms]", className, methodName, timeElapsed);
        }
    }

    private boolean validateLog(Object arg) {
        return arg != null && (ClassUtils.isPrimitiveOrWrapper(arg.getClass()) || arg instanceof String || arg instanceof Temporal || arg instanceof Collection);
    }

}