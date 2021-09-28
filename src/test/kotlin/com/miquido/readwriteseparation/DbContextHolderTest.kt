package com.miquido.readwriteseparation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DbContextHolderTest {

    @BeforeEach
    fun setUp() {
        DbContextHolder.reset()
    }

    @Test
    fun shouldSetInitialDbTypeWrite() {
        assertEquals(DataSourceType.WRITE, DbContextHolder.dbType)
    }

    @Test
    fun shouldSetDbTypeRead() {
        DbContextHolder.dbType = DataSourceType.READONLY
        assertEquals(DataSourceType.READONLY, DbContextHolder.dbType)
    }

    @Test
    fun shouldResetDbType() {
        DbContextHolder.dbType = DataSourceType.READONLY
        DbContextHolder.reset()
        assertEquals(DataSourceType.WRITE, DbContextHolder.dbType)
    }
}