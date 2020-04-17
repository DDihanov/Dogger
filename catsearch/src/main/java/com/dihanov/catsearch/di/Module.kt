package com.dihanov.catsearch.di

import com.dihanov.catsearch.data.local.repository.CatRepository
import com.dihanov.catsearch.domain.CatsSearchUseCase
import com.dihanov.catsearch.domain.GetCachedCatsUseCase
import com.dihanov.catsearch.ui.main.CatSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

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