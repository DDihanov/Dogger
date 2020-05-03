package com.dihanov.base_domain

abstract class BaseUseCase<Result, Params> {
    abstract suspend fun execute(params: Params): Resource<Result>

    protected fun success(data: Result) =
        Resource.success(data)

    protected fun error(data: Result, msg: String) =
        Resource.error(msg, data)

    //when no params needed
    class None
}