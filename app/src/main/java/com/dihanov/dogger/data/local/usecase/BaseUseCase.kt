package com.dihanov.dogger.data.local.usecase

import com.dihanov.dogger.data.local.repository.Resource

abstract class BaseUseCase<Result, Params> {
    abstract suspend fun execute(params: Params): Resource<Result>

    protected fun success(data: Result) = Resource.success(data)

    protected fun error(data: Result, msg: String) = Resource.error(msg, data)

    //when no params needed
    class None
}