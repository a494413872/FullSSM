package com.demo.extra;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class ServiceAop {

    @Pointcut(value="execution(* com.demo.extra.circleIoc.*Impl.*(..))")
    private void pointCut(){}
    @Around(value="pointCut()")
    public Object doAccessCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Class targetClass = joinPoint.getTarget().getClass();
//        Object[] args = joinPoint.getArgs(); 获取参数
//        Method[] routeMethod = t.getClass().getMethods(); 获取方法
        System.out.println(targetClass+methodName);
        return joinPoint.proceed();
    }
}
