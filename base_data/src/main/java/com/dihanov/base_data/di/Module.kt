package com.dihanov.base_data.di

import androidx.room.Room
import com.dihanov.base_data.BuildConfig
import com.dihanov.base_data.data.local.AppDb
import com.dihanov.base_data.data.local.mapper.CatMapper
import com.dihanov.base_data.data.local.mapper.DogMapper
import com.dihanov.base_data.data.remote.ApiEndpoint
import com.dihanov.base_data.data.remote.CatApiService
import com.dihanov.base_data.data.remote.DogApiService
import com.dihanov.base_data.data.remote.model.dog.BreedDeserializer
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val daoModule = module {
    single { get<AppDb>().dogDao() }
    single { get<AppDb>().catDao() }
}

val dogApiModule = module {
    fun provideDogApi(retrofit: Retrofit): DogApiService {
        return retrofit.create(DogApiService::class.java)
    }

    single { provideDogApi(get(qualifier("dogRetrofit"))) }
}

val catApiModule = module {
    fun provideCatApi(retrofit: Retrofit): CatApiService {
        return retrofit.create(CatApiService::class.java)
    }

    single { provideCatApi(get(qualifier("catRetrofit"))) }
}

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDb::class.java,
            "local_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
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

val mapperModule = module {
    single { DogMapper() }
    single { CatMapper() }
}