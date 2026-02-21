package com.subscription.plan.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class ExecutionTimer {

    @Pointcut("@annotation(com.subscription.plan.common.ExeTimer)")
    public void logExecutionTime() { }

    @Around("logExecutionTime()")
    public Object AssumeExecutionTime(ProceedingJoinPoint pjp) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        Object result = pjp.proceed();
        stopWatch.start();
        pjp.proceed();
        stopWatch.stop();

        long totalTime = stopWatch.getTotalTimeMillis();
        System.out.println("Total time taken for execution: " + totalTime + " ms");
        return result;
    }
}
