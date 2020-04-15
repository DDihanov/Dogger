package com.dihanov.dogger.data.local.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.dihanov.dogger.data.model.BaseResponse
import java.util.*

// can be converted to return livedata if needed in the future
abstract class ResultLoader<LocalType, RemoteType> {
    suspend fun execute(): Resource<LocalType> {
        val cache = loadFromCache()
        val shouldFetch = shouldFetch(cache)
        if (cache == null || shouldFetch) {
            return Resource.success(cache)
        }
        val result = handleRequest { createCall() }
        return when (result.status) {
            Status.SUCCESS -> {
                writeToDb(result.data!!)
                if (!shouldReturnFromCache()) {
                    return result
                }
                Resource.success(loadFromCache())
            }
            Status.ERROR -> {
                Resource.error(result.message.toString(), cache)
            }
            else -> Resource.error(result.message.toString(), cache)
        }
    }

    private suspend fun handleRequest(requestFunc: suspend () -> BaseResponse<RemoteType>): Resource<LocalType> {
        return try {
            val response = requestFunc.invoke()
            parseResponse(response)
        } catch (he: Exception) {
            Resource.error(he.message ?: "error", null)
        }
    }


    private suspend fun parseResponse(response: BaseResponse<RemoteType>): Resource<LocalType> {
        val status = Status.valueOf(response.status.toUpperCase(Locale.getDefault()))
        return when (status) {
            Status.SUCCESS -> {
                val parsed = processResult(response.message)
                Resource.success(parsed)
            }
            else -> Resource.error(response.message.toString(), null)
        }
    }

    @WorkerThread
    protected abstract suspend fun processResult(item: RemoteType): LocalType

    @WorkerThread
    protected abstract suspend fun writeToDb(toWrite: LocalType)

    @MainThread
    open fun shouldReturnFromCache() = true

    @MainThread
    protected abstract fun shouldFetch(data: LocalType?): Boolean

    @WorkerThread
    protected abstract suspend fun loadFromCache(): LocalType?

    @WorkerThread
    protected abstract suspend fun createCall(): BaseResponse<RemoteType>

}