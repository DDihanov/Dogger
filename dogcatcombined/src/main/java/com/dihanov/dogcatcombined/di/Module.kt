package com.dihanov.dogcatcombined.di

import com.dihanov.dogcatcombined.domain.GetCombinedCatsAndDogsWithRandomBreedUseCase
import com.dihanov.dogcatcombined.ui.combined.CombinedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val combinedViewModelModule = module {
    viewModel {
        CombinedViewModel(get())
    }
}

val combinedUseCaseModule = module {
    factory {
        GetCombinedCatsAndDogsWithRandomBreedUseCase(
            get(),
            get()
        )
    }
}