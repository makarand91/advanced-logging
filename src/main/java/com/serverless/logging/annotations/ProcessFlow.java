package com.serverless.logging.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessFlow  {
    Tags tags() default @Tags;
    String value() default "";
}
