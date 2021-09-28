package com.miquido.readwriteseparation

import com.miquido.readwriteseparation.DbContextHolder.dbType
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.Ordered
import org.springframework.stereotype.Component

@Aspect
@Component
class ReadOnlyInterceptor : Ordered {
    @Around("@annotation(ReadOnly)")
    fun setRead(joinPoint: ProceedingJoinPoint): Any {
        try {
            dbType = DataSourceType.READONLY
            return joinPoint.proceed()
        } finally {
            DbContextHolder.reset()
        }
    }

    override fun getOrder() = 0
}
