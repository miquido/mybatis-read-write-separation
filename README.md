# mybatis-read-write-separation
[![GitHub license](https://img.shields.io/badge/license-Apache2.0-brightgreen.svg)](https://github.com/miquido/mybatis-read-write-separation/blob/main/LICENSE)
![Tests](https://github.com/miquido/mybatis-read-write-separation/workflows/Tests/badge.svg?branch=main)

This is a Kotlin library providing read/write separation for Spring + MyBatis applications supporting one master db and one slave db.

The database master-slave replication is used to isolate resource modification from reading. It can solve many database bottlenecks by manipulating master table when working with data and querying using the slave table.
Read-write separation provides better expansibility, maintainability and usability of the systems.

## Setup

1. Add mybatis-read-write-separation dependency to *build.gradle* file:
```
implementation 'com.miquido:mybatis-read-write-separation:1.0'
```

2. In main application class annotated with *@SpringBootApplication* use *@Import* annotation to cherry-pick configuration classes from mybatis-read-write-separation library:
```kotlin
@Import(ReadWriteDatasourceConfig::class, ReadOnlyInterceptor::class)
```

3. Configure *application.properties* file as below to hold properties of read-write datasources:
```properties
spring.datasource.url=url-of-your-master-db
spring.datasource.username=master-db-username
spring.datasource.password=master-db-password
spring.datasource.driver-class-name=master-db-driver
spring.datasource.readonly.url=url-of-your-slave-db
spring.datasource.readonly.username=slave-db-username
spring.datasource.readonly.password=slave-db-password
spring.datasource.readonly.driver-class-name=slave-db-driver
```

4. Add *@ReadOnly* annotation to all functions in service classes where you want to run your queries on slave database, e.g.:
```kotlin
@Service
class UserService {
    
    @Transactional
    @ReadOnly
    override fun selectUserQuery() =
        repository.selectUserQuery()
    
}
```
