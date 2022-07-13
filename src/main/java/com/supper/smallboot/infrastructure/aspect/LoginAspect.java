package com.supper.smallboot.infrastructure.aspect;


import com.supper.smallboot.infrastructure.anaotation.Login;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author GuiWu
 * @version V1.0
 * @description: 登录切面验证
 * @date 1/15/22 2:41 PM
 */
@Aspect
@Component
@Slf4j
public class LoginAspect {

    @Pointcut("@annotation(com.supper.smallboot.infrastructure.anaotation.Login)")
    private void cutMethod() {
    }


    @Around(value = "cutMethod() && @annotation(login)")
    public Object login(ProceedingJoinPoint pjp, Login login) throws Throwable {
        log.info("获取登录信息切面拦截");
        return pjp.proceed();
    }
}

