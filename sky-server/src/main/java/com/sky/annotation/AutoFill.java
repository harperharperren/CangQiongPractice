package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//表明该注解是加在方法上的
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
