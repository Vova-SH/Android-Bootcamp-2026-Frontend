package com.example.planify.core.network.middleware

import com.example.planify.core.network.ApiRequestContext

class ApiClientMiddlewareChain<FinalOut>(
    // lighter = abstract-lower
    private val middlewares: List<ApiClientMiddleware<*, *>>  // [Validator, Parser, Ktor]
) {

    @Suppress("UNCHECKED_CAST")
    suspend fun execute(
        initialRequest: Any,
        context: ApiRequestContext
    ): FinalOut {
        return executeStep(0, initialRequest, context) as FinalOut
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun executeStep(
        index: Int,
        input: Any,
        context: ApiRequestContext
    ): Any {
        if (index >= middlewares.size) return input

        val middleware = middlewares[index] as ApiClientMiddleware<Any, Any>

        val nextStep: suspend (Any) -> Any? = { nextInput ->
            executeStep(index + 1, nextInput, context)
        }

        return middleware.proceed(context = context, next = nextStep, input = input)
    }

    class Builder<FinalT>(  // TODO: Add <FinalT> for compile-time type-safety
        private val steps: MutableList<ApiClientMiddleware<*, *>> = ArrayList()
    ) {
        companion object {
            fun <ChainFinalT> fromChain(other: ApiClientMiddlewareChain<*>): Builder<ChainFinalT> {
                // Since the Chain stores them in execution order [Shell -> Core],
                // and our Builder stores them in addition order [Core -> Shell],
                // we reverse them back to add them correctly.
                return Builder(other.middlewares.reversed().toMutableList())
            }
        }

        fun add(middleware: ApiClientMiddleware<*, *>): Builder<FinalT> {
            steps.add(middleware)
            return this
        }

        fun <T> insertBefore(middleware: ApiClientMiddleware<*, *>, beforeClazz: Class<T>): Builder<FinalT> {
            val index = steps.indexOfFirst { it::class.java == beforeClazz }
            if (index == -1) throw IllegalArgumentException("Cannot insert before ${beforeClazz.simpleName}: Not found in chain")
            steps.add(index, middleware)
            return this
        }

        fun <T> insertAfter(middleware: ApiClientMiddleware<*, *>, afterClazz: Class<T>): Builder<FinalT> {
            val index = steps.indexOfFirst { it::class.java == afterClazz }
            if (index == -1) throw IllegalArgumentException("Cannot insert after ${afterClazz.simpleName}: Not found in chain")
            steps.add(index + 1, middleware)
            return this
        }

        fun build(): ApiClientMiddlewareChain<FinalT> {  // TODO: Store in same order as chain?
            // We reverse them so the last added (the Shell) is index 0 (the first to execute)
            return ApiClientMiddlewareChain(steps.reversed())
        }
    }
}
