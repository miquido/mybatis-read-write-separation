package com.miquido.readwriteseparation

import com.miquido.readwriteseparation.DbContextHolder.dbType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class ReadWriteRoutingDataSource : AbstractRoutingDataSource() {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun determineCurrentLookupKey(): DataSourceType =
        dbType.also {
            log.info(it.toString())
        }
}
