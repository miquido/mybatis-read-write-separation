package com.miquido.readwriteseparation

import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import javax.sql.DataSource

@Configuration
class ReadWriteDatasourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource")
    fun writeDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @ConfigurationProperties("spring.datasource.readonly")
    fun readonlyDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Primary
    @Bean("writeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    fun writeDataSource(): DataSource {
        return writeDataSourceProperties().initializeDataSourceBuilder().build()
    }

    @Bean("readonlyDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.readonly")
    fun readonlyDataSource(): DataSource {
        return readonlyDataSourceProperties().initializeDataSourceBuilder().build()
    }

    @Bean
    @Throws(Exception::class)
    fun sqlSessionFactory(routingDataSource: AbstractRoutingDataSource): SqlSessionFactory? =
        SqlSessionFactoryBean().apply {
            setDataSource(routingDataSource)
        }.getObject()

    @Bean
    fun dataSourceTransactionManager(routingDataSource: AbstractRoutingDataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(routingDataSource)
    }

    @Bean
    fun routingDataSource(
        @Qualifier("writeDataSource") writeDataSource: DataSource,
        @Qualifier("readonlyDataSource") readonlyDataSource: DataSource
    ): AbstractRoutingDataSource {
        val proxy = ReadWriteRoutingDataSource()
        val targetDataSources: Map<Any, DataSource> = mapOf(
            DataSourceType.WRITE to writeDataSource,
            DataSourceType.READONLY to readonlyDataSource
        )
        proxy.setDefaultTargetDataSource(writeDataSource)
        proxy.setTargetDataSources(targetDataSources)
        return proxy
    }
}
