package com.dihanov.catsearch.di

import com.dihanov.catsearch.BuildConfig
import com.dihanov.catsearch.data.CatRepository
import com.dihanov.catsearch.data.mapper.CatMapper
import com.dihanov.catsearch.data.remote.ApiEndpoint
import com.dihanov.catsearch.data.remote.CatApiService
import com.dihanov.catsearch.domain.CatsSearchUseCase
import com.dihanov.catsearch.domain.GetCachedCatsUseCase
import com.dihanov.catsearch.ui.main.CatSearchViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val catRepository = module {
    single { CatRepository(get(), get()) }
}

val catViewModelModule = module {
    viewModel {
        CatSearchViewModel(get(), get())
    }
}

val catUseCaseModule = module {
    factory { CatsSearchUseCase(get(), get()) }
    factory { GetCachedCatsUseCase(get()) }
}

val catApiModule = module {
    fun provideCatApi(retrofit: Retrofit): CatApiService {
        return retrofit.create(CatApiService::class.java)
    }

    single { provideCatApi(get(qualifier("catRetrofit"))) }
}

val catMapperModule = module {
    single { CatMapper() }
}

val catRetrofitModule = module {
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


        okHttpClientBuilder.addInterceptor { chain ->
            val original  = chain.request()


            val requestBuilder: Request.Builder = original.newBuilder()
                .header("x-api-key", BuildConfig.CAT_API_KEY)

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return okHttpClientBuilder.build()
    }


    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiEndpoint.CAT_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    single(qualifier("catModuleHttpClient")) { provideHttpClient() }
    single(qualifier("catRetrofit")) { provideRetrofit(get(qualifier("catModuleHttpClient"))) }
}