package com.miquido.readwriteseparation

object DbContextHolder {

    private val contextHolder = ThreadLocal.withInitial { DataSourceType.WRITE }

    var dbType: DataSourceType
        get() = contextHolder.get()
        set(dbType) {
            contextHolder.set(dbType)
        }

    fun reset() {
        contextHolder.remove()
    }
}
