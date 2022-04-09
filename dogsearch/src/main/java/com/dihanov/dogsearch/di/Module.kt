package com.dihanov.dogsearch.di

import com.dihanov.dogsearch.BuildConfig
import com.dihanov.dogsearch.data.DogRepository
import com.dihanov.dogsearch.data.mapper.DogMapper
import com.dihanov.dogsearch.data.remote.ApiEndpoint
import com.dihanov.dogsearch.data.remote.DogApiService
import com.dihanov.dogsearch.data.remote.model.dog.BreedDeserializer
import com.dihanov.dogsearch.domain.DogsSearchUseCase
import com.dihanov.dogsearch.domain.GetCachedDogsUseCase
import com.dihanov.dogsearch.ui.main.DogSearchViewModel
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val dogRepository = module {
    single { DogRepository(get(), get()) }
}

val dogViewModelModule = module {
    viewModel {
        DogSearchViewModel(get(), get())
    }
}

val dogUseCaseModule = module {
    factory { DogsSearchUseCase(get(), get()) }
    factory { GetCachedDogsUseCase(get()) }
}

val dogRetrofitModule = module {
    fun provideHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val interceptor1 = HttpLoggingInterceptor()
        interceptor1.level = HttpLoggingInterceptor.Level.HEADERS

        val okHttpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(interceptor)
            okHttpClientBuilder.addInterceptor(interceptor1)
        }

        // api key interceptor if needed
//        okHttpClientBuilder.addInterceptor { chain ->
//            val original  = chain.request()
//
//
//            val requestBuilder: Request.Builder = original.newBuilder()
//                .header("x-api-key", BuildConfig.API_KEY)
//
//            val request = requestBuilder.build()
//            chain.proceed(request)
//        }

        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder().add(BreedDeserializer()).build()

        return Retrofit.Builder()
            .baseUrl(ApiEndpoint.DOG_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    single(qualifier("dogModuleHttpClient")) { provideHttpClient() }
    single(qualifier("dogRetrofit")) { provideRetrofit(get(qualifier("dogModuleHttpClient"))) }
}

val dogMapperModule = module {
    single { DogMapper() }
}

val dogApiModule = module {
    fun provideDogApi(retrofit: Retrofit): DogApiService {
        return retrofit.create(DogApiService::class.java)
    }

    single { provideDogApi(get(qualifier("dogRetrofit"))) }
}