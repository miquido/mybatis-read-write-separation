package com.miquido.readwriteseparation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.aspectj.lang.ProceedingJoinPoint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ReadOnlyInterceptorTest {

    private val readOnlyInterceptor = ReadOnlyInterceptor()

    @Test
    fun shouldSetDbTypeReadonly() {
        val mockJoinPoint = mockk<ProceedingJoinPoint>()

        every {
            mockJoinPoint.proceed()
        } answers {
            assertEquals(DataSourceType.READONLY, DbContextHolder.dbType)
        }

        readOnlyInterceptor.setRead(mockJoinPoint)

        verify { mockJoinPoint.proceed() }

        assertEquals(DataSourceType.WRITE, DbContextHolder.dbType)
    }
}