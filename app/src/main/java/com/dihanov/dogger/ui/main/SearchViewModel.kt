package com.dihanov.dogger.ui.main

import androidx.lifecycle.*
import com.dihanov.dogger.data.local.repository.DogRepository
import com.dihanov.dogger.data.local.repository.Resource
import com.dihanov.dogger.data.uimodel.GetDog
import com.dihanov.dogger.utils.Event

class SearchViewModel(private val repository: DogRepository) : ViewModel() {

    private val _dogImages = MutableLiveData<GetDog>()
    val dogImages = _dogImages.switchMap { model ->
        liveData {
            emit(Event(Resource.loading(null)))
            emit(Event(repository.getDog(model.breed, model.limit)))
        }
    }

    fun searchForDogs(getDog: GetDog) {
        _dogImages.value = getDog
    }
}
