package com.dihanov.catsearch.ui.main

import androidx.lifecycle.*
import com.dihanov.base_domain.Resource
import com.dihanov.catsearch.domain.CatsSearchUseCase
import com.dihanov.catsearch.domain.GetCachedCatsUseCase
import com.dihanov.catsearch.ui.uimodel.GetCat
import com.dihanov.util.Event
import kotlinx.coroutines.Dispatchers

class CatSearchViewModel(
    private val cachedCatsUseCase: GetCachedCatsUseCase,
    private val catsSearchUseCase: CatsSearchUseCase
) : ViewModel() {

    private val _catImages = MutableLiveData<GetCat>()

    val catImages = _catImages.switchMap { model ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(
                Event(
                    Resource.loading(
                        cachedCatsUseCase.execute(GetCachedCatsUseCase.Params(model.breed)).data!!
                    )
                )
            )
            emit(
                Event(
                    catsSearchUseCase.execute(
                        CatsSearchUseCase.Params(model.breed, model.limit, true)
                    )
                )
            )
        }
    }

    fun searchForCats(getCat: GetCat) {
        _catImages.value = getCat
    }
}
