package com.example.demo2.components.aspects;

import jakarta.servlet.Servlet;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
@Aspect
public class UserActivityLogger {
    private static final Logger logger = LoggerFactory.getLogger(UserActivityLogger.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) ")
    public void controllerMethods() {}

    @Around("controllerMethods() && execution(* com.example.demo2.controller.CategoryController.*(..))")
    public Object logUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        // ghi log truoc khi thuc hien method
        String methodName = joinPoint.getSignature().getName();
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        logger.info("User activity started " + methodName + ", IP address: " + remoteAddress);
        // thuc hien method goc(trong categoryController tai dong 21)
        Object result = joinPoint.proceed();

        // ghi lai log sau khi thuc hien request
        logger.info("User activity finished " + methodName);

        return result;

    }

}
