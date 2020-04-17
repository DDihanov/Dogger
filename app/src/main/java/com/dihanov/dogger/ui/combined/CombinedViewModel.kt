package com.dihanov.dogger.ui.combined

import androidx.lifecycle.*
import com.dihanov.base_domain.domain.Resource
import com.dihanov.dogger.domain.GetCombinedCatsAndDogsWithRandomBreedUseCase
import com.dihanov.dogger.ui.combined.uimodel.CombinedDogCat
import kotlinx.coroutines.Dispatchers

class CombinedViewModel(private val getCombinedCatsAndDogsWithRandomBreedUseCase: GetCombinedCatsAndDogsWithRandomBreedUseCase) : ViewModel() {
    private val _combinedDogsCats = MutableLiveData<CombinedDogCat>()
    val combinedDogsCats = _combinedDogsCats.switchMap { model ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(getCombinedCatsAndDogsWithRandomBreedUseCase.execute(
                GetCombinedCatsAndDogsWithRandomBreedUseCase.Params(model.randomBreedsToFetch, model.numberOfImagesPerBreed)))
        }
    }

    fun getCombinedDogsAndCats(model: CombinedDogCat) {
        _combinedDogsCats.value = model
    }
}