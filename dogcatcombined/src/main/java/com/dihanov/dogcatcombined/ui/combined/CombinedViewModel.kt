package com.dihanov.dogcatcombined.ui.combined

import androidx.lifecycle.*
import com.dihanov.base_domain.Resource
import com.dihanov.dogcatcombined.domain.GetCombinedCatsAndDogsWithRandomBreedUseCase
import com.dihanov.dogcatcombined.ui.combined.uimodel.CombinedDogCat
import com.dihanov.util.Event
import kotlinx.coroutines.Dispatchers

class CombinedViewModel(private val getCombinedCatsAndDogsWithRandomBreedUseCase: GetCombinedCatsAndDogsWithRandomBreedUseCase) : ViewModel() {
    private val _combinedDogsCats = MutableLiveData<CombinedDogCat>()
    val combinedDogsCats = _combinedDogsCats.switchMap { model ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Event(Resource.loading(null)))
            emit(
                Event(getCombinedCatsAndDogsWithRandomBreedUseCase.execute(
                GetCombinedCatsAndDogsWithRandomBreedUseCase.Params(model.randomBreedsToFetch, model.numberOfImagesPerBreed)))
            )
        }
    }

    fun getCombinedDogsAndCats(model: CombinedDogCat) {
        _combinedDogsCats.value = model
    }
}