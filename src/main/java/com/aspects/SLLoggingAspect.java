package com.aspects;

import com.serverless.logging.annotations.Identifier;
import com.serverless.logging.extenstions.MethodSignatureWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Aspect
public class SLLoggingAspect  {

    public SLLoggingAspect() {
        super();
    }

    @Around("@annotation(com.serverless.logging.annotations.ProcessFlow) && execution(public * *(..))")
    public Object injectContext(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("executed advice "+this);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Class<?> clazz = methodSignature.getDeclaringType();
        Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        Identifier argumentAnnotation;

        if (method.getParameterAnnotations() != null && method.getParameterAnnotations().length > 0 && method.getParameterAnnotations()[0] != null && method.getParameterAnnotations()[0].length > 0) {
            for (Annotation ann : method.getParameterAnnotations()[0]) {
                if (Identifier.class.isInstance(ann)) {
                    Object[] signatureArgs = joinPoint.getArgs();
                    if (signatureArgs != null && signatureArgs.length > 0) {
                        MDC.put("id", signatureArgs[0].toString());
                        System.out.println("identifier value" + signatureArgs[0].toString());
                    }
                }
            }
        }
        Object proceed = joinPoint.proceed();

        return joinPoint;
    }



    public int getIdentifierForMethod(MethodSignatureWrapper methodSignatureWrapper) {

        try {
            MethodSignature methodSignature = methodSignatureWrapper.getMethodSignature();
            Class<?> clazz = methodSignature.getDeclaringType();
            Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            if (method.getParameterAnnotations() != null && method.getParameterAnnotations().length > 0 && method.getParameterAnnotations()[0] != null && method.getParameterAnnotations()[0].length > 0) {

            }
        }
        catch (Exception e){}
        return 0;
    }
}
