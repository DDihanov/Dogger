package com.dihanov.dogsearch.data.remote.model.dog

data class DogSearch(val status: String, val code: Int? = 0, val message: List<String>)