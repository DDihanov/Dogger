package com.dihanov.catsearch.data.remote.model.cat

import com.squareup.moshi.Json

data class Breeds(
    @Json(name = "id")
    val breed: String
)