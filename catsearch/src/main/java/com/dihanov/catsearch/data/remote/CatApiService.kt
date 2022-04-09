package com.dihanov.catsearch.data.remote

import com.dihanov.catsearch.data.remote.model.cat.Breeds
import com.dihanov.catsearch.data.remote.model.cat.CatSearch
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {
    companion object {
        const val ENDPOINT = ApiEndpoint.CAT_ENDPOINT
    }

    @GET(ENDPOINT + "images/search")
    suspend fun getCats(@Query(value = "breed_id") breed: String, @Query(value = "limit") limit: Int): List<CatSearch>

    @GET(ENDPOINT + "breeds")
    suspend fun getCatBreeds(@Query("limit") limit: Int): List<Breeds>
}