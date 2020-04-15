package com.dihanov.dogger.data.remote

import com.dihanov.dogger.data.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    companion object {
        const val ENDPOINT = ApiEndpoint.ENDPOINT
    }

    @GET(ENDPOINT + "breed/{type}/images/random/{limit}")
    suspend fun getDog(@Path(value = "type") breed: String, @Path(value = "limit") limit: Int): BaseResponse<List<String>>
}