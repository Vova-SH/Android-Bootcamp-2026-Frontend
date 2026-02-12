package com.example.planify.core

open class SingletonHolder<T, A>(
    private val creator: (A) -> T
) {
    @Volatile
    private var instance: T? = null

    fun init(a: A) {
        check(instance == null) { "Instance already initialized" }
        instance = creator(a)
    }

    fun get(): T = checkNotNull(instance) { "Instance is not initialized. Call init() first." }
}


open class SingletonHolder2<T, A, B>(  // To have 2 constructor params but keep type safety
    private val creator: (A, B) -> T
) {
    @Volatile
    private var instance: T? = null

    fun init(a: A, b: B) {
        check(instance == null) { "Instance already initialized" }
        instance = creator(a, b)
    }

    fun get(): T =
        checkNotNull(instance) { "Instance is not initialized. Call init() first." }
}


open class SingletonHolder3<T, A, B, C>(  // To have 3 constructor params but keep type safety
    private val creator: (A, B, C) -> T
) {
    @Volatile
    private var instance: T? = null

    fun init(a: A, b: B, c: C) {
        check(instance == null) { "Instance already initialized" }
        instance = creator(a, b, c)
    }

    fun get(): T =
        checkNotNull(instance) { "Instance is not initialized. Call init() first." }
}
