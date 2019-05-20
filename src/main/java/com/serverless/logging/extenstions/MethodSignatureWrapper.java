package com.serverless.logging.extenstions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

@AllArgsConstructor
@Data
public class MethodSignatureWrapper {
    private MethodSignature methodSignature;


    @Override
    public int hashCode() {
        String key = this.methodSignature.getDeclaringTypeName() + this.methodSignature.getName();
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        String key = this.methodSignature.getDeclaringTypeName() + this.methodSignature.getName();
        String inKey = ((MethodSignatureWrapper) obj).getMethodSignature().getDeclaringTypeName() + ((MethodSignatureWrapper) obj).getMethodSignature().getName();
        return key.equals(inKey);
    }
}
