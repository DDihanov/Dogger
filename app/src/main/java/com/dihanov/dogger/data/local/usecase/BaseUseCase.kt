package com.dihanov.dogger.data.local.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dihanov.dogger.data.local.repository.Resource

abstract class BaseUseCase<T> {
    private val _data = MutableLiveData<Resource<T>>()
    val data: LiveData<Resource<T>>
        get() = _data

    protected fun loading(data: T?) {
        _data.postValue(Resource.loading(data))
    }

    protected fun error(msg: String, data: T?) {
        _data.postValue(Resource.error(msg, data))
    }

    protected fun success(data: T) {
        _data.postValue(Resource.success(data))
    }

    abstract suspend fun execute()
}