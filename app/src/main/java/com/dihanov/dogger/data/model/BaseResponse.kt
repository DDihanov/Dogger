package com.dihanov.dogger.data.model

data class BaseResponse<T>(val status: String, val code: Int, val message: T)