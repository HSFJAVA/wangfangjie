package com.kwe.kweplus.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @ClassName LogAspect
 * @Auth ly
 * @Date 2019/9/17
 * @Version 1.0
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {

    private Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.kwe.kweplus.controller.*.*(..)) &&  !execution(* com.kwe.kweplus.controller.TaskController*.*(..)) &&  !execution(* com.kwe.kweplus.controller.OcrServiceMergeController*.*(..)) && !execution(* com.kwe.kweplus.controller.indexController.*List(..))    )")
    public void log(){}

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<");
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "log()")
    public void doAfterReturning(Object ret){
        // 处理完请求，返回内容
        log.info("RESPONSE : " + ret);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
