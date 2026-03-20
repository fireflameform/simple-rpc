package com.fff.simplerpc.springboot.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface RpcService {
    Class<?> interfaceClass() default void.class;

    String interfaceName() default "";

    String version() default "";
}
