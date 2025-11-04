package com.example.demo2.components.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class PerformanceAspect {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    private String getMethodName(JoinPoint joinPoint) { return joinPoint.getSignature().getName(); }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) ")
    public void controllerMethods() {}

    @Before("controllerMethods() && execution(* com.example.demo2.controller.CourseController.*(..))")
    public Object beforeMethodExecution(JoinPoint joinPoint){

        logger.info("Start execution of: " + this.getMethodName(joinPoint));

        return this.getMethodName(joinPoint);
    }

    @After("controllerMethods()")
    public Object afterMethodExecution(JoinPoint joinPoint){

        logger.info("After execution of: " + this.getMethodName(joinPoint));

        return this.getMethodName(joinPoint);
    }

    @Around("controllerMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object result = joinPoint.proceed();
        long finish = System.nanoTime();
        String methodName = joinPoint.getSignature().getName();

        // ghi lai log sau khi thuc hien request
        logger.info("Execution time of method: " + methodName + "is " + TimeUnit.NANOSECONDS.toMillis(finish - start) + "ms");

        return result;

    }

}
