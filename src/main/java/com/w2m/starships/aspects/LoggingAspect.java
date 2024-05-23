package com.w2m.starships.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private static final String LOG_HEADER_SERVICE = "[API][StarshipServiceImpl]";
    private static final String LOG_HEADER_CONTROLLER = "[API][StarshipController]";
    private static final String LOG_HEADER_CONSUMER = "[AMQP][MessageConsumer]";
    private static final String LOG_HEADER_PRODUCER = "[AMQP][MessageProducer]";

    @Pointcut("execution(public * com.w2m.starships.controllers.*.*(..))")
    private void publicMethodsFromController() {
    }

    @Pointcut("execution(public * com.w2m.starships.services.impl.*.*(..))")
    private void publicMethodsFromService() {
    }

    @Pointcut("execution(public * com.w2m.starships.controllers.StarshipController.getStarshipById(..))")
    private void getByIdMethodFromController() {
    }

    @Pointcut("execution(public * com.w2m.starships.amqp.consumers.*.*(..))")
    private void publicMethodsFromAmqpConsumers() {
    }

    @Pointcut("execution(public * com.w2m.starships.amqp.producers.*.*(..))")
    private void publicMethodsFromAmqpProducers() {
    }

    @Before(value = "publicMethodsFromController()")
    public void logBeforeController(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("{} {}({})", LOG_HEADER_CONTROLLER, methodName, Arrays.toString(args));
    }

    @Before(value = "publicMethodsFromService()")
    public void logBeforeService(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("{} {}({})", LOG_HEADER_SERVICE, methodName, Arrays.toString(args));
    }

    @Before(value = "publicMethodsFromAmqpConsumers()")
    public void logBeforeConsumer(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("{} {}({})", LOG_HEADER_CONSUMER, methodName, Arrays.toString(args));
    }

    @Before(value = "publicMethodsFromAmqpProducers()")
    public void logBeforeProducer(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("{} {}({})", LOG_HEADER_PRODUCER, methodName, Arrays.toString(args));
    }

    @Before(value = "getByIdMethodFromController()")
    public void logBeforeGetByIdMethod(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        if (id < 0)
            log.error("{} There was an attempt to retrieve a negative id: {}", LOG_HEADER_CONTROLLER, id);
    }


}
