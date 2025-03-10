//package com.example.nbastats.aspects;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class LoggingAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Pointcut("execution(* com.example.nbastats.controllers.*.*(..)) || execution(* com.example.nbastats.services.*.*(..))")
//    public void controllerAndServiceMethods() {}
//
//    @Before("controllerAndServiceMethods()")
//    public void logBefore(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//        logger.info("Executing method: {} with arguments: {}", methodName, args);
//    }
//
//    @After("controllerAndServiceMethods()")
//    public void logAfter(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        logger.info("Completed execution of method: {}", methodName);
//    }
//}
