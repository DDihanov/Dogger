package com.dihanov.base_data.data.remote.model.cat

import com.squareup.moshi.Json

data class Breeds(
    @Json(name = "id")
    val breed: String
)