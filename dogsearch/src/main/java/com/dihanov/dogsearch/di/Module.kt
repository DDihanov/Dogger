package com.dihanov.dogsearch.di

import com.dihanov.dogsearch.data.local.repository.DogRepository
import com.dihanov.dogsearch.domain.DogsSearchUseCase
import com.dihanov.dogsearch.domain.GetCachedDogsUseCase
import com.dihanov.dogsearch.ui.main.DogSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dogRepository = module {
    single { DogRepository(get(), get())}
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