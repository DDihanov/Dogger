package com.dihanov.base_data.data.remote

import com.dihanov.base_data.data.remote.model.dog.DogBreedSearch
import com.dihanov.base_data.data.remote.model.dog.DogSearch
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    companion object {
        const val ENDPOINT = ApiEndpoint.DOG_ENDPOINT
    }

    @GET(ENDPOINT + "breed/{type}/images/random/{limit}")
    suspend fun getDog2(@Path(value = "type") breed: String, @Path(value = "limit") limit: Int): DogSearch

    @GET(ENDPOINT + "breeds/list/all")
    suspend fun getBreeds(): DogBreedSearch
}