package com.dihanov.dogger.di

import androidx.room.Room
import com.dihanov.dogger.BuildConfig
import com.dihanov.dogger.data.local.db.AppDb
import com.dihanov.dogger.data.local.mapper.DogMapper
import com.dihanov.dogger.data.local.repository.DogRepository
import com.dihanov.dogger.data.local.usecase.DogsSearchUseCase
import com.dihanov.dogger.data.remote.ApiEndpoint
import com.dihanov.dogger.data.remote.ApiService
import com.dihanov.dogger.ui.main.SearchViewModel
import com.dihanov.dogger.utils.KeyboardManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }
}

val utilModule = module {
    single { KeyboardManager() }
}

val repositoryModule = module {
    single {
        DogRepository(get(), get())
    }
}

val useCaseModule = module {
    factory { DogsSearchUseCase(get(), get()) }
}

val apiModule = module {
    fun provideUseApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    single { provideUseApi(get()) }
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

val mapperModule = module {
    single { DogMapper() }
}

val retrofitModule = module {
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
        return Retrofit.Builder()
            .baseUrl(ApiEndpoint.ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}