package com.serverless.logging.processor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.serverless.logging.annotations.Identifier;
import com.serverless.logging.annotations.Tags;
import com.serverless.logging.extenstions.MethodSignatureWrapper;
import com.serverless.logging.metadata.EntityInfo;
import com.serverless.logging.metadata.EntityLoggingContext;
import com.serverless.logging.metadata.EntityType;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class AbstractAdvice {
    LoadingCache<MethodSignatureWrapper, EntityInfo> idIndexProvider = null;

    ObjectMapper objectMapper = null;

    public AbstractAdvice() {
        idIndexProvider = CacheBuilder.newBuilder().build(
                new CacheLoader<MethodSignatureWrapper, EntityInfo>() {

                    public EntityInfo load(final MethodSignatureWrapper methodSignatureWrapper) throws Exception {
                        return getIdentifierForMethod(methodSignatureWrapper);
                    }

                }
        );
        objectMapper = new ObjectMapper();

    }

    public EntityInfo getIdentifierForMethod(MethodSignatureWrapper methodSignatureWrapper) {
        MethodSignature methodSignature = methodSignatureWrapper.getMethodSignature();

        Class<?> clazz = methodSignature.getDeclaringType();
        EntityInfo entityInfo = new EntityInfo();
        entityInfo.setType(getEntityType());

        try {
            Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            Annotation entityAnnotation = method.getAnnotation(getAnnotationType());
            Tags tags = getTags(entityAnnotation);
            entityInfo.setName(getName(entityAnnotation));
            entityInfo.setTags(tags.value());

            Identifier argumentAnnotation;

            if (method.getParameterAnnotations() != null && method.getParameterAnnotations().length > 0) {

                final Annotation[][] parameterAnnotations =
                        method.getParameterAnnotations();
                for (int i = 0; i < parameterAnnotations.length; i++) {

                    if (getAnnotationByType(parameterAnnotations[i], Identifier.class) != null) {
                        entityInfo.setIndex(i);
                    }
                }

            }
        } catch (Exception e) {
        }
        return entityInfo;
    }

    public abstract EntityType getEntityType();

    public abstract Class<? extends Annotation> getAnnotationType();

    public abstract Tags getTags(Annotation entityAnotation);

    public abstract String getName(Annotation entityAnotation);

    private <T extends Annotation> T getAnnotationByType(final Annotation[] annotations,
                                                         final Class<T> clazz) {

        T result = null;
        for (final Annotation annotation : annotations) {
            if (clazz.isAssignableFrom(annotation.getClass())) {
                result = (T) annotation;
                break;
            }
        }
        return result;
    }

    private EntityInfo getMethodEntityDetails(MethodSignatureWrapper methodSignatureWrapper) {
        try {
            return idIndexProvider.get(methodSignatureWrapper);
        } catch (Exception e) {
        }
        return null;
    }

    private void populateMDCContext(EntityInfo entityInfo, JoinPoint jp) throws JsonProcessingException {

        EntityLoggingContext entityLoggingContext = new EntityLoggingContext();

        entityLoggingContext.setName(entityInfo.getName());
        entityLoggingContext.setEntityType(entityInfo.getType());

        //   MDC.put("entityType", entityInfo.getType().toString());
        // MDC.put("tags", entityInfo.getTags() != null ? StringUtils.join(entityInfo.getTags(), ",") : null);
        entityLoggingContext.setTags(entityInfo.getTags() != null ? StringUtils.join(entityInfo.getTags(), ",") : null);
        //MDC.put("name", entityInfo.getName());
        if (entityInfo.getIndex() > -1) {
            //  MDC.put("id", jp.getArgs()[entityInfo.getIndex()].toString());
            entityLoggingContext.setId(jp.getArgs()[entityInfo.getIndex()].toString());
        }

        MDC.put("EntityLoggingContext", objectMapper.writeValueAsString(entityLoggingContext));
    }

    public void addLoggingMetadata(MethodSignatureWrapper methodSignatureWrapper, JoinPoint joinPoint) {
        try {
            EntityInfo entityInfo = getMethodEntityDetails(methodSignatureWrapper);
            populateMDCContext(entityInfo, joinPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parkContext() {
        return MDC.get("EntityLoggingContext");

    }

    public void returnContext(String context) {
        MDC.clear();
        if (context != null)
            MDC.put("EntityLoggingContext", context);

    }


    public String preProcess(JoinPoint joinPoint) {
        String parentContext = parkContext();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        MethodSignatureWrapper methodSignatureWrapper = new MethodSignatureWrapper(methodSignature);
        addLoggingMetadata(methodSignatureWrapper, joinPoint);

        return parentContext;


    }

    public void postProcess(String parrentContext) {
        returnContext(parrentContext);
    }


}
