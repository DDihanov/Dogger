package com.dihanov.dogger.data.local.repository

import com.dihanov.dogger.data.model.BaseResponse
import java.util.*


//can also be used
abstract class BaseRepository<RemoteType, LocalType> {
    abstract suspend fun writeToDb(toWrite: RemoteType)

    abstract suspend fun readFromDb(): LocalType

    abstract fun shouldFetch(): Boolean

    protected suspend fun handleRequestNoCache(requestFunc: suspend () -> BaseResponse<RemoteType>): Resource<RemoteType> {
        return try {
            val response = requestFunc.invoke()
            return parseResponseNoCache(response)
        } catch (he: Exception) {
            Resource.error(he.message ?: "error", null)
        }
    }

    protected suspend fun handleRequest(requestFunc: suspend () -> BaseResponse<RemoteType>): Resource<LocalType> {
        return try {
            val response = requestFunc.invoke()
            return parseResponse(response)
        } catch (he: Exception) {
            Resource.error(he.message ?: "error", readFromDb())
        }
    }

    private suspend fun parseResponseNoCache(response: BaseResponse<RemoteType>): Resource<RemoteType> {
        val status = Status.valueOf(response.status.toUpperCase(Locale.getDefault()))
        return when (status) {
            Status.SUCCESS -> {
                Resource.success(response.message)
            }
            else -> Resource.error(response.message.toString(), null)
        }
    }

    private suspend fun parseResponse(response: BaseResponse<RemoteType>): Resource<LocalType> {
        val status = Status.valueOf(response.status.toUpperCase(Locale.getDefault()))
        return when (status) {
            Status.SUCCESS -> {
                val payload = response.message
                writeToDb(payload)
                Resource.success(readFromDb())
            }
            else -> Resource.error(response.message.toString(), readFromDb())
        }
    }
}