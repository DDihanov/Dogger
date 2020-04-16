package com.dihanov.dogger.ui.main

import androidx.lifecycle.*
import com.dihanov.dogger.data.local.repository.DogRepository
import com.dihanov.dogger.data.local.repository.Resource
import com.dihanov.dogger.data.local.usecase.DogsSearchUseCase
import com.dihanov.dogger.data.uimodel.GetDog
import com.dihanov.dogger.utils.Event
import kotlinx.coroutines.Dispatchers

class SearchViewModel(private val repository: DogRepository, private val dogsSearchUseCase: DogsSearchUseCase) : ViewModel() {

    private val _dogImages = MutableLiveData<GetDog>()
//    val dogImages = _dogImages.switchMap { model ->
//        liveData {
//            emit(Event(Resource.loading(null)))
//            emit(Event(repository.getDog(model.breed, model.limit)))
//        }
//    }

    val dogImages = _dogImages.switchMap { model ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Event(Resource.loading(repository.getDogsFromCache(model.breed))))
            emit(Event(dogsSearchUseCase.execute(DogsSearchUseCase.Params(model.breed, model.limit, true))))
        }
    }

    fun searchForDogs(getDog: GetDog) {
        _dogImages.value = getDog
    }
}
