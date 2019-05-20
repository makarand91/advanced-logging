package com.serverless.logging.advice;

import com.serverless.logging.annotations.Tags;
import com.serverless.logging.annotations.Task;
import com.serverless.logging.extenstions.MethodSignatureWrapper;
import com.serverless.logging.metadata.EntityType;
import com.serverless.logging.processor.AbstractAdvice;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

@Aspect
public class TaskAdvice extends AbstractAdvice {

    public TaskAdvice() {
        super();
    }

    @Around("@annotation(com.serverless.logging.annotations.Task) && execution(public * *(..))")
    public Object injectContext(ProceedingJoinPoint joinPoint) throws Throwable {
        String parentContext =preProcess(joinPoint);
        joinPoint.proceed();
        returnContext(parentContext);
        return joinPoint;

    }

    @Override
    public EntityType getEntityType() {
        return EntityType.TaskType;
    }

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return Task.class;
    }

    @Override
    public Tags getTags(Annotation entityAnotation) {
        return ((Task) entityAnotation).tags();
    }

    @Override
    public String getName(Annotation entityAnotation) {
        return ((Task) entityAnotation).value();
    }
}