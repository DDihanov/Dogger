package com.dihanov.dogger.di

import com.dihanov.dogger.domain.GetCombinedCatsAndDogsWithRandomBreedUseCase
import com.dihanov.dogger.ui.combined.CombinedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val combinedViewModelModule = module {
    viewModel {
        CombinedViewModel(get())
    }
}

val combinedUseCaseModule = module {
    factory { GetCombinedCatsAndDogsWithRandomBreedUseCase(get(), get()) }
}